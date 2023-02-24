package com.maktabsharif.homeservices.domain;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;


import java.util.List;

@Entity
@Table(name = "wallet")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Wallet{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    private Long id;

    @Column(name = "balance")
    private Double balance;

    @LazyCollection(LazyCollectionOption.FALSE)
    @OneToMany(mappedBy = "wallet", cascade = CascadeType.MERGE/*,
            fetch = FetchType.EAGER*/)
    private List<Transactions> transaction;


    @OneToOne(mappedBy = "wallet")
    private User user;

}
