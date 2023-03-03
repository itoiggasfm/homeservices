package com.maktabsharif.homeservices.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.maktabsharif.homeservices.domain.Orders;
import com.maktabsharif.homeservices.domain.User;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Component
public class SuggestionsDto {

    String expertSuggestion;
    Double expertSuggestedPrice;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss"/*, timezone = "Asia/Tehran"*/)
    Timestamp expertSuggestionDate;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss"/*, timezone = "Asia/Tehran"*/)
    Timestamp startDateByExpert;
    Integer orderDoDuration;
    Boolean selected;
    User user;
    Orders orders;

}

