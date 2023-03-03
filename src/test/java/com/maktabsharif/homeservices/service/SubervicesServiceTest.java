package com.maktabsharif.homeservices.service;

import com.maktabsharif.homeservices.domain.Services;
import com.maktabsharif.homeservices.domain.Subservices;
import com.maktabsharif.homeservices.domain.User;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class SubervicesServiceTest {

    @Autowired
    private SubservicesService subservicesService;

    Subservices subservices = Subservices.builder()
            .subserviceTitle("TV")
            .basePrice(1000d)
            .description("new TV")
            .services(Services.builder()
                    .id(1l)
                    .build())
            .build();

    @Test
    @Order(1)
    void create() throws Exception {

        subservicesService.create(subservices);
        Subservices foundSubservice = subservicesService.findBySubserviceTitleAndServicesId(subservices.getSubserviceTitle(), subservices.getServices().getId());
            assertNotNull(foundSubservice);
    }

    @Test
    @Order(3)
    void findBySubserviceTitle() {
        Subservices foundService = subservicesService.findBySubserviceTitle(subservices.getSubserviceTitle());
        assertNotNull(foundService);
    }

    @Test
    @Order(4)
    void findBySubserviceTitleAndServicesId() {
        Subservices foundService = subservicesService.findBySubserviceTitleAndServicesId(subservices.getSubserviceTitle(), subservices.getServices().getId());
        assertNotNull(foundService);
    }

    @Test
    @Order(5)
    void findById() {
        Subservices foundServiceBySubserviceTitleAndServices = subservicesService.findBySubserviceTitleAndServicesId(subservices.getSubserviceTitle(), subservices.getServices().getId());
        Subservices foundSubserviceByServiceId = subservicesService.findById(foundServiceBySubserviceTitleAndServices.getId());
        assertNotNull(foundSubserviceByServiceId);
    }

    @Test
    @Order(6)
    void findAll() {
        List<Subservices> subservices = subservicesService.findAll();
        assertEquals(subservices.size(), 1);
    }

    @Test
    @Order(7)
    void edit() throws Exception{
        Subservices underEditionService = subservicesService.findBySubserviceTitleAndServicesId(subservices.getSubserviceTitle(), subservices.getServices().getId());

        String newSubserviceTitle = null;
        Double newBasePrice = 2000d;
        String newDescription = "New all tv";
        Subservices editedSubervice = subservicesService.edit(underEditionService.getId(), newSubserviceTitle, newBasePrice, newDescription);
        assertTrue(
                (newSubserviceTitle != null && editedSubervice.getSubserviceTitle().equals(newSubserviceTitle) ? true : newSubserviceTitle == null && editedSubervice.getSubserviceTitle().equals(underEditionService.getSubserviceTitle()) ? true : false) &&
                        (newBasePrice != null && Objects.equals(editedSubervice.getBasePrice(), newBasePrice) ? true : newBasePrice == null && Objects.equals(editedSubervice.getBasePrice(), underEditionService.getBasePrice()) ? true : false) &&
                        (newDescription != null && editedSubervice.getDescription().equals(newDescription) ? true : newDescription == null && editedSubervice.getDescription().equals(underEditionService.getDescription()) ? true : false)
        );
    }

    @Test
    @Order(8)
    void deleteById() {
        Subservices foundServiceBySubserviceTitleAndServicesId = subservicesService.findBySubserviceTitleAndServicesId(subservices.getSubserviceTitle(), subservices.getServices().getId());
        subservicesService.deleteById(foundServiceBySubserviceTitleAndServicesId.getId());
        List<Subservices> allSubservices = subservicesService.findAll();
        assertEquals(allSubservices.size(), 0);
    }
}