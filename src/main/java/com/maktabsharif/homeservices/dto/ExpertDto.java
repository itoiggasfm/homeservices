package com.maktabsharif.homeservices.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.maktabsharif.homeservices.domain.Subservices;
import com.maktabsharif.homeservices.domain.Suggestions;
import com.maktabsharif.homeservices.domain.Wallet;
import com.maktabsharif.homeservices.domain.enumeration.ExpertStatus;
import com.maktabsharif.homeservices.domain.enumeration.Role;
import jakarta.persistence.*;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
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
public class ExpertDto{

        String name;

        String familyName;

        String username;

        String password;

        @Email
        String email;

}

