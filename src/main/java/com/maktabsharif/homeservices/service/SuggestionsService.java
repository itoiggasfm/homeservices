package com.maktabsharif.homeservices.service;

import com.maktabsharif.homeservices.Exceptions.*;
import com.maktabsharif.homeservices.domain.*;
import com.maktabsharif.homeservices.domain.enumeration.ExpertStatus;
import com.maktabsharif.homeservices.domain.enumeration.OrderStatus;
import com.maktabsharif.homeservices.domain.enumeration.Role;
import com.maktabsharif.homeservices.repository.ExpertRepository;
import com.maktabsharif.homeservices.repository.SuggestionsRepository;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.*;
import java.util.stream.Collectors;


@Service
public class SuggestionsService {

    private final SuggestionsRepository suggestionsRepository;
    private final OrdersService ordersService;
    private final ExpertService expertService;
    private final UserService userService;

    public SuggestionsService(SuggestionsRepository suggestionsRepository,
                              @Lazy OrdersService ordersService,
                              ExpertService expertService, UserService userService) {
        this.suggestionsRepository = suggestionsRepository;
        this.ordersService = ordersService;
        this.expertService = expertService;
        this.userService = userService;
    }

    public Suggestions create(Suggestions suggestions) throws Exception{
        Optional<Suggestions> foundSuggestion = suggestionsRepository.findByExpertAndOrders(suggestions.getExpert(), suggestions.getOrders());

        if(foundSuggestion.isPresent())
            throw new DuplicateSuggestionException("Suggestion is already made.");

        Expert suggestionExpert = expertService.findById(suggestions.getExpert().getId());
        if(!suggestionExpert.getRole().equals(Role.ROLE_EXPERT))
            throw new NotExpertUserException(suggestionExpert.getName() + " " + suggestionExpert.getFamilyName()
                    + " has not registered as an expert.");
        if(!suggestionExpert.getExpertStatus().equals(ExpertStatus.APPROVED))
            throw new NotApprovedExpertException(suggestionExpert.getName() + " " + suggestionExpert.getFamilyName()
                    + " has not been approved as expert."
            );

        Orders suggestionOrder = ordersService.findById(suggestions.getOrders().getId());
        List<Subservices> expertServices = suggestionExpert.getSubservices();

        if(!expertServices.stream().filter(t -> Objects.equals(t.getId(), suggestionOrder.getSubservices().getId())).findFirst().isPresent())
            throw new OrderServiceNotInExpertServices("Order service is not in " +
                    suggestionExpert.getName() + " " + suggestionExpert.getFamilyName() +
                    " services.");

        if(suggestionOrder.getClientSuggestedPrice() > suggestions.getSuggestedPrice())
            throw new LessExpertSuggestedPrice("Expert's suggested price less than client's suggested price.");
        if(suggestionOrder.getStartDateByClient().after(suggestions.getStartDateByExpert()))
            throw new StartDateByExpertBeforeStartDateByClintException("Start date by expert is before start date by client.");

        suggestions.setSelected(false);
        suggestionOrder.setOrderStatus(OrderStatus.AWAITING_EXPERT_SELECTION);
        ordersService.updateByOrder(suggestionOrder);

        return suggestionsRepository.save(suggestions);



    }

    public Suggestions findByExpertIdAndOrdersId(Long expertId, Long ordersId) throws Exception {
        Expert expert = expertService.findById(expertId);
        Orders orders = ordersService.findById(ordersId);
        return suggestionsRepository.findByExpertAndOrders(expert, orders)
                .orElseThrow(() -> new SuggestionNotFoundException("Suggestion not found."));
    }

    public Suggestions findById(Long id){
        return suggestionsRepository.findById(id)
                .orElseThrow(() -> new SuggestionNotFoundException("Suggestion not found."));
    }

    public List<Suggestions> findAll(){
        return suggestionsRepository.findAll();
    }

    public void deleteById(Long suggestionsId){
        suggestionsRepository.deleteById(suggestionsId);
    }
    public Suggestions update(Suggestions suggestions){
        return suggestionsRepository.save(suggestions);
    }

    public List<Suggestions> findOrdersSuggestionsSortedByExpertSuggestedPrice(Long ordersId) throws Exception {
        Orders order = ordersService.findById(ordersId);

        List<Suggestions> suggestions = suggestionsRepository.findAll();
        List<Suggestions> orderSuggestions = suggestions.stream()
                .filter(s -> Objects.equals(s.getOrders().getId(), ordersId))
                .sorted(Comparator.comparingDouble(Suggestions::getSuggestedPrice))
                .collect(Collectors.toList());

        return orderSuggestions;
    }

    public List<Suggestions> findOrdersSuggestionsSortedByExpertPoint(Long ordersId) throws Exception {
        Orders order = ordersService.findById(ordersId);

        List<Suggestions> suggestions = suggestionsRepository.findAll();
        List<Suggestions> orderSuggestions = suggestions.stream()
                .filter(s -> Objects.equals(s.getOrders().getId(), ordersId))
                .sorted(Comparator.comparingInt(s -> s.getExpert().getExpertPoint()))
                .collect(Collectors.toList());

        return orderSuggestions;
    }

    public List<Suggestions> findOrdersSuggestionsSortedByExpertPointAndSuggestedPrice(Long ordersId) throws Exception {
        Orders order = ordersService.findById(ordersId);

        List<Suggestions> suggestions = suggestionsRepository.findAll();
        List<Suggestions> orderSuggestions = suggestions.stream()
                .filter(s -> Objects.equals(s.getOrders().getId(), ordersId))
                .sorted(Comparator.comparingDouble(Suggestions::getSuggestedPrice)
                        .thenComparingInt(s -> s.getExpert().getExpertPoint()))
                .collect(Collectors.toList());

        return orderSuggestions;
    }

    public void selectSuggestion(Long orderId, Long suggestionId) throws Exception {

        Orders order = ordersService.findById(orderId);
        Suggestions suggestion = suggestionsRepository.findById(suggestionId)
                .orElseThrow(() -> new SuggestionNotFoundException("Suggestion not found."));

        suggestion.setSelected(true);
        suggestionsRepository.save(suggestion);

        order.setSelectedSuggestionId(suggestionId);
        order.setOrderStatus(OrderStatus.AWAITING_EXPERT_TO_COME_TO_YOUR_PLACE);
        ordersService.updateByOrder(order);
    }


}
