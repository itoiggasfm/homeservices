package com.maktabsharif.homeservices.service;

import com.maktabsharif.homeservices.Exceptions.*;
import com.maktabsharif.homeservices.domain.Client;
import com.maktabsharif.homeservices.domain.User;
import com.maktabsharif.homeservices.domain.Wallet;
import com.maktabsharif.homeservices.domain.enumeration.ExpertStatus;
import com.maktabsharif.homeservices.domain.enumeration.Role;
import com.maktabsharif.homeservices.repository.ClientRepository;
import com.maktabsharif.homeservices.repository.UserRepository;
import com.maktabsharif.homeservices.validation.Validators;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Date;

@Service
public class ClientService {


    private final ClientRepository clientRepository;
    private final UserService userService;
    private final Validators validator;
    private final BCryptPasswordEncoder passwordEncoder;
    private final EmailDetailsService emailDetailsService;


    public ClientService(ClientRepository clientRepository,
                         UserService userService,
                         BCryptPasswordEncoder passwordEncoder,
                         EmailDetailsService emailDetailsService) {
        this.clientRepository = clientRepository;
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.emailDetailsService = emailDetailsService;
        validator = new Validators();
    }

    public User create(Client client) throws Exception {

        User user = userService.findByUsername(client.getUsername());

        if(user != null)
            throw new DuplicateUsernameException("Username already exists.");

        if(!validator.validatePasswordPolicy(client.getPassword()))
            throw new PasswordPolicyException("Password does not meet the password policy requirement." +
                    "Password must contain at least 8 characters including letters and numbers.");

        if(!validator.validateEmail(client.getEmail()))
            throw new InvalidEmailException("Invalid Email address");

        Wallet wallet = new Wallet();
        wallet.setBalance(0d);
        client.setWallet(wallet);
        client.setPassword(passwordEncoder.encode(client.getPassword() ));
        client.setEnabled(false);
        client.setRole(Role.ROLE_CLIENT);

        client.setTempEmail(client.getEmail());
        client.setEmail(null);

        Client newClient = clientRepository.save(client);
        emailDetailsService.setActivationEmailAndSend(newClient);
        return newClient;

    }

    public Client update(Client client) {
        return clientRepository.save(client);
    }

    public Client singIn(String username, String password) {
        Client client = clientRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("User not found."));

        if(!client.isEnabled())
            throw new UserIsActiveException("User isn't active.");
        if(!client.getPassword().equals(password))
            throw new IncorrectUsernameOrPasswordException("Incorrect username or password.");

        return client;
    }

    public boolean activateAccount(Long userId){
        Client client = (Client) userService.findById(userId);
        if(client == null)
            throw new UserNotFoundException("User not found.");
        if(client.isEnabled())
            throw new AlreadyActivatedAccountException("Account is already activated.");

        if(userService.existsByEmail(client.getTempEmail()))
            throw new DuplicateEmailException("Email already exists.");

        client.setEnabled(true);
        client.setEmail(client.getTempEmail());
        client.setTempEmail(null);

        clientRepository.save(client);

        return true;
    }

}
