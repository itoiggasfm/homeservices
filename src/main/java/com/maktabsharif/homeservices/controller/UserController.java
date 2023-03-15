package com.maktabsharif.homeservices.controller;

import com.maktabsharif.homeservices.domain.User;
import com.maktabsharif.homeservices.domain.enumeration.Role;
import com.maktabsharif.homeservices.service.ClientService;
import com.maktabsharif.homeservices.service.ExpertService;
import com.maktabsharif.homeservices.service.UserService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService userService;
    private final ExpertService expertService;
    private final ClientService clientService;

    public UserController(UserService userService,
                          ExpertService expertService,
                          ClientService clientService) {
        this.userService = userService;
        this.expertService = expertService;
        this.clientService = clientService;
    }

    @GetMapping("/changePassword/{userId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'EXPERT', 'CLIENT')")
    public User changePassword(@PathVariable Long userId, @RequestParam(value = "oldPassword", required = false) String oldPassword, @RequestParam(value = "newPassword", required = false) String newPassword, @RequestParam(value = "newPasswordConfirmation", required = false) String newPasswordConfirmation) throws Exception {
//        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return userService.changePassword(userId, oldPassword, newPassword, newPasswordConfirmation);
    }

    @GetMapping("/findById/{userId}")
    public User findById(@PathVariable Long userId) throws Exception {
        return userService.findById(userId);
    }

    @GetMapping("/findByUsername")
    public User findByUsername(@RequestParam(value = "username", required = false) String username) throws Exception {
        return userService.findByUsername(username);
    }

    @GetMapping("/findAll")
    public List<User> findAll() throws Exception {
        return userService.findAll();
    }

    @DeleteMapping("/deleteById/{userId}")
    @PreAuthorize("hasRole('ADMIN')")
    public String deleteById(@PathVariable Long userId) throws Exception {
        userService.deleteById(userId);
        return "OK";
    }


    @PostMapping("/uploadImage/{userId}")
    public void uploadImage(@PathVariable Long userId, @RequestParam("imageFile") MultipartFile imageFile) throws Exception {

        userService.photo(userId, imageFile);
    }

    @GetMapping("/activateAccount/{userId}")
    public String activateAccount(@PathVariable Long userId) throws Exception {

        if (userService.findById(userId).getRole().equals(Role.ROLE_EXPERT)){
            if(expertService.activateAccount(userId))
                return "Account is appending approval.";
        }
        if (userService.findById(userId).getRole().equals(Role.ROLE_CLIENT)){
            if(clientService.activateAccount(userId))
                return "Account is activated.";
        }

        return "Account not activated.";
    }

    @GetMapping("/getBalance")
    public Double getBalance(){
        return userService.userBalance();
    }

}
