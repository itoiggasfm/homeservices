package com.maktabsharif.homeservices.domain;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import java.util.List;

@Entity
@Table(name = "subservices")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Subservices {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    private Long id;

    @Column(name = "subservice_title")
    private String subserviceTitle;

    @Column(name = "base_price")
    private Double basePrice;

    @Column(name = "description")
    private String description;

    @ManyToOne
    Services services;

    @LazyCollection(LazyCollectionOption.FALSE)
    @ManyToMany(mappedBy = "subservices")
     List<Expert> exprt;

    @LazyCollection(LazyCollectionOption.FALSE)
    @OneToMany(mappedBy = "subservices", cascade = CascadeType.MERGE/*, fetch = FetchType.LAZY*/)
     List<Orders> orders;

}
