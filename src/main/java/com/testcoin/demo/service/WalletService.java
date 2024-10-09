package com.testcoin.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.testcoin.demo.model.TWallet;
import com.testcoin.demo.model.response.WalletDetailsResponse;
import com.testcoin.demo.repository.TWalletRepository;

import java.util.Comparator;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
public class WalletService {

    @Autowired
    private TWalletRepository tWalletRepository;

    public WalletDetailsResponse getWalletDetails(int idUser) {
        Set<TWallet> wallets = new HashSet<>(tWalletRepository.findByIdUser(idUser));
        Optional<TWallet> lastPurchase = wallets.stream()
                .max(Comparator.comparing(TWallet::getCountTimeStamp));

        double totalWorth = wallets.size(); // Each token is worth 1

        return new WalletDetailsResponse(wallets, totalWorth, lastPurchase.orElse(null));
    }
}
