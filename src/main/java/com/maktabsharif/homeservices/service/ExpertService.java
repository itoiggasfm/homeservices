package com.maktabsharif.homeservices.service;

import com.maktabsharif.homeservices.Exceptions.*;
import com.maktabsharif.homeservices.domain.Expert;
import com.maktabsharif.homeservices.domain.User;
import com.maktabsharif.homeservices.domain.Wallet;
import com.maktabsharif.homeservices.domain.enumeration.ExpertStatus;
import com.maktabsharif.homeservices.domain.enumeration.Role;
import com.maktabsharif.homeservices.repository.ExpertRepository;
import com.maktabsharif.homeservices.validation.Validators;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Date;

@Service
public class ExpertService {

    private final ExpertRepository expertRepository;
    private final UserService userService;
    private final Validators validator;
    private final BCryptPasswordEncoder passwordEncoder;
    private final EmailDetailsService emailDetailsService;

    public ExpertService(
            ExpertRepository expertRepository,
            UserService userService,
            BCryptPasswordEncoder passwordEncoder,
            EmailDetailsService emailDetailsService) {
        this.expertRepository = expertRepository;
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.emailDetailsService = emailDetailsService;
        validator = new Validators();
    }


    public User create(Expert expert) throws Exception {

        User user = userService.findByUsername(expert.getUsername());

        if(user != null)
            throw new DuplicateUsernameException("Username already exists.");

        if(!validator.validatePasswordPolicy(expert.getPassword()))
            throw new PasswordPolicyException("Password does not meet the password policy requirement." +
                    "Password must contain at least 8 characters including letters and numbers.");

        if(!validator.validateEmail(expert.getEmail()))
            throw new InvalidEmailException("Invalid Email address");

        Wallet wallet = new Wallet();
        wallet.setBalance(0d);
        expert.setWallet(wallet);
        expert.setPassword(passwordEncoder.encode(expert.getPassword() ));
        expert.setEnabled(false);
        expert.setRole(Role.ROLE_EXPERT);
        expert.setExpertStatus(ExpertStatus.NEW);
        expert.setExpertPoint(0);
        expert.setTempEmail(expert.getEmail());
        expert.setEmail(null);

        Expert newExpert = expertRepository.save(expert);
        emailDetailsService.setActivationEmailAndSend(newExpert);
        return newExpert;

    }

    public Expert findById(Long id){
        return expertRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found."));
    }

    public Expert update(Expert expert) {
        return expertRepository.save(expert);
    }

    public Expert singIn(String username, String password) {
        Expert expert = expertRepository.findByUsername(username)
                .orElseThrow(() ->  new UserNotFoundException("User not found."));
        if(!expert.isEnabled())
            throw new UserIsActiveException("User isn't active.");
        if(!(expert.getPassword().equals(password) && expert.getExpertStatus().equals(ExpertStatus.APPROVED)))
            throw new IncorrectUsernameOrPasswordException("Incorrect username or password.");

        return expert;
    }

    public boolean activateAccount(Long userId){
        Expert expert = expertRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found."));
        if(expert.isEnabled())
            throw new AlreadyActivatedAccountException("Account is already activated.");

        if(userService.existsByEmail(expert.getTempEmail()))
            throw new DuplicateEmailException("Email already exists.");

        expert.setEnabled(true);
        expert.setExpertStatus(ExpertStatus.PENDING_APPROVAL);
        expert.setEmail(expert.getTempEmail());
        expert.setTempEmail(null);

        expertRepository.save(expert);

        return true;
    }

}
