package com.maktabsharif.homeservices.service;
import com.maktabsharif.homeservices.Exceptions.*;
import com.maktabsharif.homeservices.domain.Services;
import com.maktabsharif.homeservices.domain.enumeration.ExpertStatus;
import com.maktabsharif.homeservices.domain.enumeration.UserRole;
import com.maktabsharif.homeservices.validation.Validators;
import com.maktabsharif.homeservices.domain.User;
import com.maktabsharif.homeservices.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class UserService {


    private final UserRepository userRepository;
    private final Validators validator;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
        this.validator = new Validators();
    }


    public User create(User user) throws Exception {

        Optional<User> foundUser = userRepository.findByUsername(user.getUsername());

        if(foundUser.isPresent())
            throw new DuplicateUsernameException("Username already exists.");

        if(!validator.validatePasswordPolicy(user.getPassword()))
            throw new PasswordPolicyException("Password does not meet the password policy requirement." +
                    "Password must contain at least 8 characters including letters and numbers.");

         boolean existByEmail = userRepository.existsByEmail(user.getEmail());
        if(existByEmail)
            throw new DuplicateEmailException("Email already exists.");

        if(!validator.validateEmail(user.getEmail()))
            throw new InvalidEmailException("Invalid Email address");

//        user.setRegisterDate(Timestamp.valueOf("registerDte"));

       return userRepository.save(user);

    }

    public User findById(Long id){
        return userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found."));
    }

    public User findByUsername(String username){
        return userRepository.findByUsername(username).
                orElseThrow(() -> new UserNotFoundException("User not found."));
    }

    public User singIn(String username, String password) {
        Optional<User> user = userRepository.findByUsername(username);
        if( user.isPresent() && user.get().getPassword().equals(password) && (user.get().getUserRole().equals(UserRole.CLIENT) || (user.get().getUserRole().equals(UserRole.EXPERT) && user.get().getExpertStatus().equals(ExpertStatus.APPROVED)) || user.get().getUserRole().equals(UserRole.ADMIN)))
            return user.get();
        else
            throw new IncorrectUsernameOrPasswordException("Incorrect username or password.");
    }

    public User updateUser(User user) {
        return userRepository.save(user);
    }

    public User changePassword(Long id, String oldPassword, String newPassword, String newPasswordConfirmation) {
        Optional<User> passwordChangingUser = userRepository.findById(id);
        if(passwordChangingUser.isEmpty())
            throw new UserNotFoundException("User not found");
        if(!passwordChangingUser.get().getPassword().equals(oldPassword))
            throw new IncorrectUsernameOrPasswordException("Incorrect password.");
        if(!newPassword.equals(newPasswordConfirmation))
            throw new PasswordsNotMatchException("Passwords do not match.");
        if(!validator.validatePasswordPolicy(newPassword))
            throw new PasswordPolicyException("Password does not meet the password policy requirement." +
                    "Password must contain at least 8 characters including letters and numbers.");
        passwordChangingUser.get().setPassword(newPassword);

        return userRepository.save(passwordChangingUser.get());
    }

    public User approveNewExperts(Long userId) {
        Optional<User> foundUser = userRepository.findById(userId);
        if(!foundUser.isPresent())
            throw new UserNotFoundException("User not found.");
            if(!foundUser.get().getUserRole().equals(UserRole.EXPERT))
                throw new NotExpertUserException(foundUser.get().getName() + " " + foundUser.get().getFamilyName()
                        + " has not registered as an expert.");
                if(foundUser.get().getExpertStatus().equals(ExpertStatus.APPROVED))
                    throw new ApprovedExpertException(foundUser.get().getName() + " " + foundUser.get().getFamilyName()
                            + " has already been approved as expert.");

                foundUser.get().setExpertStatus(ExpertStatus.APPROVED);
                 return userRepository.save(foundUser.get());
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

//    public User addExpertToServices(User user, Long servicesId){
//        Optional<User> foundUser = userRepository.findByUsername(user.getUsername());
//        if(foundUser.isEmpty())
//            throw new UserNotFoundException("User not found.");
//        if(!foundUser.get().getUserRole().equals(UserRole.EXPERT))
//                throw new NotExpertUserException(foundUser.get().getName() + " " + foundUser.get().getFamilyName()
//                        + " has not registered as an expert.");
//        if(!foundUser.get().getExpertStatus().equals(ExpertStatus.APPROVED))
//                    throw new NotApprovedExpertException(foundUser.get().getName() + " " + foundUser.get().getFamilyName()
//                            + " has not been approved as expert."
//                    );
//        List<Services> servicesList = servicesService.findAll();
//        List<Services> foundUserServices = foundUser.get().getServices();
//        if(!(servicesList.stream().filter(t -> Objects.equals(t.getId(), servicesId)).findFirst()).isPresent())
//            throw new ServicesNotFoundException("Service with ID " +
//                    servicesId +
//                    " not found."
//                        );
//
//        if((foundUserServices.stream().filter(t -> Objects.equals(t.getId(), servicesId)).findFirst()).isPresent())
//            throw new ServicesNotFoundException("Service with ID " +
//                    servicesId +
//                    " already assigned to " +
//                    foundUser.get().getName() + " " + foundUser.get().getFamilyName()
//            );
//        foundUserServices.add(Services.builder()
//                            .id(servicesId)
//                            .build());
//        foundUser.get().setServices(foundUserServices);
//
//        return userRepository.save(foundUser.get());
//    }
//
//    public User removeExpertFromServices(User user, Long servicesId){
//        Optional<User> foundUser = userRepository.findByUsername(user.getUsername());
//        if(foundUser.isEmpty())
//            throw new UserNotFoundException("User not found.");
//        if(!foundUser.get().getUserRole().equals(UserRole.EXPERT))
//            throw new NotExpertUserException(foundUser.get().getName() + " " + foundUser.get().getFamilyName()
//                    + " has not registered as an expert.");
//        if(!foundUser.get().getExpertStatus().equals(ExpertStatus.APPROVED))
//            throw new NotApprovedExpertException(foundUser.get().getName() + " " + foundUser.get().getFamilyName()
//                    + " has not been approved as expert.");
//        List<Services> servicesList = servicesService.findAll();
//        List<Services> foundUserServices = foundUser.get().getServices();
//        if(!(servicesList.stream().filter(t -> Objects.equals(t.getId(), servicesId)).findFirst()).isPresent())
//            throw new ServicesNotFoundException("Service with ID " +
//                    servicesId +
//                    " not found."
//            );
//        if(!(foundUserServices.stream().filter(t -> Objects.equals(t.getId(), servicesId)).findFirst()).isPresent())
//            throw new ServicesNotFoundException("Service with ID " +
//                    servicesId +
//                    " has not been assigned to " +
//                    foundUser.get().getName() + " " + foundUser.get().getFamilyName()
//            );
//        foundUserServices.remove((foundUserServices.stream().filter(t -> Objects.equals(t.getId(), servicesId)).findFirst()).get());
//        foundUser.get().setServices(foundUserServices);
//
//        return userRepository.save(foundUser.get());
//    }

}
