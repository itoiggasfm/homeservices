package com.maktabsharif.homeservices.service;

import com.maktabsharif.homeservices.domain.Expert;
import com.maktabsharif.homeservices.domain.User;
import com.maktabsharif.homeservices.domain.Wallet;
import com.maktabsharif.homeservices.domain.enumeration.ExpertStatus;
import com.maktabsharif.homeservices.domain.enumeration.Role;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ExpertServiceTest {

    @Autowired
    ExpertService expertService;
    @Autowired
    UserService userService;

    Expert expert = Expert.builder()
            .name("Ali")
            .familyName("Alavi")
            .username("0011111111")
            .password("Aa123456")
            .email("hassan.rahimi@live.com")
            .build();
    @Test
    void create() throws Exception {
        expertService.create(expert);
        User foundUser = userService.findByUsername(expert.getUsername());
        assertNotNull(foundUser);
    }

    @Test
    void findById() {
    }

    @Test
    void update() {
    }

    @Test
    void singIn() {
        User signedInUser = expertService.singIn("0011111111", "Aa123456");
        assertNotNull(signedInUser);
    }

    @Test
    void activateAccount() {
        User foundUserByUsername = userService.findByUsername(expert.getUsername());
        assertTrue(expertService.activateAccount(foundUserByUsername.getId()));
    }
}