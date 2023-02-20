package com.maktabsharif.homeservices.service;

import com.maktabsharif.homeservices.domain.Services;
import com.maktabsharif.homeservices.domain.User;
import com.maktabsharif.homeservices.domain.Wallet;
import com.maktabsharif.homeservices.domain.enumeration.ExpertStatus;
import com.maktabsharif.homeservices.domain.enumeration.UserRole;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class UserServiceTest {
    @Autowired
    private UserService userService;

    User user = User.builder()
            .name("client")
            .familyName("1")
            .username("0011111112")
            .password("Aa123456")
            .email("c1@live.com")
            .userRole(UserRole.CLIENT)
            .registerDate(new Timestamp(new Date().getTime()))
            .wallet(Wallet.builder()
                    .balance(0d).build())
            .build();



    @Test
    @Order(1)
    void createUser() throws Exception {
        userService.create(user);
        User foundUser = userService.findByUsername(user.getUsername());
        assertNotNull(foundUser);
    }

    @Test
    @Order(2)
    void findByUsername() {
        User foundUser = userService.findByUsername(user.getUsername());
        assertNotNull(foundUser);
    }

    @Test
    @Order(3)
    void findById(){
        User foundUserByUsername = userService.findByUsername(user.getUsername());
        User foundUserById = userService.findById(foundUserByUsername.getId());
        assertNotNull(foundUserById);
    }

    @Test
    @Order(4)
    void findAll() {
        List<User> users = userService.findAll();
        assertEquals(users.size(), 1);
    }
    @Test
    @Order(5)
    void approveNewExperts() {
        User foundUser = userService.findByUsername(user.getUsername());
        User approvedExpert = userService.approveNewExperts(foundUser.getId());
        assertTrue(approvedExpert.getExpertStatus().equals(ExpertStatus.APPROVED));
    }

    @Test
    @Order(6)
    void singIn() {
        User signedInUser = userService.singIn("0011111111", "Aa123456");
        assertNotNull(signedInUser);
    }

    @Test
    @Order(7)
    void changePassword() {
        User foundUser = userService.findByUsername(user.getUsername());
        User passwordChangingUser = userService.changePassword(foundUser.getId(), "Aa123456","Ss123456", "Ss123456");
        assertTrue(passwordChangingUser.getPassword().equals("Ss123456"));
    }



    @Test
    @Order(8)
    void deleteById() {
        User foundUser = userService.findByUsername(user.getUsername());
        userService.deleteById(foundUser.getId());
        List<User> users = userService.findAll();
        assertEquals(users.size(), 0);
    }

//    @Test
//    void addExpertToServices() {
//        User assignedToServicesExpert = userService.addExpertToServices(user, 9l);
//        assertTrue((assignedToServicesExpert.getServices().stream().filter(t -> Objects.equals(t.getId(), 9)).findFirst()).isPresent());
//    }
//
//    @Test
//    void removeExpertFromServices() {
//        User underRemovingFromServicesExpert = userService.findByUsername(user.getUsername());
//        User RemovedFromServicesExpert = userService.removeExpertFromServices(underRemovingFromServicesExpert, 8l);
//        assertTrue(!(RemovedFromServicesExpert.getServices().stream().filter(t -> t.getId() == 8).findFirst()).isPresent());
//    }




}