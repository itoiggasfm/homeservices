package com.maktabsharif.homeservices.service;

import com.maktabsharif.homeservices.domain.Services;
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
class ServicesServiceTest {

    @Autowired
    private ServicesService servicesService;

    Services services = Services.builder()
            .serviceTitle("Audio Video")
            .subserviceTitle("Radio")
            .basePrice(1000d)
            .description("new Radio")
            .build();

    @Test
    @Order(1)
    void create() throws Exception {

        servicesService.create(services);
        if(services.getSubserviceTitle() == null){
            Services foundService = servicesService.findByServiceTitle(services.getServiceTitle());
            assertNotNull(foundService);
        }else{
            Services foundSubservice = servicesService.findBySubserviceTitle(services.getSubserviceTitle());
            assertNotNull(foundSubservice);

        }

    }

    @Test
    @Order(2)
    void findByServiceTitle() {
        Services foundService = servicesService.findByServiceTitle(services.getServiceTitle());
        assertNotNull(foundService);
    }

    @Test
    @Order(3)
    void findBySubserviceTitle() {
        Services foundService = servicesService.findBySubserviceTitle(services.getSubserviceTitle());
        assertNotNull(foundService);
    }

    @Test
    @Order(4)
    void findByServiceTitleAndSubserviceTitle() {
        Services foundService = servicesService.findByServiceTitleAndSubserviceTitle(services.getServiceTitle(), services.getSubserviceTitle());
        assertNotNull(foundService);
    }

    @Test
    @Order(5)
    void findById() {
        Services foundServiceByServiceTitle = servicesService.findByServiceTitle(services.getServiceTitle());
        Services foundServiceByServiceId = servicesService.findById(foundServiceByServiceTitle.getId());
        assertNotNull(foundServiceByServiceId);
    }

    @Test
    @Order(6)
    void findAll() {
        List<Services> services = servicesService.findAll();
        assertEquals(services.size(), 1);
    }

    @Test
    @Order(7)
    void editServices() throws Exception{
        Services underEditService = servicesService.findByServiceTitleAndSubserviceTitle(services.getServiceTitle(), services.getSubserviceTitle());

        String newServiceTitle = null;
        String newSubserviceTitle = null;
        Double newBasePrice = 2000d;
        String newDescription = "New radio tuned";
        Services editedService = servicesService.editServices(underEditService, newServiceTitle, newSubserviceTitle, newBasePrice, newDescription);
        assertTrue(
                (newServiceTitle != null && editedService.getServiceTitle().equals(newServiceTitle) ? true : newServiceTitle == null && editedService.getServiceTitle().equals(underEditService.getServiceTitle()) ? true : false) &&
                        (newSubserviceTitle != null && editedService.getSubserviceTitle().equals(newSubserviceTitle) ? true : newSubserviceTitle == null && editedService.getSubserviceTitle().equals(underEditService.getSubserviceTitle()) ? true : false) &&
                        (newBasePrice != null && Objects.equals(editedService.getBasePrice(), newBasePrice) ? true : newBasePrice == null && Objects.equals(editedService.getBasePrice(), underEditService.getBasePrice()) ? true : false) &&
                        (newDescription != null && editedService.getDescription().equals(newDescription) ? true : newDescription == null && editedService.getDescription().equals(underEditService.getDescription()) ? true : false)
        );
    }

    @Test
    @Order(8)
    void addExpertToServices() {
        Services foundService = servicesService.findByServiceTitleAndSubserviceTitle(services.getServiceTitle(), services.getSubserviceTitle());
        User assignedToServicesExpert = servicesService.addExpertToServices(foundService.getId(), 4l);
        assertTrue((assignedToServicesExpert.getServices().stream().filter(t -> Objects.equals(t.getId(), foundService.getId())).findFirst()).isPresent());

    }

    @Test
    @Order(9)
    void removeExpertFromServices() {
        Services foundService = servicesService.findByServiceTitleAndSubserviceTitle(services.getServiceTitle(), services.getSubserviceTitle());
        User RemovedFromServicesExpert = servicesService.removeExpertFromServices(foundService.getId(), 4l);
        assertTrue(!(RemovedFromServicesExpert.getServices().stream().filter(t -> Objects.equals(t.getId(), foundService.getId())).findFirst()).isPresent());
    }

    @Test
    @Order(10)
    void deleteById() {
        Services foundService = servicesService.findByServiceTitle(services.getServiceTitle());
        servicesService.deleteById(foundService.getId());
        List<Services> allServices = servicesService.findAll();
        assertEquals(allServices.size(), 0);
    }
}