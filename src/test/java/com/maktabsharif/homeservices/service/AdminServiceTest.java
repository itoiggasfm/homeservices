package com.maktabsharif.homeservices.service;

import com.maktabsharif.homeservices.domain.Expert;
import com.maktabsharif.homeservices.domain.User;
import com.maktabsharif.homeservices.domain.enumeration.ExpertStatus;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class AdminServiceTest {

    @Autowired
    AdminService adminService;

    @Autowired
    UserService userService;

    @Test
    void create() {
    }

    @Test
    void update() {
    }

    @Test
    void singIn() {
    }

    @Test
    void approveNewExperts() {
        User foundUser = userService.findByUsername("0011111111");
        Expert approvedExpert = adminService.approveNewExperts(foundUser.getId());
        assertTrue(approvedExpert.getExpertStatus().equals(ExpertStatus.APPROVED));
    }

    @Test
    void addExpertToServices() {
        Expert assignedToServicesExpert = adminService.addExpertToServices(3l, 1l);
        assertTrue(assignedToServicesExpert.getSubservices().stream()
                .filter(t -> Objects.equals(t.getId(), 1l))
                .findFirst().isPresent());
    }

    @Test
    void removeExpertFromServices() {
        Expert RemovedFromServicesExpert = adminService.removeExpertFromServices(3l, 1l);
        assertTrue(!(RemovedFromServicesExpert.getSubservices().stream()
                .filter(t -> Objects.equals(t.getId(), 1l))
                .findFirst()).isPresent());
    }
}