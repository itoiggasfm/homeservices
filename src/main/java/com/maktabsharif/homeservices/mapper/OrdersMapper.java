package com.maktabsharif.homeservices.mapper;

import com.maktabsharif.homeservices.domain.Orders;
import com.maktabsharif.homeservices.dto.OrdersDto;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface OrdersMapper {

    OrdersMapper INSTANCE = Mappers.getMapper(OrdersMapper.class);
    Orders dtoToModel(OrdersDto ordersDto);

}
