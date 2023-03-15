package com.maktabsharif.homeservices.dto;

import com.maktabsharif.homeservices.domain.enumeration.OrderStatus;
import lombok.*;;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class NewOrderCheckDto {

    OrderStatus orderStatus;
    ClientDto clientDto;
    SubservicesDto subservicesDto;


}