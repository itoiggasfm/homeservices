package com.maktabsharif.homeservices.service;
import com.maktabsharif.homeservices.Exceptions.*;
import com.maktabsharif.homeservices.controller.UserController;
import com.maktabsharif.homeservices.domain.*;
import com.maktabsharif.homeservices.domain.enumeration.ExpertStatus;
import com.maktabsharif.homeservices.domain.enumeration.Role;
import com.maktabsharif.homeservices.repository.UserRepositoryImpl;
import com.maktabsharif.homeservices.utilities.FileUploadUtil;
import com.maktabsharif.homeservices.validation.Validators;
import com.maktabsharif.homeservices.repository.UserRepository;
import org.mapstruct.control.MappingControl;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {


    private final UserRepository userRepository;
    private final Validators validator;
    private final BCryptPasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository,
                       BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.validator = new Validators();
    }

    public  boolean existsByEmail(String email){
        return userRepository.existsByEmail(email);
    }

    public User findById(Long id){
        return userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found."));
    }

    public User findByUsername(String username){
        Optional<User> user = userRepository.findByUsername(username);
        if(user.isPresent())
            return user.get();
        else
            return null;
        }

    public User changePassword(Long id, String oldPassword, String newPassword, String newPasswordConfirmation) {
        User passwordChangingUser = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
        if(!passwordChangingUser.getPassword().equals(passwordEncoder.encode(oldPassword)))
            throw new IncorrectUsernameOrPasswordException("Incorrect password.");
        if(!newPassword.equals(newPasswordConfirmation))
            throw new PasswordsNotMatchException("Passwords do not match.");
        if(!validator.validatePasswordPolicy(newPassword))
            throw new PasswordPolicyException("Password does not meet the password policy requirement." +
                    "Password must contain at least 8 characters including letters and numbers.");
        passwordChangingUser.setPassword(passwordEncoder.encode(newPassword));

        return userRepository.save(passwordChangingUser);
    }

    public List<User> findAll() {
        List<User> users = userRepository.findAll();
        return users;
    }

    public void deleteById(Long userId) {
        Optional<User> user = userRepository.findById(userId);
        if(user.isEmpty())
            throw new UserNotFoundException("User not found.");
        userRepository.deleteById(userId);
    }

    public void photo(Long userId, MultipartFile imageFile) throws Exception {
        if(imageFile.isEmpty())
            throw new NoFileUploadedException("No file uploaded.");
        if(imageFile.getSize()/1024 > 300)
            throw new LargeImmageFileException("Image file is greater than 300 KB.");
        if(validator.validateImageExtension(imageFile.getOriginalFilename()))
            throw new IncorrectImageFileExtensionException("Image file is not of jpg format.");

        String fileName = StringUtils.cleanPath(imageFile.getOriginalFilename());
        Optional<User> user = userRepository.findById(userId);
        user.get().setProfilePhotoName(fileName);
        userRepository.save(user.get());

        String uploadDir = "user-photos/" + userId;

        FileUploadUtil.saveFile(uploadDir, fileName, imageFile);

    }

    public List<User> findUserByUserRoleAndNameAndFamilyNameAndEmailAndExpertPoint(User user, String subserviceTitle, Integer lowExpertPoint, Integer highExpertPoint){
        return userRepository.findUserByUserRoleAndNameAndFamilyNameAndEmailAndExpertPoint(user, subserviceTitle, lowExpertPoint, highExpertPoint);
    }

    public Double userBalance(){
        User user1 = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return user1.getWallet().getBalance();
    }


}
