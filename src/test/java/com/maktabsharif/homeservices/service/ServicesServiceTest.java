package com.maktabsharif.homeservices.service;

import com.maktabsharif.homeservices.domain.Services;
import com.maktabsharif.homeservices.domain.Subservices;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ServicesServiceTest {

    @Autowired
    ServicesService servicesService;

    Services services = Services.builder()
            .serviceTitle("Home appliance")
            .build();

    @Test
    @Order(1)
    void create() {
        servicesService.create(services);
        Services foundService = servicesService.findByServiceTitle(services.getServiceTitle());
        assertNotNull(foundService);
    }

    @Test
    @Order(2)
    void findById() {
        Services foundServiceByServiceTitle = servicesService.findByServiceTitle(services.getServiceTitle());
        Services foundServiceById = servicesService.findById(foundServiceByServiceTitle.getId());
        assertNotNull(foundServiceById);
    }

    @Test@Order(3)
    void findByServiceTitle() {
        Services foundService = servicesService.findByServiceTitle(services.getServiceTitle());
        assertNotNull(foundService);
    }

    @Test
    @Order(4)
    void findAll() {
        assertEquals(servicesService.findAll().size(), 1);
    }

    @Test
    @Order(5)
    void edit() {
        Services underEditionService = servicesService.findByServiceTitle(services.getServiceTitle());

        String newServiceTitle = "All Tv 1";
        Services editedService = servicesService.edit(underEditionService.getId(), newServiceTitle);
        assertTrue(editedService.getServiceTitle().equals(newServiceTitle));
    }



    @Test
    void update() {
        Services underUpdateService = servicesService.findByServiceTitle(services.getServiceTitle());

        underUpdateService.setServiceTitle("new service");
        Services updatedService = servicesService.update(underUpdateService);
        assertTrue(updatedService.getServiceTitle().equals("new service"));
    }

    @Test
    @Order(6)
    void deleteById() {
        Services underdeletionService = servicesService.findByServiceTitle(services.getServiceTitle());
        servicesService.deleteById(underdeletionService.getId());
        assertEquals(servicesService.findAll().size(), 0);
    }




}