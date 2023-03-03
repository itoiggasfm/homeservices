package com.maktabsharif.homeservices.dto;

import com.maktabsharif.homeservices.domain.Orders;
import com.maktabsharif.homeservices.domain.Services;
import com.maktabsharif.homeservices.domain.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import org.springframework.stereotype.Component;

import java.util.List;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Component
public class SubservicesDto {

    Long id;
    String subserviceTitle;
    Double basePrice;
    String description;
//    ServicesDto servicesDto;
//    List<UserDto> userDtoList;
//    List<OrdersDto> ordersDtoList;
    Services services;
    List<User> user;
    List<Orders> orders;

}

