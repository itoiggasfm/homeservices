package com.maktabsharif.homeservices.service;

import com.maktabsharif.homeservices.domain.Client;
import com.maktabsharif.homeservices.domain.Expert;
import com.maktabsharif.homeservices.domain.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ClientServiceTest {

    @Autowired
    ClientService clientService;
    @Autowired
    UserService userService;

    Client client = Client.builder()
            .name("Ali")
            .familyName("Alavi")
            .username("0011111112")
            .password("Aa123456")
            .email("hassan.rahimi@live.com")
            .build();

    @Test
    void create() throws Exception {
        clientService.create(client);
        User foundUser = userService.findByUsername(client.getUsername());
        assertNotNull(foundUser);
    }

    @Test
    void update() {
    }

    @Test
    void singIn() {
        Client signedInUser = clientService.singIn("0011111112", "Aa123456");
        assertNotNull(signedInUser);
    }

    @Test
    void activateAccount() {
        User foundUserByUsername = userService.findByUsername(client.getUsername());
        assertTrue(clientService.activateAccount(foundUserByUsername.getId()));
    }
}