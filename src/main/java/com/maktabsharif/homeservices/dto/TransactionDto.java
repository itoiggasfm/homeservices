package com.maktabsharif.homeservices.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.*;
import jakarta.validation.constraints.Pattern;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class TransactionDto {

    @Pattern(regexp = "^[0-9]{16}", message = "Card number is 10-digit number.")
    String srcCardNumber;
    @Pattern(regexp = "^[0-9]{16}", message = "Card number is 10-digit number.")
    String destCardNumber;
//    @Pattern(regexp = "^[0-9]+$")
    Double transactionAmount;
    @Pattern(regexp = "^[0-9]{3,4}", message = "Card number is 3 or 4-digit number.")
    String cvv2;
    @Pattern(regexp = "^[0-9]{1,2}", message = "Card number is 1 or 2-digit number.")
    String expYear;
    @Pattern(regexp = "^[0-9]{1,2}", message = "Card number is 1 or 2-digit number.")
    @Min(1)
    @Max(12)
    String expMonth;
    String secPin;
    @Pattern(regexp = "^[A-za-z0-9]{5}")
    String cltCaptcha;
    String srvCaptcha;
    String cvv2EdSaved;

}

