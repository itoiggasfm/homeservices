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
public class Services {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    private Long id;

    @Column(name = "service_title", unique = true)
    private String serviceTitle;

    @LazyCollection(LazyCollectionOption.FALSE)
    @OneToMany(mappedBy = "services", cascade = CascadeType.MERGE/*, fetch = FetchType.LAZY*/)
     List<Subservices> subservices;
}
