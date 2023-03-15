package com.maktabsharif.homeservices.domain;

import jakarta.persistence.Entity;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Entity
@Data
@AllArgsConstructor
//@NoArgsConstructor
@SuperBuilder
public class Admin extends User{
}
