package com.maktabsharif.homeservices.service;

import com.maktabsharif.homeservices.Exceptions.*;
import com.maktabsharif.homeservices.domain.*;
import com.maktabsharif.homeservices.domain.enumeration.ExpertStatus;
import com.maktabsharif.homeservices.domain.enumeration.Role;
import com.maktabsharif.homeservices.repository.AdminRepository;
import com.maktabsharif.homeservices.validation.Validators;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Service
public class AdminService {

    private final AdminRepository adminRepository;
    private final ExpertService expertService;
    private final UserService userService;
    private final SubservicesService subservicesService;
    private final Validators validator;
    private final BCryptPasswordEncoder passwordEncoder;
    private final EmailDetailsService emailDetailsService;


    public AdminService(AdminRepository adminRepository,
                        ExpertService expertService,
                        UserService userService,
                        SubservicesService subservicesService,
                        BCryptPasswordEncoder passwordEncoder,
                        EmailDetailsService emailDetailsService) {
        this.adminRepository = adminRepository;
        this.expertService = expertService;
        this.userService = userService;
        this.subservicesService = subservicesService;
        this.passwordEncoder = passwordEncoder;
        this.emailDetailsService = emailDetailsService;
        validator = new Validators();
    }

    public User create(Admin admin) throws Exception {

        User user = userService.findByUsername(admin.getUsername());

        if(user != null)
            throw new DuplicateUsernameException("Username already exists.");

        if(!validator.validatePasswordPolicy(admin.getPassword()))
            throw new PasswordPolicyException("Password does not meet the password policy requirement." +
                    "Password must contain at least 8 characters including letters and numbers.");

        if(!validator.validateEmail(admin.getEmail()))
            throw new InvalidEmailException("Invalid Email address");

        if(userService.existsByEmail(admin.getEmail()))
            throw new DuplicateEmailException("Email already exists.");

        admin.setRegisterDate(new Timestamp(new Date().getTime()));
        Wallet wallet = new Wallet();
        wallet.setBalance(0d);
        admin.setWallet(wallet);
        admin.setPassword(passwordEncoder.encode(admin.getPassword() ));
        admin.setEnabled(false);
        admin.setRole(Role.ROLE_ADMIN);

        admin.setTempEmail(admin.getEmail());
        admin.setEmail(null);

        Admin newAdmin = adminRepository.save(admin);
        emailDetailsService.setActivationEmailAndSend(newAdmin);
        return newAdmin;

    }

    public Admin update(Admin admin) {
        return adminRepository.save(admin);
    }

    public Admin singIn(String username, String password) {
        Admin admin = adminRepository.findByUsername(username);
        if(admin == null)
                throw  new UserNotFoundException("User not found.");
        if(!admin.isEnabled())
            throw new UserIsActiveException("User isn't active.");
        if(!admin.getPassword().equals(passwordEncoder.encode(password)))
            throw new IncorrectUsernameOrPasswordException("Incorrect username or password.");

        return admin;
    }

    public Expert approveNewExperts(Long expertId) {

        Expert expert = expertService.findById(expertId);
        if(!expert.isEnabled())
            throw new NotActivatedUserException("User not activated.");
        if(!expert.getRole().equals(Role.ROLE_EXPERT))
            throw new NotExpertUserException(expert.getName() + " " + expert.getFamilyName()
                    + " has not registered as an expert.");
        if(expert.getExpertStatus().equals(ExpertStatus.APPROVED))
            throw new ApprovedExpertException(expert.getName() + " " + expert.getFamilyName()
                    + " has already been approved as expert.");

        expert.setExpertStatus(ExpertStatus.APPROVED);
        return expertService.update(expert);
    }

    public Expert addExpertToServices(Long expertId, Long servicesId){
        Expert foundExpert = expertService.findById(expertId);

        if(!foundExpert.getRole().equals(Role.ROLE_EXPERT))
            throw new NotExpertUserException(foundExpert.getName() + " " + foundExpert.getFamilyName()
                    + " has not registered as an expert.");

        if(!foundExpert.getExpertStatus().equals(ExpertStatus.APPROVED))
            throw new NotApprovedExpertException(foundExpert.getName() + " " + foundExpert.getFamilyName()
                    + " has not been approved as expert."
            );
        if(subservicesService.findById(servicesId) == null)
            throw new ServicesNotFoundException("Service with ID " + servicesId + " not found.");

        List<Subservices> foundExpertServices = foundExpert.getSubservices();
        if((foundExpertServices.stream().filter(t -> Objects.equals(t.getId(), servicesId)).findFirst()).isPresent())
            throw new ServicesNotFoundException("Service with ID " +
                    servicesId +
                    " already assigned to " +
                    foundExpert.getName() + " " + foundExpert.getFamilyName()
            );
        foundExpertServices.add(Subservices.builder()
                .id(servicesId)
                .build());
        foundExpert.setSubservices(foundExpertServices);

        return expertService.update(foundExpert);
    }

    public Expert removeExpertFromServices(Long expertId, Long servicesId){
        Expert foundExpert = expertService.findById(expertId);

        if(!foundExpert.getRole().equals(Role.ROLE_EXPERT))
            throw new NotExpertUserException(foundExpert.getName() + " " + foundExpert.getFamilyName()
                    + " has not registered as an expert.");

        if(!foundExpert.getExpertStatus().equals(ExpertStatus.APPROVED))
            throw new NotApprovedExpertException(foundExpert.getName() + " " + foundExpert.getFamilyName()
                    + " has not been approved as expert.");

        if(subservicesService.findById(servicesId) == null )
            throw new ServicesNotFoundException("Service " + servicesId + " not found.");

        List<Subservices> foundExpertServices = foundExpert.getSubservices();
        if(!(foundExpertServices.stream().filter(t -> Objects.equals(t.getId(), servicesId)).findFirst()).isPresent())
            throw new ServicesNotFoundException("Service " +
                    subservicesService.findById(servicesId).getSubserviceTitle() +
                    " has not been assigned to " +
                    foundExpert.getName() + " " + foundExpert.getFamilyName()
            );
        foundExpertServices.remove((foundExpertServices.stream().filter(t -> Objects.equals(t.getId(), servicesId)).findFirst()).get());
        foundExpert.setSubservices(foundExpertServices);

        return expertService.update(foundExpert);
    }
    public List<User> findUserByUserRoleAndNameAndFamilyNameAndEmailAndExpertPoint(User user, String subserviceTitle, Integer lowExpertPoint, Integer highExpertPoint){
        return userService.findUserByUserRoleAndNameAndFamilyNameAndEmailAndExpertPoint(user, subserviceTitle, lowExpertPoint, highExpertPoint);
    }


}
