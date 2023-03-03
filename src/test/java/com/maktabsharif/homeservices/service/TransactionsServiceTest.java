package com.maktabsharif.homeservices.service;

import com.maktabsharif.homeservices.domain.Transactions;
import com.maktabsharif.homeservices.domain.Wallet;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.sql.Timestamp;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class TransactionsServiceTest {

    @Autowired
    TransactionsService transactionsService;

    Transactions transactions = Transactions.builder()
            .destCardNo("0123456789012345")
            .cvv2("1234")
            .transactionAmount(5000d)
            .transactionDate(new Timestamp(new Date().getTime()))
            .wallet(Wallet.builder()
                    .id(4l)
                    .balance(0d)
                    .build())
            .build();

    @Test
    void createByCredit() {
    }

    @Test
    void createByCard() {
        transactionsService.createByCard(transactions);
    }
}