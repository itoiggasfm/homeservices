package com.maktabsharif.homeservices.service;

import com.maktabsharif.homeservices.domain.Orders;
import com.maktabsharif.homeservices.domain.Suggestions;
import com.maktabsharif.homeservices.domain.User;
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
class SuggestionsServiceTest {

    @Autowired
    private SuggestionsService suggestionsService;

    Suggestions suggestions = Suggestions.builder()
            .orders(Orders.builder()
                    .id(1l)
                    .build())
            .user(User.builder()
                    .id(1l)
                    .build())
            .expertSuggestion("ready for it")
            .expertSuggestionDate(new Timestamp(new Date().getTime()))
            .expertSuggestedPrice(3000d)
            .startDateByExpert(Timestamp.valueOf("2023-2-24 11:53:00"))
            .orderDoDuration(2)
            .selecetd(false)
            .build();

    @Test
    @Order(1)
    void create() throws Exception {
        suggestionsService.create(suggestions);

        Suggestions foundSuggestion = suggestionsService.findByUserAndOrders(suggestions.getUser(), suggestions.getOrders());
        assertNotNull(foundSuggestion);
    }

    @Test
    @Order(2)
    void findByUserAndOrders() {
        Suggestions foundSuggestion = suggestionsService.findByUserAndOrders(suggestions.getUser(), suggestions.getOrders());
        assertNotNull(foundSuggestion);
    }

    @Test
    @Order(3)
    void findById() {
        Suggestions foundSuggestionByUserAndOrder = suggestionsService.findByUserAndOrders(suggestions.getUser(), suggestions.getOrders());
        Suggestions foundSuggestionById = suggestionsService.findById(foundSuggestionByUserAndOrder.getId());
        assertNotNull(foundSuggestionById);
    }

    @Test
    @Order(4)
    void findOrdersSuggestions() throws Exception {
        List<Suggestions> ordersSuggestions = suggestionsService.findOrdersSuggestions(suggestions.getOrders().getId());
        assertEquals(ordersSuggestions.size(), 1);
    }

    @Test
    @Order(5)
    void selectSuggestion() throws Exception {
        Suggestions foundSuggestion = suggestionsService.findByUserAndOrders(suggestions.getUser(), suggestions.getOrders());
        suggestionsService.selectSuggestion(suggestions.getOrders().getId(), foundSuggestion.getId());
        Suggestions selectedSuggestion = suggestionsService.findById(foundSuggestion.getId());
        assertTrue(selectedSuggestion.getSelecetd());
    }

    @Test
    @Order(6)
    void findAssignedToExpertOrders() {
        List<Orders> assignedToExpertOrders = suggestionsService.findAssignedToExpertOrders(4l);
        assertEquals(assignedToExpertOrders.size(), 1);
    }
}