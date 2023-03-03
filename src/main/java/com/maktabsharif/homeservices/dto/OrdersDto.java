package com.maktabsharif.homeservices.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.maktabsharif.homeservices.domain.Subservices;
import com.maktabsharif.homeservices.domain.Suggestions;
import com.maktabsharif.homeservices.domain.User;
import com.maktabsharif.homeservices.domain.enumeration.OrderStatus;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.util.List;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Component
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
//    UserDto userDto;
//    List<SuggestionsDto> suggestionsDtoList;
//    SubservicesDto subservicesDto;
    User user;
    List<Suggestions> suggestions;
    Subservices subservices;


}

