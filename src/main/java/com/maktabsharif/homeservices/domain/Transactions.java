package com.maktabsharif.homeservices.domain;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.sql.Timestamp;


@Entity
@Table(name = "transactions")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Transactions {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    private Long id;
    @Column(name = "transaction_date", nullable = false)
    private Timestamp transactionDate;
    @Column(name = "transaction_amount")
    private Double transactionAmount;
    @Column(name = "src_card_No")
    private String srcCardNo;
    @Column(name = "exp_year")
    private Integer expYear;
    @Column(name = "exp_month")
    private Integer expMonth;
    @Column(name = "cvv2")
    private String cvv2;
    @Column(name = "cvv2_ed_saved")
    private boolean cvv2EdSaved;
    @Column(name = "dest_card_No")
    private String destCardNo;

//    @ManyToOne
//    private User srcUser;
//    @ManyToOne
//    private User destUser;


    @ManyToOne/*(fetch = FetchType.LAZY)*/
    @JoinColumn(name = "wallet_id", referencedColumnName = "id", nullable = false)
    private Wallet wallet;

}
