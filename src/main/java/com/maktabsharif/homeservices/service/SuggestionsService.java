package com.maktabsharif.homeservices.service;

import com.maktabsharif.homeservices.Exceptions.*;
import com.maktabsharif.homeservices.domain.*;
import com.maktabsharif.homeservices.domain.enumeration.ExpertStatus;
import com.maktabsharif.homeservices.domain.enumeration.OrderStatus;
import com.maktabsharif.homeservices.domain.enumeration.UserRole;
import com.maktabsharif.homeservices.repository.SuggestionsRepository;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
public class SuggestionsService {

    private final SuggestionsRepository suggestionsRepository;
    private final OrdersService orderService;
    private final UserService userService;

    public SuggestionsService(SuggestionsRepository suggestionsRepository,
                              @Lazy OrdersService orderService,
                              UserService userService) {
        this.suggestionsRepository = suggestionsRepository;
        this.orderService = orderService;
        this.userService = userService;
    }

    public void create(Suggestions suggestions) throws Exception{
        Optional<Suggestions> foundSuggestion = suggestionsRepository.findByUserAndOrders(suggestions.getUser(), suggestions.getOrders());

        if(foundSuggestion.isPresent())
            throw new DuplicateSuggestionException("Suggestion is already made.");

        User suggestionExpert = userService.findById(suggestions.getUser().getId());
        if(!suggestionExpert.getUserRole().equals(UserRole.EXPERT))
            throw new NotExpertUserException(suggestionExpert.getName() + " " + suggestionExpert.getFamilyName()
                    + " has not registered as an expert.");
        if(!suggestionExpert.getExpertStatus().equals(ExpertStatus.APPROVED))
            throw new NotApprovedExpertException(suggestionExpert.getName() + " " + suggestionExpert.getFamilyName()
                    + " has not been approved as expert."
            );

        Orders suggestionOrder = orderService.findById(suggestions.getOrders().getId());
        List<Services> expertServices = suggestionExpert.getServices();

        if(!expertServices.stream().filter(t -> Objects.equals(t.getId(), suggestionOrder.getServices().getId())).findFirst().isPresent())
            throw new OrderServiceNotInExpertServices("Order service is not in " +
                    suggestionExpert.getName() + " " + suggestionExpert.getFamilyName() +
                    " services.");

        if(suggestionOrder.getClientSuggestedPrice() > suggestions.getExpertSuggestedPrice())
            throw new LessExpertSuggestedPrice("Expert's suggested price less than client's suggested price.");
        if(suggestionOrder.getStartDateByClient().after(suggestions.getStartDateByExpert()))
            throw new StartDateByExpertBeforeStartDateByClintException("Start date by expert is before start date by client.");

        suggestionsRepository.save(suggestions);

        suggestionOrder.setOrderStatus(OrderStatus.AWAITING_EXPERT_SELECTION);
        orderService.updateByOrder(suggestionOrder);

    }

    public Suggestions findByUserAndOrders(User user, Orders order){
        return suggestionsRepository.findByUserAndOrders(user, order)
                .orElseThrow(() -> new SuggestionNotFoundException("Suggestion not found."));
    }


    public Suggestions findById(Long id){
        return suggestionsRepository.findById(id)
                .orElseThrow(() -> new SuggestionNotFoundException("Suggestion not found."));
    }


    public List<Suggestions> findOrdersSuggestions(Long orderId) throws Exception {
        Orders order = orderService.findById(orderId);

        List<Suggestions> suggestions = suggestionsRepository.findAll();
        List<Suggestions> orderSuggestions = suggestions.stream().filter(s -> Objects.equals(s.getOrders().getId(), orderId)).collect(Collectors.toList());

        return orderSuggestions;
    }


    public void selectSuggestion(Long orderId, Long suggestionId) throws Exception {

        Orders order = orderService.findById(orderId);
        Optional<Suggestions> suggestion = suggestionsRepository.findById(suggestionId);

        suggestion.get().setSelecetd(true);
        suggestionsRepository.save(suggestion.get());

        order.setSelectedSuggestionId(suggestionId);
        order.setOrderStatus(OrderStatus.AWAITING_EXPERT_TO_COME_TO_YOUR_PLACE);
        orderService.updateByOrder(order);
    }

    public List<Orders> findAssignedToExpertOrders(Long expertId){
        List<Orders> orders = orderService.findAll();
        if(orders.isEmpty())
            throw new OrderNotFoundException("Order not found");

        List<Orders> assignedToExpertOrders = orders.stream()
                .filter(o -> o.getSelectedSuggestionId() != null &&
                        Objects.equals(suggestionsRepository.findById(o.getSelectedSuggestionId()).get().getUser().getId(), expertId))
                .collect(Collectors.toList());

        if(assignedToExpertOrders.isEmpty())
            throw new OrderNotFoundException("Order not found");

        return assignedToExpertOrders;

    }

}
