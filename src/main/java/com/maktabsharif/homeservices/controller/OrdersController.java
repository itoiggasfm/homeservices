package com.maktabsharif.homeservices.controller;

import com.maktabsharif.homeservices.domain.Client;
import com.maktabsharif.homeservices.domain.Orders;
import com.maktabsharif.homeservices.domain.Subservices;
import com.maktabsharif.homeservices.dto.NewOrderCheckDto;
import com.maktabsharif.homeservices.dto.OrdersDto;
import com.maktabsharif.homeservices.dto.SubservicesDto;
import com.maktabsharif.homeservices.mapper.ClientMapper;
import com.maktabsharif.homeservices.mapper.NewOrderCheckMapper;
import com.maktabsharif.homeservices.mapper.OrdersMapper;
import com.maktabsharif.homeservices.mapper.SubservicesMapper;
import com.maktabsharif.homeservices.service.OrdersService;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/orders")
public class OrdersController {

    private final OrdersService ordersService;

    public OrdersController(OrdersService ordersService) {
        this.ordersService = ordersService;
    }


    @PostMapping("/create")
    public Orders create(@RequestBody OrdersDto ordersDto) throws Exception {
        Orders orders = OrdersMapper.INSTANCE.dtoToModel(ordersDto);
        System.out.println(orders);
        return ordersService.create(orders);
    }

    @PutMapping("/update")
    @PreAuthorize("hasRole('ADMIN')")
    public Orders update(@RequestBody OrdersDto ordersDto) throws Exception {
        Orders orders = OrdersMapper.INSTANCE.dtoToModel(ordersDto);
        return ordersService.updateByOrder(orders);
    }

    @GetMapping("/findById/{userId}")
    @PreAuthorize("hasRole('ADMIN')")
    public Orders findById(@PathVariable Long ordersId) throws Exception {
        return ordersService.findById(ordersId);
    }

    @GetMapping("/findAll")
    @PreAuthorize("hasRole('ADMIN')")
    public List<Orders> findAll() throws Exception {
        return ordersService.findAll();
    }

    @PostMapping("/findByUserIdAndServicesId")
    @PreAuthorize("hasRole('ADMIN')")
    public Orders findByClientAndSubservicesAndOrOrderStatusNot(@Valid @RequestBody NewOrderCheckDto newOrderCheckDto) throws Exception {
        Client client = ClientMapper.INSTANCE.dtoToModel(newOrderCheckDto.getClientDto());
        Subservices subservices = SubservicesMapper.INSTANCE.dtoToModel(newOrderCheckDto.getSubservicesDto());
        Orders orders = NewOrderCheckMapper.INSTANCE.dtoToModel(newOrderCheckDto);
        orders.setClient(client);
        orders.setSubservices(subservices);
        return ordersService.findByClientAndSubservicesAndOrderStatusNot(orders);
    }

    @GetMapping("/findClientOrders/{clientId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'CLIENT')")
    public List<Orders> findClientOrders(@PathVariable Long clientId) throws Exception {
        return ordersService.findClientOrders(clientId);

    }

    @GetMapping("/findAssignedToExpertOrders/{userId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'EXPERT')")
    public List<Orders> findAssignedToExpertOrders(@PathVariable Long userId){
        return ordersService.findAssignedToExpertOrders(userId);
    }

    @PostMapping("/findSuggestibleByExpertOrders")
    @PreAuthorize("hasAnyRole('ADMIN', 'EXPERT')")
    public List<Orders> findSuggestibleByExpertOrders(@RequestBody List<SubservicesDto> expertServicesDtoList){
        List<Subservices> expertServicesList = new ArrayList<>();
        for(SubservicesDto subservicesDto: expertServicesDtoList){
            expertServicesList.add(SubservicesMapper.INSTANCE.dtoToModel(subservicesDto));
        }
        return ordersService.findSuggestibleByExpertOrders(expertServicesList);
    }

    @GetMapping("/rateExpert/{userId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'CLIENT')")
    public String rateExpert(@PathVariable Long orderId, @RequestParam Integer clientPointToExpert){
        ordersService.rateExpert(orderId, clientPointToExpert);
        return "Done";
    }

    @GetMapping("/comment/{userId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'CLIENT')")
    public String comment(@PathVariable Long orderId, @RequestParam String comment) {
        ordersService.comment(orderId, comment);
        return "Done";
    }

    @PostMapping("/payByCredit")
    @PreAuthorize("hasAnyRole('ADMIN', 'CLIENT')")
    public String payByCredit(@RequestBody OrdersDto ordersDto){
        Orders orders = OrdersMapper.INSTANCE.dtoToModel(ordersDto);
        ordersService.payByCredit(orders);
        return "done";
    }

    @GetMapping("/started/{userId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'EXPERT')")
    public String changeOrderStatusToStarted(@PathVariable Long orderId){
        ordersService.changeOrderStatusToStarted(orderId);
        return "Done";
    }

    @GetMapping("/done/{userId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'EXPERT')")
    public String changeOrderStatusToDone(@PathVariable Long orderId){
        ordersService.changeOrderStatusToDone(orderId);
        return "Done";
    }

    @GetMapping("/paid/{userId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'CLIENT')")
    public String changeOrderStatusToPaid(@PathVariable Long orderId){
        ordersService.changeOrderStatusToPaid(orderId);
        return "Done";
    }

}
