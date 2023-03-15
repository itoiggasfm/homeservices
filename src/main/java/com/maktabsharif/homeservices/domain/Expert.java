package com.maktabsharif.homeservices.domain;

import com.maktabsharif.homeservices.domain.enumeration.ExpertStatus;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class Expert extends User{



    @Column(name = "expert_status")
    @Enumerated(EnumType.STRING)
    private ExpertStatus expertStatus;

    @Column(name = "expert_point")
    private Integer expertPoint;

    @LazyCollection(LazyCollectionOption.FALSE)
    @ManyToMany/*(cascade = CascadeType.MERGE, fetch = FetchType.EAGER)*/
    @JoinTable(
            name = "user_services",
            joinColumns = {@JoinColumn(name = "user_id", referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "services_id", referencedColumnName = "id")})
    private List<Subservices> subservices;

    @LazyCollection(LazyCollectionOption.FALSE)
    @OneToMany(mappedBy = "expert")
    private List<Suggestions> suggestions;

}
