package com.maktabsharif.homeservices.service;

import com.maktabsharif.homeservices.Exceptions.*;
import com.maktabsharif.homeservices.domain.*;
import com.maktabsharif.homeservices.domain.enumeration.ExpertStatus;
import com.maktabsharif.homeservices.domain.enumeration.OrderStatus;
import com.maktabsharif.homeservices.domain.enumeration.UserRole;
import com.maktabsharif.homeservices.repository.SuggestionsRepository;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
public class SuggestionsService {

    private final SuggestionsRepository suggestionsRepository;
    private final OrdersService ordersService;
    private final UserService userService;

    public SuggestionsService(SuggestionsRepository suggestionsRepository,
                              @Lazy OrdersService ordersService,
                              UserService userService) {
        this.suggestionsRepository = suggestionsRepository;
        this.ordersService = ordersService;
        this.userService = userService;
    }

    public Suggestions create(Suggestions suggestions) throws Exception{
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

        Orders suggestionOrder = ordersService.findById(suggestions.getOrders().getId());
        List<Subservices> expertServices = suggestionExpert.getSubservices();

        if(!expertServices.stream().filter(t -> Objects.equals(t.getId(), suggestionOrder.getSubservices().getId())).findFirst().isPresent())
            throw new OrderServiceNotInExpertServices("Order service is not in " +
                    suggestionExpert.getName() + " " + suggestionExpert.getFamilyName() +
                    " services.");

        if(suggestionOrder.getClientSuggestedPrice() > suggestions.getExpertSuggestedPrice())
            throw new LessExpertSuggestedPrice("Expert's suggested price less than client's suggested price.");
        if(suggestionOrder.getStartDateByClient().after(suggestions.getStartDateByExpert()))
            throw new StartDateByExpertBeforeStartDateByClintException("Start date by expert is before start date by client.");

        suggestions.setExpertSuggestionDate(new Timestamp(new Date().getTime()));
        suggestions.setSelected(false);
        suggestionOrder.setOrderStatus(OrderStatus.AWAITING_EXPERT_SELECTION);
        ordersService.updateByOrder(suggestionOrder);

        return suggestionsRepository.save(suggestions);



    }

    public Suggestions findByUserIdAndOrdersId(Long userId, Long ordersId) throws Exception {
        User user = userService.findById(userId);
        Orders orders = ordersService.findById(ordersId);
        return suggestionsRepository.findByUserAndOrders(user, orders)
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

    public List<Suggestions> findOrdersSuggestions(Long ordersId) throws Exception {
        Orders order = ordersService.findById(ordersId);

        List<Suggestions> suggestions = suggestionsRepository.findAll();
        List<Suggestions> orderSuggestions = suggestions.stream()
                .filter(s -> Objects.equals(s.getOrders().getId(), ordersId))
                .collect(Collectors.toList());

        return orderSuggestions;
    }

    public void selectSuggestion(Long orderId, Long suggestionId) throws Exception {

        Orders order = ordersService.findById(orderId);
        Optional<Suggestions> suggestion = suggestionsRepository.findById(suggestionId);

        suggestion.get().setSelected(true);
        suggestionsRepository.save(suggestion.get());

        order.setSelectedSuggestionId(suggestionId);
        order.setOrderStatus(OrderStatus.AWAITING_EXPERT_TO_COME_TO_YOUR_PLACE);
        ordersService.updateByOrder(order);
    }


}
