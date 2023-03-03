package com.maktabsharif.homeservices.service;

import com.maktabsharif.homeservices.Exceptions.InsufficientFondsException;
import com.maktabsharif.homeservices.Exceptions.InvalidCardNumberException;
import com.maktabsharif.homeservices.domain.Transactions;
import com.maktabsharif.homeservices.repository.TransactionsRepository;
import com.maktabsharif.homeservices.validation.Validators;
import org.springframework.stereotype.Service;

@Service
public class TransactionsService {
    private final TransactionsRepository transactionsRepository;
    private final WalletService walletService;

    private final Validators validators;

    public TransactionsService(TransactionsRepository transactionsRepository,
                               WalletService walletService) {
        this.transactionsRepository = transactionsRepository;
        this.walletService = walletService;
        validators = new Validators();
    }



    public Transactions createByCredit(Transactions transactions){
        if(transactions.getTransactionAmount() < 0 && transactions.getWallet().getBalance() < -transactions.getTransactionAmount())
            throw new InsufficientFondsException("Insufficient funds.");

        transactions.getWallet().setBalance(transactions.getWallet().getBalance() + transactions.getTransactionAmount());
        walletService.update(transactions.getWallet());
        return transactionsRepository.save(transactions);

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
