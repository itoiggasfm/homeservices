package com.maktabsharif.homeservices.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.maktabsharif.homeservices.domain.User;
import com.maktabsharif.homeservices.domain.Wallet;
import com.maktabsharif.homeservices.domain.enumeration.ExpertStatus;
import com.maktabsharif.homeservices.domain.enumeration.Role;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.*;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.util.List;


@Getter
@Setter
@AllArgsConstructor
//@NoArgsConstructor
@ToString
@Component
public class AdminDto extends UserDto {

}

