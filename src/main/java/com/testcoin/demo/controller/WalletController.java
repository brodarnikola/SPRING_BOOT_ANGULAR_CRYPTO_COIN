package com.testcoin.demo.controller;

import com.testcoin.demo.model.TWallet;
import com.testcoin.demo.model.response.WalletDetailsResponse;
import com.testcoin.demo.repository.TWalletRepository;
import com.testcoin.demo.service.WalletService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.Set;

@RestController
@RequestMapping("/api/coin")
public class WalletController {

    private final TWalletRepository tWalletRepository;
    private WalletService walletService; ;

    public WalletController(
                            TWalletRepository tWalletRepositoryParam,
                            WalletService walletServiceParam ) {
        tWalletRepository = tWalletRepositoryParam;
        walletService = walletServiceParam;
    }

    @GetMapping("/wallets")
    public ResponseEntity<Set<TWallet>> getWallet(@RequestParam int idUser) { 
        Set<TWallet> wallets = new HashSet<>(tWalletRepository.findByIdUser(idUser)); // Fetch from DB
        return ResponseEntity.ok(wallets);
    }

    @GetMapping("/wallets/details")
    public ResponseEntity<WalletDetailsResponse> getWalletDetails(@RequestParam int idUser) {
        WalletDetailsResponse response = walletService.getWalletDetails(idUser);
        return ResponseEntity.ok(response);
    }
}