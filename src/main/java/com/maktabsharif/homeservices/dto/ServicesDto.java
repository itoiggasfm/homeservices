package com.maktabsharif.homeservices.dto;

import com.maktabsharif.homeservices.domain.Subservices;
import jakarta.persistence.CascadeType;
import jakarta.persistence.OneToMany;
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
public class ServicesDto {

    String serviceTitle;
    List<Subservices> subservices;

}

