package com.maktabsharif.homeservices.controller;

import com.maktabsharif.homeservices.domain.Orders;
import com.maktabsharif.homeservices.domain.Subservices;
import com.maktabsharif.homeservices.dto.OrdersDto;
import com.maktabsharif.homeservices.dto.SubservicesDto;
import com.maktabsharif.homeservices.mapper.OrdersMapper;
import com.maktabsharif.homeservices.mapper.SubservicesMapper;
import com.maktabsharif.homeservices.service.OrdersService;
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
    public Orders update(@RequestBody OrdersDto ordersDto) throws Exception {
        Orders orders = OrdersMapper.INSTANCE.dtoToModel(ordersDto);
        return ordersService.updateByOrder(orders);
    }

    @GetMapping("/findById/{userId}")
    public Orders findById(@PathVariable Long ordersId) throws Exception {
        return ordersService.findById(ordersId);
    }

    @GetMapping("/findAll")
    public List<Orders> findAll() throws Exception {
        return ordersService.findAll();
    }

    @GetMapping("/findByUserIdAndServicesId")
    public Orders findByUserIdAndServicesId(@RequestParam(value = "userId", required = false)Long userId,
                                            @RequestParam(value = "subservicesId", required = false)Long subservicesId) throws Exception {
        return ordersService.findByUserIdAndServicesId(userId, subservicesId);
    }

    @GetMapping("/findClientOrders/{clientId}")
    public List<Orders> findClientOrders(@PathVariable Long clientId) throws Exception {
        return ordersService.findClientOrders(clientId);

    }

    @GetMapping("/findAssignedToExpertOrders/{userId}")
    public List<Orders> findAssignedToExpertOrders(@PathVariable Long userId) throws Exception{
        return ordersService.findAssignedToExpertOrders(userId);
    }

    @PostMapping("/findRelatedToExpertOrders")
    public List<Orders> findRelatedToExpertOrders(@RequestBody List<SubservicesDto> expertServicesDtoList){
        List<Subservices> expertServicesList = new ArrayList<>();
        for(SubservicesDto subservicesDto: expertServicesDtoList){
            expertServicesList.add(SubservicesMapper.INSTANCE.dtoToModel(subservicesDto));
        }
        return ordersService.findRelatedToExpertOrders(expertServicesList);
    }

    @PostMapping("/findSuggestibleByExpertOrders")
    public List<Orders> findSuggestibleByExpertOrders(@RequestBody List<SubservicesDto> expertServicesDtoList){
        List<Subservices> expertServicesList = new ArrayList<>();
        for(SubservicesDto subservicesDto: expertServicesDtoList){
            expertServicesList.add(SubservicesMapper.INSTANCE.dtoToModel(subservicesDto));
        }
        return ordersService.findSuggestibleByExpertOrders(expertServicesList);
    }

}
