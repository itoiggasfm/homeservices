package com.maktabsharif.homeservices.controller;

import com.maktabsharif.homeservices.domain.User;
import com.maktabsharif.homeservices.domain.enumeration.UserRole;
import com.maktabsharif.homeservices.dto.UserDto;
import com.maktabsharif.homeservices.mapper.UserMapper;
import com.maktabsharif.homeservices.service.UserService;
import com.maktabsharif.homeservices.validation.Captcha;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.view.RedirectView;

import java.sql.Timestamp;
import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/create")
    public User create(@RequestBody UserDto userDto) throws Exception {
        User user = UserMapper.INSTANCE.dtoToModel(userDto);
           return userService.create(user);
    }

    @GetMapping("/signIn")
    public User signIn(@RequestParam(value = "username", required = false) String username, @RequestParam(value = "password", required = false) String password) throws Exception {
        return userService.singIn(username, password);
    }

    @GetMapping("/changePassword/{userId}")
    public User changePassword(@PathVariable Long userId, @RequestParam(value = "oldPassword", required = false) String oldPassword, @RequestParam(value = "newPassword", required = false) String newPassword, @RequestParam(value = "newPasswordConfirmation", required = false) String newPasswordConfirmation) throws Exception {
        return userService.changePassword(userId, oldPassword, newPassword, newPasswordConfirmation);
    }

    @PutMapping("/update")
    public User update(@RequestBody UserDto userDto) throws Exception {
        User user = UserMapper.INSTANCE.dtoToModel(userDto);
        return userService.update(user);
    }

    @GetMapping("/findById/{userId}")
    public User findById(@PathVariable Long userId) throws Exception {
        return userService.findById(userId);
    }

    @GetMapping("/approveNewExperts/{userId}")
    public User approveNewExperts(@PathVariable Long userId) throws Exception {
        return userService.approveNewExperts(userId);
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
    public String deleteById(@PathVariable Long userId) throws Exception {
        userService.deleteById(userId);
        return "OK";
    }

    @GetMapping("/searchForClientOrExpert")
    public List<User> SearchForClientOrExpert(@RequestParam(value = "UserRole", required = false) UserRole userRole) throws Exception {
        return userService.searchForClientOrExpert(userRole);
    }

    @GetMapping("/searchForName")
    public List<User> SearchForName(@RequestParam(value = "name", required = false) String name) throws Exception {
        return userService.searchForName(name);
    }

    @GetMapping("/searchForFamilyName")
    public List<User> SearchForFamilyName(@RequestParam(value = "familyName", required = false) String familyName) throws Exception {
        return userService.searchForFamilyName(familyName);
    }

    @GetMapping("/searchForEmail")
    public List<User> SearchForEmail(@RequestParam(value = "email", required = false) String email) throws Exception {
        return userService.searchForEmail(email);
    }

    @GetMapping("/SearchForExpertPoint")
    public List<User> SearchForExpertPoint(@RequestParam(value = "minPoint", required = false) Integer minPoint, @RequestParam(value = "maxPoint", required = false) Integer maxPoint) throws Exception {
        return userService.searchForExpertPoint(minPoint, maxPoint);
    }

    @GetMapping("/searchForExpertise")
    public List<User> searchForExpertise(@RequestParam(value = "expertise", required = false) String expertise) throws Exception {
        return userService.searchForExpertise(expertise);
    }

    @PostMapping("/uploadImage/{userId}")
    public void uploadImage(@PathVariable Long userId, @RequestParam("imageFile") MultipartFile imageFile) throws Exception {

        userService.photo(userId, imageFile);
    }

//    @GetMapping("/captcha")
//    public String generateCaptcha() throws Exception {
//        String captcha = Captcha.returnCaptchaText();
//        return captcha;
//    }

    @GetMapping("/activateAccount/{userId}")
    public String activateAccount(@PathVariable Long userId) throws Exception {
        boolean isActive = userService.activateAccount(userId);
        if(isActive){
            if (userService.findById(userId).getUserRole().equals(UserRole.EXPERT))
                return "Account is appending approval.";
            else
                return "Account is activated.";
        } else
            return "Account not activated.";
    }


}
