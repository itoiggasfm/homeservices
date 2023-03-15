package com.maktabsharif.homeservices.service;

import com.maktabsharif.homeservices.Exceptions.InsufficientFondsException;
import com.maktabsharif.homeservices.Exceptions.InvalidCardNumberException;
import com.maktabsharif.homeservices.domain.Orders;
import com.maktabsharif.homeservices.domain.Transactions;
import com.maktabsharif.homeservices.repository.TransactionsRepository;
import com.maktabsharif.homeservices.validation.Validators;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Date;

@Service
public class TransactionsService {
    private final TransactionsRepository transactionsRepository;
    private final WalletService walletService;
    private final SuggestionsService suggestionsService;

    private final Validators validators;

    public TransactionsService(TransactionsRepository transactionsRepository,
                               WalletService walletService,
                               SuggestionsService suggestionsService) {
        this.transactionsRepository = transactionsRepository;
        this.walletService = walletService;
        this.suggestionsService = suggestionsService;
        validators = new Validators();
    }

    public Transactions setClientTransaction(Orders orders) {
        //Client transactions and withdrawal from wallet
        Transactions clientTransactions = new Transactions();
        clientTransactions.setTransactionAmount(-suggestionsService.findById(orders.getSelectedSuggestionId()).getSuggestedPrice());
        clientTransactions.setSrcCardNo("wallet ID: " + orders.getClient().getWallet().getId());
        clientTransactions.setDestCardNo("wallet ID: " + suggestionsService.findById(orders.getSelectedSuggestionId()).getExpert().getWallet().getId());
        clientTransactions.setWallet(orders.getClient().getWallet());
        return clientTransactions;
    }

    public Transactions setExpertTransaction(Orders orders) {
        //Expert transaction and deposit to wallet
        Transactions expertTransactions = new Transactions();
        expertTransactions.setTransactionAmount(suggestionsService.findById(orders.getSelectedSuggestionId()).getSuggestedPrice()*7/10);
        expertTransactions.setSrcCardNo("wallet ID: " + orders.getClient().getWallet().getId());
        expertTransactions.setDestCardNo("wallet ID: " + suggestionsService.findById(orders.getSelectedSuggestionId()).getExpert().getWallet().getId());
        expertTransactions.setWallet(suggestionsService.findById(orders.getSelectedSuggestionId()).getExpert().getWallet());
        return expertTransactions;
    }

    public void payByCredit(Orders order){
        create(setClientTransaction(order));
        create(setExpertTransaction(order));
    }

    public void create(Transactions transactions){

        if(transactions.getTransactionAmount() < 0 && transactions.getWallet().getBalance() < -transactions.getTransactionAmount())
            throw new InsufficientFondsException("Insufficient funds.");

        transactions.getWallet().setBalance(transactions.getWallet().getBalance() + transactions.getTransactionAmount());
        walletService.update(transactions.getWallet());
         transactionsRepository.save(transactions);

    }

    public Transactions createByCard(Transactions transactions){
        if(transactions.getTransactionAmount() < 0 && transactions.getWallet().getBalance() < -transactions.getTransactionAmount())
            throw new InsufficientFondsException("Insufficient funds.");
        if(!validators.validateCardNumber(transactions.getDestCardNo()))
            throw new InvalidCardNumberException("Invalid card number.");
        if(!validators.validateCcv2(transactions.getCvv2()))
            throw new InvalidCardNumberException("Invalid CVV2.");

        transactions.getWallet().setBalance(transactions.getWallet().getBalance() + transactions.getTransactionAmount());
        walletService.update(transactions.getWallet());
        return transactionsRepository.save(transactions);

    }

}
