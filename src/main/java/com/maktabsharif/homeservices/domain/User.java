package com.maktabsharif.homeservices.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.maktabsharif.homeservices.domain.enumeration.*;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import java.sql.Timestamp;
import java.util.List;

@Entity
@Table(name = "users")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    private Long id;
    @Column(name = "name")
    private String name;
    @Column(name = "family_name")
    private String familyName;
    @Column(name = "username", unique = true, nullable = false)
    private String username;
    @Column(name = "password", nullable = false)
    private String password;
    @Column(name = "email", unique = true)
    private String email;
    @Column(name = "tempEmail")
    private String tempEmail;
    @Column(name = "register_date")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss"/*, timezone = "Asia/Tehran"*/)
    private Timestamp registerDate;

    @Column(name = "user_role")
    private UserRole userRole;

    @Column(name = "expert_status")
    private ExpertStatus expertStatus;

    @Column(name = "expert_point")
    private Integer expertPoint;

    @Column(name = "profile_photo_name")
    private String profilePhotoName;

    @Column(name = "active", columnDefinition = "boolean default false")
    private boolean active;

    @LazyCollection(LazyCollectionOption.FALSE)
    @ManyToMany/*(cascade = CascadeType.MERGE, fetch = FetchType.EAGER)*/
    @JoinTable(
            name = "user_services",
            joinColumns = {@JoinColumn(name = "user_id", referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "services_id", referencedColumnName = "id")})
    private List<Subservices> subservices;

    @OneToOne(cascade = CascadeType.ALL/*, fetch = FetchType.EAGER*/)
    @JoinColumn(name = "wallet_id", referencedColumnName = "id")
    private Wallet wallet;

    @LazyCollection(LazyCollectionOption.FALSE)
    @OneToMany(mappedBy = "user")
    private List<Orders> orders;

    @LazyCollection(LazyCollectionOption.FALSE)
    @OneToMany(mappedBy = "user")
    private List<Suggestions> suggestions;

}
