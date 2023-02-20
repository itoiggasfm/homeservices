package com.maktabsharif.homeservices.service;

import com.maktabsharif.homeservices.domain.Orders;
import com.maktabsharif.homeservices.domain.Services;
import com.maktabsharif.homeservices.domain.User;
import com.maktabsharif.homeservices.domain.enumeration.OrderStatus;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.Disabled;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class OrdersServiceTest {

    @Autowired
    private OrdersService orderService;
    @Autowired
    private UserService userService;


    Orders order = Orders.builder()
            .orderDate(new Timestamp(new Date().getTime()))
            .services(Services.builder()
                    .id(3l)
                    .build())
            .user(User.builder()
                    .id(5l)
                    .build())
            .workDescription("New Radio")
            .address("Tehran - Beheshti Ave.")
            .clientSuggestedPrice(2000d)
            .startDateByClient(Timestamp.valueOf("2023-2-20 12:00:00"))
            .orderStatus(OrderStatus.AWAITING_EXPERTS_SUGGESTION)
            .build();


    @Test
    @Order(1)
    void create() throws Exception {
        orderService.create(order);

        Orders foundOrder = orderService.findByUserAndServices(order.getUser(), order.getServices());

        assertNotNull(foundOrder);
    }


    @Test
    @Order(2)
    void findByUserAndServices() throws Exception {
        Orders foundOrder = orderService.findByUserAndServices(order.getUser(), order.getServices());
        assertNotNull(foundOrder);
    }

    @Test
    @Order(3)
    void findById() throws Exception {
        Orders foundOrderByUserAndServices = orderService.findByUserAndServices(order.getUser(), order.getServices());
        Orders foundOrderById = orderService.findById(foundOrderByUserAndServices.getId());
        assertNotNull(foundOrderById);
    }

    @Test
    @Order(4)
    void findAll() throws Exception {
        List<Orders> orders = orderService.findAll();
        assertEquals(orders.size(),  2);
    }

    @Test
    @Order(5)
    void findClientOrders() throws Exception {
        Orders foundOrder = orderService.findByUserAndServices(order.getUser(), order.getServices());
        List<Orders> clientOrders = orderService.findClientOrders(foundOrder.getUser().getId());
        assertEquals(clientOrders.size(),  1);

    }

    @Test
    @Order(6)
    void findRelatedToExpertOrders() {
        User expert = userService.findById(4l);
        List<Orders> relatedToExpertOrders = orderService.findRelatedToExpertOrders(expert.getServices());
        assertEquals(relatedToExpertOrders.size(),  2);
    }


    @Test
    @Order(7)
    void findSuggestibleByExpertOrders() {
        User expert = userService.findById(4l);
        List<Orders> suggestibleByExpertOrders = orderService.findSuggestibleByExpertOrders(expert.getServices());
        assertEquals(suggestibleByExpertOrders.size(),  2);
    }

    @Test
    @Disabled
    void changeOrderStatusToStarted() {
    }

    @Test
    @Disabled
    void changeOrderStatusToDone() {
    }

    @Test
    @Disabled
    void changeOrderStatusToPaid() {
    }


}