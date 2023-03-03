package com.maktabsharif.homeservices.service;

import com.maktabsharif.homeservices.domain.Subservices;
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

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class UserServiceTest {
    @Autowired
    private UserService userService;
    @Autowired
    private SubservicesService subservicesService;

    User user = User.builder()
//            .name("Ahmad")
//            .familyName("Ahmadi")
//            .username("0011111111")
            .name("Ali")
            .familyName("Alavi")
            .username("0011111112")
            .password("Aa123456")
//            .email("aahmadi@live.com")
            .email("aalavi@live.com")
//            .userRole(UserRole.EXPERT)
            .userRole(UserRole.CLIENT)
//            .expertPoint(3)
//            .expertStatus(ExpertStatus.NEW)
            .wallet(Wallet.builder()
                    .balance(10000d).build())
            .build();



    @Test
    @Order(1)
    void create() throws Exception {
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
    void activateAccount() {
        User foundUserByUsername = userService.findByUsername(user.getUsername());
        assertTrue(userService.activateAccount(foundUserByUsername.getId()));
    }
    @Test
    @Order(6)
    void approveNewExperts() {
        User foundUser = userService.findByUsername(user.getUsername());
        User approvedExpert = userService.approveNewExperts(foundUser.getId());
        assertTrue(approvedExpert.getExpertStatus().equals(ExpertStatus.APPROVED));
    }

    @Test
    @Order(7)
    void singIn() {
        User signedInUser = userService.singIn("0011111111", "Aa123456");
        assertNotNull(signedInUser);
    }

    @Test
    @Order(8)
    void changePassword() {
        User foundUser = userService.findByUsername(user.getUsername());
        User passwordChangingUser = userService.changePassword(foundUser.getId(), "Aa123456","Ss123456", "Ss123456");
        assertTrue(passwordChangingUser.getPassword().equals("Ss123456"));
    }

    @Test
    @Order(9)
    void addExpertToServices() {
        User assignedToServicesExpert = userService.addExpertToServices(user, 1l);
        assertTrue(assignedToServicesExpert.getSubservices().stream()
                .filter(t -> Objects.equals(t.getId(), 1l))
                .findFirst().isPresent());
    }

    @Order(10)
    @Test
    void searchForClientOrExpert() {
        List<User> users = userService.searchForClientOrExpert(UserRole.EXPERT);
        assertEquals(users.size(), 1);
    }

    @Order(11)
    @Test
    void searchForName() {
        List<User> users = userService.searchForName("mad");
        assertEquals(users.size(), 1);
    }

    @Order(12)
    @Test
    void searchForFamilyName() {
        List<User> users = userService.searchForName("mad");
        assertEquals(users.size(), 1);
    }

    @Order(13)
    @Test
    void searchForEmail() {
        List<User> users = userService.searchForEmail("mad");
        assertEquals(users.size(), 1);
    }

    @Order(14)
    @Test
    void searchForExpertPoint() {
        List<User> users = userService.searchForExpertPoint(1, 5);
        assertEquals(users.size(), 1);
    }

    @Order(15)
    @Test
    void searchForExpertise() {
        assertEquals(userService.searchForExpertise("TV").size(), 1);
    }

    @Test
    @Order(16)
    void removeExpertFromServices() {
        User RemovedFromServicesExpert = userService.removeExpertFromServices(user, 1l);
        assertTrue(!(RemovedFromServicesExpert.getSubservices().stream()
                .filter(t -> Objects.equals(t.getId(), 1l))
                .findFirst()).isPresent());
    }

    @Test
    @Order(17)
    void deleteById() {
        User foundUser = userService.findByUsername(user.getUsername());
        userService.deleteById(foundUser.getId());
        List<User> users = userService.findAll();
        assertEquals(users.size(), 0);
    }

    @Test
    @Order(18)
    void update() {
        User foundUserByUsername = userService.findByUsername(user.getUsername());
        foundUserByUsername.setFamilyName("Qurbani");
        User updatedUser = userService.update(foundUserByUsername);
        User foundUserById = userService.findById(foundUserByUsername.getId());
        assertEquals(updatedUser.getFamilyName(), foundUserById.getFamilyName());
    }

}