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
    private OrdersService ordersService;
    @Autowired
    private UserService userService;


    Orders order = Orders.builder()
            .orderDate(new Timestamp(new Date().getTime()))
            .services(Services.builder()
                    .id(1l)
                    .build())
            .user(User.builder()
                    .id(1l)
                    .build())
            .workDescription("New Radio")
            .address("Tehran - Beheshti Ave.")
            .clientSuggestedPrice(2000d)
            .startDateByClient(Timestamp.valueOf("2023-2-24 11:46:00"))
            .orderStatus(OrderStatus.AWAITING_EXPERTS_SUGGESTION)
            .build();


    @Test
    @Order(1)
    void create() throws Exception {
        ordersService.create(order);

        Orders foundOrder = ordersService.findByUserAndServices(order.getUser(), order.getServices());

        assertNotNull(foundOrder);
    }


    @Test
    @Order(2)
    void findByUserAndServices() throws Exception {
        Orders foundOrder = ordersService.findByUserAndServices(order.getUser(), order.getServices());
        assertNotNull(foundOrder);
    }

    @Test
    @Order(3)
    void findById() throws Exception {
        Orders foundOrderByUserAndServices = ordersService.findByUserAndServices(order.getUser(), order.getServices());
        Orders foundOrderById = ordersService.findById(foundOrderByUserAndServices.getId());
        assertNotNull(foundOrderById);
    }

    @Test
    @Order(4)
    void findAll() throws Exception {
        List<Orders> orders = ordersService.findAll();
        assertEquals(orders.size(),  2);
    }

    @Test
    @Order(5)
    void findClientOrders() throws Exception {
        Orders foundOrder = ordersService.findByUserAndServices(order.getUser(), order.getServices());
        List<Orders> clientOrders = ordersService.findClientOrders(foundOrder.getUser().getId());
        assertEquals(clientOrders.size(),  1);

    }

    @Test
    @Order(6)
    void findRelatedToExpertOrders() {
        User expert = userService.findById(1l);
        List<Orders> relatedToExpertOrders = ordersService.findRelatedToExpertOrders(expert.getServices());
        assertEquals(relatedToExpertOrders.size(),  1);
    }


    @Test
    @Order(7)
    void findSuggestibleByExpertOrders() {
        User expert = userService.findById(4l);
        List<Orders> suggestibleByExpertOrders = ordersService.findSuggestibleByExpertOrders(expert.getServices());
        assertEquals(suggestibleByExpertOrders.size(),  2);
    }

    @Test
    void changeOrderStatusToStarted() throws Exception {
        Orders foundOrder = ordersService.findByUserAndServices(order.getUser(), order.getServices());
        Orders changedOrderStatusToStarted = ordersService.changeOrderStatusToStarted(foundOrder.getId());
        assertTrue(changedOrderStatusToStarted.getOrderStatus().equals(OrderStatus.STARTED));
    }

    @Test
    void changeOrderStatusToDone() throws Exception {
        Orders foundOrder = ordersService.findByUserAndServices(order.getUser(), order.getServices());
        Orders changedOrderStatusToDone = ordersService.changeOrderStatusToStarted(foundOrder.getId());
        assertTrue(changedOrderStatusToDone.getOrderStatus().equals(OrderStatus.DONE));

    }

    @Test
    void changeOrderStatusToPaid() throws Exception {
        Orders foundOrder = ordersService.findByUserAndServices(order.getUser(), order.getServices());
        Orders changedOrderStatusToPiad = ordersService.changeOrderStatusToStarted(foundOrder.getId());
        assertTrue(changedOrderStatusToPiad.getOrderStatus().equals(OrderStatus.PAID));
    }


}