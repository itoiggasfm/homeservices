package com.maktabsharif.homeservices.domain;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import java.util.List;

@Entity
@Table(name = "services")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Services{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    private Long id;
    @Column(name = "service_title")
    private String serviceTitle;
    @Column(name = "subservice_title")
    private String subserviceTitle;
    @Column(name = "base_price")
    private Double basePrice;
    @Column(name = "description")
    private String description;

    @LazyCollection(LazyCollectionOption.FALSE)
    @ManyToMany(mappedBy = "services")
     List<User> user;

    @LazyCollection(LazyCollectionOption.FALSE)
    @OneToMany(mappedBy = "services", cascade = CascadeType.MERGE/*, fetch = FetchType.LAZY*/)
     List<Orders> orders;

}
