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
    private final SuggestionsService suggestionsService;
    private final TransactionsService transactionsService;
    private final WalletService walletService;

    public OrdersService(OrdersRepository ordersRepository,
                         SubservicesService subservicesService,
                         UserService userService,
                         SuggestionsService suggestionsService,
                         TransactionsService transactionsService,
                         WalletService walletService) {
        this.ordersRepository = ordersRepository;
        this.subservicesService = subservicesService;
        this.userService = userService;
        this.suggestionsService = suggestionsService;
        this.transactionsService = transactionsService;
        this.walletService = walletService;
    }

    public Orders create(Orders order) throws Exception{
        Optional<Orders> foundOrder = ordersRepository.findByUserAndSubservices(order.getUser(), order.getSubservices());

        if(foundOrder.isPresent())
            throw new DuplicateOrderException("Order already registered.");
        if(order.getClientSuggestedPrice()<subservicesService.findById(order.getSubservices().getId()).getBasePrice())
            throw new LessClientSuggestedPrice("Suggested base price is less than base price of service.");
        if(order.getStartDateByClient().before(new Timestamp(new Date().getTime())))
            throw new StartDateByClientBeforeNowException("Start date is before now.");

        order.setOrderDate(new Timestamp(new Date().getTime()));
        order.setOrderStatus(OrderStatus.AWAITING_EXPERTS_SUGGESTION);
        order.setUser(userService.findById(order.getUser().getId()));
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

    public Orders findByUserIdAndServicesId(Long orderUserId, Long orderSubservicesId) throws Exception{
        User orderUser = userService.findById(orderUserId);
        Subservices orderSubServices = subservicesService.findById(orderSubservicesId);
        return ordersRepository.findByUserAndSubservices(orderUser, orderSubServices)
                .orElseThrow(() -> new OrderNotFoundException("Order not found."));
    }

    public List<Orders> findClientOrders(Long clientId) throws Exception{

        List<Orders> orders = ordersRepository.findAll();
        if(orders.isEmpty())
            throw new OrderNotFoundException("Order not found");

        List<Orders> clientOrders = orders.stream()
                .filter(o -> Objects.equals(o.getUser().getId(), clientId))
                .collect(Collectors.toList());

        if(clientOrders.isEmpty())
            throw new OrderNotFoundException("Order not found");

        return clientOrders;
    }

    public List<Orders> findRelatedToExpertOrders(List<Subservices> expertServices){
        List<Orders> orders = ordersRepository.findAll();
        if(orders.isEmpty())
            throw new OrderNotFoundException("Order not found");

        List<Orders> expertRelatedOrders = orders.stream()
                .filter(o -> expertServices.stream()
                        .anyMatch(es -> Objects.equals(o.getSubservices().getId(), es.getId())))
                .collect(Collectors.toList());

        if(expertRelatedOrders.isEmpty())
            throw new OrderNotFoundException("Order not found");

        return expertRelatedOrders;

    }

    public List<Orders> findSuggestibleByExpertOrders(List<Subservices> expertServices){
        List<Orders> orders = ordersRepository.findAll();
        if(orders.isEmpty())
            throw new OrderNotFoundException("Order not found");

        List<Orders> expertRelatedOrders = orders.stream()
                .filter(o -> expertServices.stream()
                        .anyMatch(es -> Objects.equals(o.getSubservices().getId(), es.getId()) &&
                                (o.getOrderStatus().equals(OrderStatus.AWAITING_EXPERT_SELECTION) || o.getOrderStatus().equals(OrderStatus.AWAITING_EXPERTS_SUGGESTION))
                        )).collect(Collectors.toList());

        if(expertRelatedOrders.isEmpty())
            throw new OrderNotFoundException("Order not found");

        return expertRelatedOrders;

    }

    public List<Orders> findAssignedToExpertOrders(Long expertId){
        List<Orders> orders = ordersRepository.findAll();
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
        return ordersRepository.save(order);
    }

    public Orders changeOrderStatusToStarted(Long orderId){
        Orders order = ordersRepository.findById(orderId)
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
            throw new AlreadyStartedOrderException("Order is already started.");
        if(order.getOrderStatus().equals(OrderStatus.DONE))
            throw new AlreadyDoneOrderException("Order is already done.");
        if(order.getOrderStatus().equals(OrderStatus.PAID))
            throw new AlreadyStartedOrderException("Order is already paid.");

        order.setOrderStatus(OrderStatus.STARTED);
        return ordersRepository.save(order);
    }

    public Orders changeOrderStatusToDone(Long orderId){
        Orders order = ordersRepository.findById(orderId)
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
            throw new NotStartedOrderYetException("Order isn't started yet.");
        if(order.getOrderStatus().equals(OrderStatus.DONE))
            throw new AlreadyStartedOrderException("Order is already done.");
        if(order.getOrderStatus().equals(OrderStatus.PAID))
            throw new AlreadyPaidOrderException("Order is already paid.");

        Suggestions selectedSuggestion = suggestionsService.findById(ordersRepository.findById(orderId).get().getSelectedSuggestionId());
        User user = selectedSuggestion.getUser();


        Timestamp startDateByExpert = selectedSuggestion.getStartDateByExpert();
        Timestamp now = new Timestamp(new Date().getTime());
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(startDateByExpert.getTime());
        cal.setTimeInMillis(now.getTime());
        int diffInHours = (int) ((now.getTime() - startDateByExpert.getTime())/3600000);
        int orderDoDurationHours = selectedSuggestion.getOrderDoDuration();
        if(diffInHours > orderDoDurationHours){
            user.setExpertPoint(user.getExpertPoint() + orderDoDurationHours - diffInHours);
            if(user.getExpertPoint()<0)
                user.setActive(false);
            userService.update(user);
        }

        order.setOrderStatus(OrderStatus.DONE);
         return ordersRepository.save(order);
    }

    public Orders changeOrderStatusToPaidByCredit(Long orderId){
        Orders order = ordersRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException("Order not found."));
        Suggestions selectedSuggestion = suggestionsService.findById(order.getSelectedSuggestionId());

        if(order.getOrderStatus().equals(OrderStatus.AWAITING_EXPERTS_SUGGESTION))
            throw new NotExpertsSuggestedYetException("No expert suggested on order yet and the order status can't be changed to paid.");
        if(order.getOrderStatus().equals(OrderStatus.AWAITING_EXPERT_SELECTION))
            throw new NotExpertSelectedYetException("No expert selected for order yet and the order status can't be changed to paid.");
        if(order.getOrderStatus().equals(OrderStatus.AWAITING_EXPERT_TO_COME_TO_YOUR_PLACE) &&
                selectedSuggestion.getStartDateByExpert().after(new Timestamp(new Date().getTime())))
            throw new NotExpertHasComeToYpurPlaceYetException("It's not time yet foe expert to come to your place and the order status can't be changed to paid.");
        if(order.getOrderStatus().equals(OrderStatus.AWAITING_EXPERT_TO_COME_TO_YOUR_PLACE) &&
                selectedSuggestion.getStartDateByExpert().before(new Timestamp(new Date().getTime())))
            throw new NotStartedOrderYetException("Order isn't started yet.");
        if(order.getOrderStatus().equals(OrderStatus.STARTED))
            throw new AlreadyStartedOrderException("Order isn't done yet.");
        if(order.getOrderStatus().equals(OrderStatus.PAID))
            throw new AlreadyPaidOrderException("Order is already paid.");

        Transactions clientTransactions = new Transactions();
        clientTransactions.setTransactionAmount(-selectedSuggestion.getExpertSuggestedPrice());
        clientTransactions.setTransactionDate(new Timestamp(new Date().getTime()));
        clientTransactions.setSrcCardNo("wallet ID: " + order.getUser().getWallet().getId());
        clientTransactions.setDestCardNo("wallet ID: " + selectedSuggestion.getUser().getWallet().getId());
        clientTransactions.setWallet(order.getUser().getWallet());
       transactionsService.createByCredit(clientTransactions);

        Transactions expertTransactions = new Transactions();
        expertTransactions.setTransactionAmount(selectedSuggestion.getExpertSuggestedPrice()*7/10);
        expertTransactions.setTransactionDate(new Timestamp(new Date().getTime()));
        expertTransactions.setSrcCardNo("wallet ID: " + order.getUser().getWallet().getId());
        expertTransactions.setDestCardNo("wallet ID: " + selectedSuggestion.getUser().getWallet().getId());
        expertTransactions.setWallet(selectedSuggestion.getUser().getWallet());
       transactionsService.createByCredit(expertTransactions);

        order.setOrderStatus(OrderStatus.PAID);
        return ordersRepository.save(order);
    }

    public void changeOrderStatusToPaidByCard(Long orderId){
        Orders order = ordersRepository.findById(orderId)
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
            throw new NotStartedOrderYetException("Order isn't started yet.");
        if(order.getOrderStatus().equals(OrderStatus.STARTED))
            throw new AlreadyStartedOrderException("Order isn't done yet.");
        if(order.getOrderStatus().equals(OrderStatus.PAID))
            throw new AlreadyPaidOrderException("Order is already paid.");

        order.setOrderStatus(OrderStatus.PAID);
        ordersRepository.save(order);
    }


}
