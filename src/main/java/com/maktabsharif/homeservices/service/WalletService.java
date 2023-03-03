package com.maktabsharif.homeservices.service;

import com.maktabsharif.homeservices.Exceptions.WalletNotFoundException;
import com.maktabsharif.homeservices.domain.Wallet;
import com.maktabsharif.homeservices.repository.WalletRepository;
import org.springframework.stereotype.Service;

@Service
public class WalletService {

    private final WalletRepository walletRepository;

    public WalletService(WalletRepository walletRepository) {
        this.walletRepository = walletRepository;
    }


    public Wallet save(Wallet wallet){
        return walletRepository.save(wallet);
    }

    public Wallet findById(Long id){
        return walletRepository.findById(id)
                .orElseThrow(() -> new WalletNotFoundException("Wallet not found."));
    }

    public Wallet update(Wallet wallet){
        return walletRepository.save(wallet);
    }
}
