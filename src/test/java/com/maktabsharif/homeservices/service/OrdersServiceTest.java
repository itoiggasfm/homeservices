package com.maktabsharif.homeservices.service;

import com.maktabsharif.homeservices.domain.Orders;
import com.maktabsharif.homeservices.domain.Subservices;
import com.maktabsharif.homeservices.domain.User;
import com.maktabsharif.homeservices.domain.enumeration.OrderStatus;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
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
//            .orderDate(new Timestamp(new Date().getTime()))
            .subservices(Subservices.builder()
                    .id(1l)
                    .build())
            .user(User.builder()
                    .id(1l)
                    .build())
            .workDescription("New Radio")
            .address("Tehran - Beheshti Ave.")
            .clientSuggestedPrice(2000d)
            .startDateByClient(Timestamp.valueOf("2023-3-24 11:46:00"))
//            .orderStatus(OrderStatus.AWAITING_EXPERTS_SUGGESTION)
            .build();


    @Test
    @Order(1)
    void create() throws Exception {
        ordersService.create(order);

        Orders foundOrder = ordersService.findByUserIdAndServicesId(order.getUser().getId(), order.getSubservices().getId());

        assertNotNull(foundOrder);
    }


    @Test
    @Order(2)
    void findByUserIdAndServicesId() throws Exception {
        Orders foundOrder = ordersService.findByUserIdAndServicesId(order.getUser().getId(), order.getSubservices().getId());
        assertNotNull(foundOrder);
    }

    @Test
    @Order(3)
    void findById() throws Exception {
        Orders foundOrderByUserAndServices = ordersService.findByUserIdAndServicesId(order.getUser().getId(), order.getSubservices().getId());
        Orders foundOrderById = ordersService.findById(foundOrderByUserAndServices.getId());
        assertNotNull(foundOrderById);
    }

    @Test
    @Order(4)
    void findAll() throws Exception {
        List<Orders> orders = ordersService.findAll();
        assertEquals(orders.size(),  1);
    }

    @Test
    @Order(5)
    void findClientOrders() throws Exception {
        Orders foundOrder = ordersService.findByUserIdAndServicesId(order.getUser().getId(), order.getSubservices().getId());
        List<Orders> clientOrders = ordersService.findClientOrders(foundOrder.getUser().getId());
        assertEquals(clientOrders.size(),  1);

    }

    @Test
    @Order(6)
    void findRelatedToExpertOrders() {
        User expert = userService.findById(1l);
        List<Orders> relatedToExpertOrders = ordersService.findRelatedToExpertOrders(expert.getSubservices());
        assertEquals(relatedToExpertOrders.size(),  1);
    }


    @Test
    @Order(7)
    void findSuggestibleByExpertOrders() {
        User expert = userService.findById(1l);
        List<Orders> suggestibleByExpertOrders = ordersService.findSuggestibleByExpertOrders(expert.getSubservices());
        assertEquals(suggestibleByExpertOrders.size(),  1);
    }

    @Test
    @Order(8)
    void findAssignedToExpertOrders() {
        List<Orders> assignedToExpertOrders = ordersService.findAssignedToExpertOrders(1l);
        assertEquals(assignedToExpertOrders.size(), 1);
    }

    @Test
    @Order(9)
    void changeOrderStatusToStarted() throws Exception {
        Orders foundOrder = ordersService.findByUserIdAndServicesId(order.getUser().getId(), order.getSubservices().getId());
        Orders changedOrderStatusToStarted = ordersService.changeOrderStatusToStarted(foundOrder.getId());
        assertTrue(changedOrderStatusToStarted.getOrderStatus().equals(OrderStatus.STARTED));
    }

    @Test
    @Order(10)
    void changeOrderStatusToDone() throws Exception {
        Orders foundOrder = ordersService.findByUserIdAndServicesId(order.getUser().getId(), order.getSubservices().getId());
        Orders changedOrderStatusToDone = ordersService.changeOrderStatusToDone(foundOrder.getId());
        assertTrue(changedOrderStatusToDone.getOrderStatus().equals(OrderStatus.DONE));

    }

    @Test
    @Order(11)
    void changeOrderStatusToPaid() throws Exception {
        Orders foundOrder = ordersService.findByUserIdAndServicesId(order.getUser().getId(), order.getSubservices().getId());
        Orders changedOrderStatusToPiad = ordersService.changeOrderStatusToPaidByCredit(foundOrder.getId());
        assertTrue(changedOrderStatusToPiad.getOrderStatus().equals(OrderStatus.PAID));
    }




}