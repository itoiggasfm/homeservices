package com.maktabsharif.homeservices.service;

import com.maktabsharif.homeservices.Exceptions.*;
import com.maktabsharif.homeservices.domain.*;
import com.maktabsharif.homeservices.domain.enumeration.OrderStatus;
import com.maktabsharif.homeservices.repository.OrdersRepository;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class OrdersService {

    private final OrdersRepository ordersRepository;
    private final SubservicesService subservicesService;
    private final UserService userService;
    private final ExpertService expertService;
    private final SuggestionsService suggestionsService;
    private final TransactionsService transactionsService;
    private final WalletService walletService;

    public OrdersService(OrdersRepository ordersRepository,
                         SubservicesService subservicesService,
                         UserService userService,
                         ExpertService expertService, SuggestionsService suggestionsService,
                         TransactionsService transactionsService,
                         WalletService walletService) {
        this.ordersRepository = ordersRepository;
        this.subservicesService = subservicesService;
        this.userService = userService;
        this.expertService = expertService;
        this.suggestionsService = suggestionsService;
        this.transactionsService = transactionsService;
        this.walletService = walletService;
    }

    public Orders create(Orders order) throws Exception{
        Optional<Orders> foundOrder = ordersRepository.findByClientAndSubservicesAndOrderStatusNot(order.getClient(), order.getSubservices(), OrderStatus.PAID);

        if(foundOrder.isPresent() && !foundOrder.get().getOrderStatus().equals(OrderStatus.PAID))
            throw new DuplicateOrderException("Order already registered.");
        if(order.getClientSuggestedPrice()<subservicesService.findById(order.getSubservices().getId()).getBasePrice())
            throw new LessClientSuggestedPrice("Suggested base price is less than base price of service.");
        if(order.getStartDateByClient().before(new Timestamp(new Date().getTime())))
            throw new StartDateByClientBeforeNowException("Start date is before now.");

        order.setOrderStatus(OrderStatus.AWAITING_EXPERTS_SUGGESTION);
        order.setClient( (Client) userService.findById(order.getClient().getId()));
        order.setSubservices(subservicesService.findById(order.getSubservices().getId()));
        return ordersRepository.save(order);
    }

    public Orders findById(Long id) throws Exception{
        return ordersRepository.findById(id)
                .orElseThrow(() -> new OrderNotFoundException("Order not found."));
    }

    public List<Orders> findAll(){
        return ordersRepository.findAll();
    }

    public Orders findByClientAndSubservicesAndOrderStatusNot(Orders orders) throws Exception{
        Client client = (Client) userService.findById(orders.getClient().getId());
        Subservices orderSubServices = subservicesService.findById(orders.getSubservices().getId());
        return ordersRepository.findByClientAndSubservicesAndOrderStatusNot(client, orderSubServices, OrderStatus.PAID)
                .orElseThrow(() -> new OrderNotFoundException("Order not found."));
    }

    public List<Orders> findClientOrders(Long clientId) throws Exception{

        List<Orders> orders = ordersRepository.findAll();
        if(orders.isEmpty())
            throw new OrderNotFoundException("Order not found");

        List<Orders> clientOrders = orders.stream()
                .filter(o -> Objects.equals(o.getClient().getId(), clientId))
                .collect(Collectors.toList());

        if(clientOrders.isEmpty())
            throw new OrderNotFoundException("Order not found");

        return clientOrders;
    }

    public List<Orders> findSuggestibleByExpertOrders(List<Subservices> expertServices){
        List<Orders> orders = ordersRepository.findAll();
        if(orders.isEmpty())
            throw new OrderNotFoundException("Order not found");

        List<Orders> suggestibleByExpertOrders = orders.stream()
                .filter(o -> expertServices.stream()
                        .anyMatch(es -> Objects.equals(o.getSubservices().getId(), es.getId()) &&
                                (o.getOrderStatus().equals(OrderStatus.AWAITING_EXPERT_SELECTION) || o.getOrderStatus().equals(OrderStatus.AWAITING_EXPERTS_SUGGESTION))
                        )).collect(Collectors.toList());

        if(suggestibleByExpertOrders.isEmpty())
            throw new OrderNotFoundException("Order not found");

        return suggestibleByExpertOrders;

    }

    public List<Orders> findAssignedToExpertOrders(Long expertId){
        List<Orders> orders = ordersRepository.findAll();
        if(orders.isEmpty())
            throw new OrderNotFoundException("Order not found");

        List<Orders> assignedToExpertOrders = orders.stream()
                .filter(o -> o.getSelectedSuggestionId() != null &&
                        Objects.equals(suggestionsService.findById(o.getSelectedSuggestionId()).getExpert().getId(), expertId))
                .collect(Collectors.toList());

        if(assignedToExpertOrders.isEmpty())
            throw new OrderNotFoundException("Order not found");

        return assignedToExpertOrders;

    }

    public Orders updateByOrder(Orders order){
        return ordersRepository.save(order);
    }

    public void checkOrderStatus(Orders order, String changeOrderStatusTo){
        if(order.getOrderStatus().equals(OrderStatus.AWAITING_EXPERTS_SUGGESTION))
            throw new NotExpertsSuggestedYetException("No expert suggested on order yet and the order can't be started.");
        if(order.getOrderStatus().equals(OrderStatus.AWAITING_EXPERT_SELECTION))
            throw new NotExpertSelectedYetException("No expert selected for order yet and the order can't be started.");
        if(order.getOrderStatus().equals(OrderStatus.AWAITING_EXPERT_TO_COME_TO_YOUR_PLACE) &&
                suggestionsService.findById(order.getSelectedSuggestionId())
                        .getStartDateByExpert().after(new Timestamp(new Date().getTime())))
            throw new NotExpertHasComeToYpurPlaceYetException("It's not time yet for expert to come to your place and the order can't be started.");
        if(changeOrderStatusTo.equals("started")){
            if(order.getOrderStatus().equals(OrderStatus.STARTED))
                throw new AlreadyStartedOrderException("Order is already started.");
            if(order.getOrderStatus().equals(OrderStatus.DONE))
                throw new AlreadyDoneOrderException("Order is already done.");
            order.setOrderStatus(OrderStatus.STARTED);
        }
        if(changeOrderStatusTo.equals("done")){
            if(order.getOrderStatus().equals(OrderStatus.AWAITING_EXPERT_TO_COME_TO_YOUR_PLACE) &&
                    suggestionsService.findById(order.getSelectedSuggestionId())
                            .getStartDateByExpert().before(new Timestamp(new Date().getTime())))
                throw new NotStartedOrderYetException("Order isn't started yet.");
            if(order.getOrderStatus().equals(OrderStatus.DONE))
                throw new AlreadyStartedOrderException("Order is already done.");
        }
        if(changeOrderStatusTo.equals("paid")){
            if(order.getOrderStatus().equals(OrderStatus.AWAITING_EXPERT_TO_COME_TO_YOUR_PLACE) &&
                    suggestionsService.findById(order.getSelectedSuggestionId())
                            .getStartDateByExpert().before(new Timestamp(new Date().getTime())))
                throw new NotStartedOrderYetException("Order isn't started yet.");
            if(order.getOrderStatus().equals(OrderStatus.STARTED))
                throw new AlreadyStartedOrderException("Order isn't done yet.");;
        }
        if(order.getOrderStatus().equals(OrderStatus.PAID))
            throw new AlreadyStartedOrderException("Order is already paid.");
    }

    public Orders changeOrderStatusToStarted(Long orderId){
        Orders order = ordersRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException("Order not found."));

        checkOrderStatus(order, "started");

        return ordersRepository.save(order);
    }

    public Orders changeOrderStatusToDone(Long orderId){
        Orders order = ordersRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException("Order not found."));

        checkOrderStatus(order, "done");

        Suggestions selectedSuggestion = suggestionsService.findById(ordersRepository.findById(orderId).get().getSelectedSuggestionId());
        Expert orderExpert = selectedSuggestion.getExpert();

        Integer expertDelay = calcExpertDelay(selectedSuggestion);
        orderExpert.setExpertPoint(orderExpert.getExpertPoint() - expertDelay);
        expertService.update(orderExpert);
        makeExpertInactiveIfPointIsNegative(orderExpert);

        order.setOrderStatus(OrderStatus.DONE);
         return ordersRepository.save(order);
    }

    public Integer calcExpertDelay(Suggestions selectedSuggestion){
        Timestamp startDateByExpert = selectedSuggestion.getStartDateByExpert();
        Timestamp now = new Timestamp(new Date().getTime());
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(startDateByExpert.getTime());
        cal.setTimeInMillis(now.getTime());
        int diffInHours = (int) ((now.getTime() - startDateByExpert.getTime())/3600000);
        int orderDoDurationHours = selectedSuggestion.getDoDuration();
        return  diffInHours - orderDoDurationHours;
    }

    public void makeExpertInactiveIfPointIsNegative(Expert expert){
        expert.setEnabled(true);
        if(expert.getExpertPoint()<0)
            expert.setEnabled(false);
        expertService.update(expert);
    }

    public Orders changeOrderStatusToPaid(Long orderId){
        Orders order = ordersRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException("Order not found."));

        checkOrderStatus(order, "paid");

       payByCredit(order);

        order.setOrderStatus(OrderStatus.PAID);
        return ordersRepository.save(order);
    }

    public void rateExpert(Long orderId, Integer clientPointToExpert){
        Suggestions selectedSuggestion = suggestionsService.findById(ordersRepository.findById(orderId).get().getSelectedSuggestionId());
        Expert orderExpert = selectedSuggestion.getExpert();

        orderExpert.setExpertPoint(orderExpert.getExpertPoint() + clientPointToExpert);
        expertService.update(orderExpert);
        makeExpertInactiveIfPointIsNegative(orderExpert);
    }

    public void comment(Long orderId, String comment){
        Orders order = ordersRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException("Order not found."));
        order.setComment(comment);
        ordersRepository.save(order);
    }

    public void payByCredit(Orders order){
        transactionsService.payByCredit(order);
    }

    public void changeOrderStatusToPaidByCard(Long orderId){
        Orders order = ordersRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException("Order not found."));

        checkOrderStatus(order, "paid");

        order.setOrderStatus(OrderStatus.PAID);
        ordersRepository.save(order);
    }


}
