package com.maktabsharif.homeservices.service;

import com.maktabsharif.homeservices.Exceptions.*;
import com.maktabsharif.homeservices.domain.*;
import com.maktabsharif.homeservices.domain.enumeration.OrderStatus;
import com.maktabsharif.homeservices.repository.OrderRepository;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class OrdersService {

    private final OrderRepository orderRepository;
    private final ServicesService servicesService;
    private final UserService userService;
    private final SuggestionsService suggestionsService;

    public OrdersService(OrderRepository orderRepository,
                         ServicesService servicesService,
                         UserService userService,
                         SuggestionsService suggestionsService) {
        this.orderRepository = orderRepository;
        this.servicesService = servicesService;
        this.userService = userService;
        this.suggestionsService = suggestionsService;
    }

    public void create(Orders order) throws Exception{
        Optional<Orders> foundOrder = orderRepository.findByUserAndServices(order.getUser(), order.getServices());

        if(foundOrder.isPresent())
            throw new DuplicateOrderException("Order already registered.");
        if(order.getClientSuggestedPrice()<servicesService.findById(order.getServices().getId()).getBasePrice())
            throw new LessClientSuggestedPrice("Suggested base price is less than base price of service.");
        if(order.getStartDateByClient().before(new Timestamp(new Date().getTime())))
            throw new StartDateByClientBeforeNowException("Start date is before now.");

        order.setUser(userService.findById(order.getUser().getId()));
        orderRepository.save(order);
    }


    public Orders findById(Long id) throws Exception{
        return orderRepository.findById(id)
                .orElseThrow(() -> new OrderNotFoundException("Order not found."));
    }


    public List<Orders> findAll(){
        return orderRepository.findAll();
    }

    public Orders findByUserAndServices(User orderUser, Services orderServices) throws Exception{
        return orderRepository.findByUserAndServices(orderUser, orderServices)
                .orElseThrow(() -> new OrderNotFoundException("Order not found."));
    }


    public List<Orders> findClientOrders  (Long clientId) throws Exception{

        List<Orders> orders = orderRepository.findAll();
        if(orders.isEmpty())
            throw new OrderNotFoundException("Order not found");

        List<Orders> clientOrders = orders.stream()
                .filter(o -> Objects.equals(o.getUser().getId(), clientId))
                .collect(Collectors.toList());

        if(clientOrders.isEmpty())
            throw new OrderNotFoundException("Order not found");

        return clientOrders;
    }


    public List<Orders> findRelatedToExpertOrders(List<Services> expertServices){
        List<Orders> orders = orderRepository.findAll();
        if(orders.isEmpty())
            throw new OrderNotFoundException("Order not found");

        List<Orders> expertRelatedOrders = orders.stream()
                .filter(o -> expertServices.stream()
                        .anyMatch(es -> Objects.equals(o.getServices().getId(), es.getId())))
                .collect(Collectors.toList());

        if(expertRelatedOrders.isEmpty())
            throw new OrderNotFoundException("Order not found");

        return expertRelatedOrders;

    }


    public List<Orders> findSuggestibleByExpertOrders(List<Services> expertServices){
        List<Orders> orders = orderRepository.findAll();
        if(orders.isEmpty())
            throw new OrderNotFoundException("Order not found");

        List<Orders> expertRelatedOrders = orders.stream()
                .filter(o -> expertServices.stream()
                        .anyMatch(es -> Objects.equals(o.getServices().getId(), es.getId()) &&
                                (o.getOrderStatus().equals(OrderStatus.AWAITING_EXPERT_SELECTION) || o.getOrderStatus().equals(OrderStatus.AWAITING_EXPERTS_SUGGESTION))
                        )).collect(Collectors.toList());

        if(expertRelatedOrders.isEmpty())
            throw new OrderNotFoundException("Order not found");

        return expertRelatedOrders;

    }


    public List<Orders> findAssignedToExpertOrders(Long expertId){
        List<Orders> orders = orderRepository.findAll();
        if(orders.isEmpty())
            throw new OrderNotFoundException("Order not found");

        List<Orders> assignedToExpertOrders = orders.stream()
                .filter(o -> o.getSelectedSuggestionId() != null &&
                        Objects.equals(suggestionsService.findById(o.getSelectedSuggestionId()).getUser().getId(), expertId))
                .collect(Collectors.toList());

        if(assignedToExpertOrders.isEmpty())
            throw new OrderNotFoundException("Order not found");

        return assignedToExpertOrders;

    }

    public Orders updateByOrder(Orders order){
        return orderRepository.save(order);
    }

    public void changeOrderStatusToStarted(Long orderId){
        Orders order = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException("Order not found."));

        if(order.getOrderStatus().equals(OrderStatus.AWAITING_EXPERTS_SUGGESTION))
            throw new NotExpertsSuggestedYetException("No expert suggested on order yet and the order can't be started.");
        if(order.getOrderStatus().equals(OrderStatus.AWAITING_EXPERT_SELECTION))
            throw new NotExpertSelectedYetException("No expert selected for order yet and the order can't be started.");
        if(order.getOrderStatus().equals(OrderStatus.AWAITING_EXPERT_TO_COME_TO_YOUR_PLACE) &&
                suggestionsService.findById(order.getSelectedSuggestionId())
                        .getStartDateByExpert().after(new Timestamp(new Date().getTime())))
            throw new NotExpertHasComeToYpurPlaceYetException("It's not time yet foe expert to come to your place and the order can't be started.");
        if(order.getOrderStatus().equals(OrderStatus.STARTED))
            throw new AlreadyStartedOrder("Order is already started.");
        if(order.getOrderStatus().equals(OrderStatus.DONE))
            throw new AlreadyDoneOrder("Order is already done.");
        if(order.getOrderStatus().equals(OrderStatus.PAID))
            throw new AlreadyStartedOrder("Order is already paid.");

        order.setOrderStatus(OrderStatus.STARTED);
        orderRepository.save(order);
    }

    public void changeOrderStatusToDone(Long orderId){
        Orders order = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException("Order not found."));

        if(order.getOrderStatus().equals(OrderStatus.AWAITING_EXPERTS_SUGGESTION))
            throw new NotExpertsSuggestedYetException("No expert suggested on order yet and the order status can't be changed to done.");
        if(order.getOrderStatus().equals(OrderStatus.AWAITING_EXPERT_SELECTION))
            throw new NotExpertSelectedYetException("No expert selected for order yet and the order status can't be changed to done.");
        if(order.getOrderStatus().equals(OrderStatus.AWAITING_EXPERT_TO_COME_TO_YOUR_PLACE) &&
                suggestionsService.findById(order.getSelectedSuggestionId())
                        .getStartDateByExpert().after(new Timestamp(new Date().getTime())))
            throw new NotExpertHasComeToYpurPlaceYetException("It's not time yet foe expert to come to your place and the order status can't be changed to done.");
        if(order.getOrderStatus().equals(OrderStatus.AWAITING_EXPERT_TO_COME_TO_YOUR_PLACE) &&
                suggestionsService.findById(order.getSelectedSuggestionId())
                        .getStartDateByExpert().before(new Timestamp(new Date().getTime())))
            throw new NotStartedOrderYet("Order isn't started yet.");
        if(order.getOrderStatus().equals(OrderStatus.DONE))
            throw new AlreadyStartedOrder("Order is already done.");
        if(order.getOrderStatus().equals(OrderStatus.PAID))
            throw new AlreadyPaidOrder("Order is already paid.");

        order.setOrderStatus(OrderStatus.DONE);
        orderRepository.save(order);
    }

    public void changeOrderStatusToPaid(Long orderId){
        Orders order = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException("Order not found."));

        if(order.getOrderStatus().equals(OrderStatus.AWAITING_EXPERTS_SUGGESTION))
            throw new NotExpertsSuggestedYetException("No expert suggested on order yet and the order status can't be changed to paid.");
        if(order.getOrderStatus().equals(OrderStatus.AWAITING_EXPERT_SELECTION))
            throw new NotExpertSelectedYetException("No expert selected for order yet and the order status can't be changed to paid.");
        if(order.getOrderStatus().equals(OrderStatus.AWAITING_EXPERT_TO_COME_TO_YOUR_PLACE) &&
                suggestionsService.findById(order.getSelectedSuggestionId())
                        .getStartDateByExpert().after(new Timestamp(new Date().getTime())))
            throw new NotExpertHasComeToYpurPlaceYetException("It's not time yet foe expert to come to your place and the order status can't be changed to paid.");
        if(order.getOrderStatus().equals(OrderStatus.AWAITING_EXPERT_TO_COME_TO_YOUR_PLACE) &&
                suggestionsService.findById(order.getSelectedSuggestionId())
                        .getStartDateByExpert().before(new Timestamp(new Date().getTime())))
            throw new NotStartedOrderYet("Order isn't started yet.");
        if(order.getOrderStatus().equals(OrderStatus.STARTED))
            throw new AlreadyStartedOrder("Order isn't done yet.");
        if(order.getOrderStatus().equals(OrderStatus.PAID))
            throw new AlreadyPaidOrder("Order is already paid.");

        order.setOrderStatus(OrderStatus.PAID);
        orderRepository.save(order);
    }


}
