//package com.maktabsharif.homeservices.service;
//
//import com.maktabsharif.homeservices.domain.User;
//import com.maktabsharif.homeservices.domain.Wallet;
//import com.maktabsharif.homeservices.domain.enumeration.ExpertStatus;
//import com.maktabsharif.homeservices.domain.enumeration.Role;
//import org.junit.jupiter.api.*;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//
//import java.util.List;
//import java.util.Objects;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//@SpringBootTest
//@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
//class UserServiceTest {
//    @Autowired
//    private UserService userService;
//    @Autowired
//    private SubservicesService subservicesService;
//
//
////    @Test
////    @Order(1)
////    void create() throws Exception {
////        userService.create(user);
////        User foundUser = userService.findByUsername(user.getUsername());
////        assertNotNull(foundUser);
////    }
//
//    @Test
//    @Order(2)
//    void findByUsername() {
//        User foundUser = userService.findByUsername(user.getUsername());
//        assertNotNull(foundUser);
//    }
//
//    @Test
//    @Order(3)
//    void findById(){
//        User foundUserByUsername = userService.findByUsername(user.getUsername());
//        User foundUserById = userService.findById(foundUserByUsername.getId());
//        assertNotNull(foundUserById);
//    }
//
//    @Test
//    @Order(4)
//    void findAll() {
//        List<User> users = userService.findAll();
//        assertEquals(users.size(), 1);
//    }
//


//
//    @Test
//    @Order(7)
//    void singIn() {
//        User signedInUser = userService.singIn("0011111111", "Aa123456");
//        assertNotNull(signedInUser);
//    }
//
//    @Test
//    @Order(8)
//    void changePassword() {
//        User foundUser = userService.findByUsername(user.getUsername());
//        User passwordChangingUser = userService.changePassword(foundUser.getId(), "Aa123456","Ss123456", "Ss123456");
//        assertTrue(passwordChangingUser.getPassword().equals("Ss123456"));
//    }
//


//
//    @Test
//    @Order(17)
//    void deleteById() {
//        User foundUser = userService.findByUsername(user.getUsername());
//        userService.deleteById(foundUser.getId());
//        List<User> users = userService.findAll();
//        assertEquals(users.size(), 0);
//    }
//
//    @Test
//    @Order(18)
//    void update() {
//        User foundUserByUsername = userService.findByUsername(user.getUsername());
//        foundUserByUsername.setFamilyName("Qurbani");
//        User updatedUser = userService.update(foundUserByUsername);
//        User foundUserById = userService.findById(foundUserByUsername.getId());
//        assertEquals(updatedUser.getFamilyName(), foundUserById.getFamilyName());
//    }
//
//}