package com.maktabsharif.homeservices.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.maktabsharif.homeservices.domain.enumeration.OrderStatus;
import lombok.*;


import java.sql.Timestamp;
import java.util.List;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class OrdersDto {

    String workDescription;
    Double clientSuggestedPrice;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss"/*, timezone = "Asia/Tehran"*/)
    Timestamp orderDate;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss"/*, timezone = "Asia/Tehran"*/)
    Timestamp startDateByClient;
    String address;
    OrderStatus orderStatus;
    String comment;
    Long selectedSuggestionId;
    ClientDto clientDto;
    List<SuggestionsDto> suggestionsDtoList;
    SubservicesDto subservicesDto;


}

