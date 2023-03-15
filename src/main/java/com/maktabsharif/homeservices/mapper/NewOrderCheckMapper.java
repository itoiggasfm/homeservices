package com.maktabsharif.homeservices.mapper;

import com.maktabsharif.homeservices.domain.Orders;
import com.maktabsharif.homeservices.dto.NewOrderCheckDto;
import com.maktabsharif.homeservices.dto.OrdersDto;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

;

@Mapper
public interface NewOrderCheckMapper {

    NewOrderCheckMapper INSTANCE = Mappers.getMapper(NewOrderCheckMapper.class);
    Orders dtoToModel(NewOrderCheckDto newOrderCheckDto);

}