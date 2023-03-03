package com.maktabsharif.homeservices.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.maktabsharif.homeservices.domain.Orders;
import com.maktabsharif.homeservices.domain.Subservices;
import com.maktabsharif.homeservices.domain.Suggestions;
import com.maktabsharif.homeservices.domain.Wallet;
import com.maktabsharif.homeservices.domain.enumeration.ExpertStatus;
import com.maktabsharif.homeservices.domain.enumeration.UserRole;
import jakarta.validation.constraints.*;
import lombok.*;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.util.List;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Component
public class UserDto {

        Long id;
        String name;
        String familyName;
        String username;
        String password;
        @Email
        String email;
        UserRole userRole;
        Wallet wallet;

//        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss"/*, timezone = "Asia/Tehran"*/)
//        Timestamp registerDate;
//        ExpertStatus expertStatus;
//        @Digits(integer = 1, fraction = 0)
//        @Min(1)
//        @Max(5)
//        Integer expertPoint;
//        String profilePhotoName;
//        boolean active;
//        List<SubservicesDto> subservicesDtoList;
//        List<OrdersDto> ordersDtoList;
//        List<SuggestionsDto> suggestionsDtoList;

}

