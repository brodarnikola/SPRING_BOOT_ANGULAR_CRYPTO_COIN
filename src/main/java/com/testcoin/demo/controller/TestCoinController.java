package com.testcoin.demo.controller;

import com.testcoin.demo.model.TExchangeRate;
import com.testcoin.demo.model.TUser;
import com.testcoin.demo.model.TWallet;
import com.testcoin.demo.model.response.WalletDetailsResponse;
import com.testcoin.demo.repository.TExchangeRateRepository;
import com.testcoin.demo.repository.TUserRepository;
import com.testcoin.demo.repository.TWalletRepository;
import com.testcoin.demo.service.ExchangeRateService;
import com.testcoin.demo.service.WalletService;
 
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/coin")
public class TestCoinController {


    private final TUserRepository tUserRepository;
    private final TWalletRepository tWalletRepository;
    private final TExchangeRateRepository tExchangeRateRepository;
    private WalletService walletService;
    private ExchangeRateService exchangeRateService;

    public TestCoinController(  TUserRepository tUserRepositoryParam,
                              TWalletRepository tWalletRepositoryParam, 
                              TExchangeRateRepository tExchangeRateRepositoryParam,
                              WalletService walletServiceParam,
                              ExchangeRateService exchangeRateServiceParam) {
        tUserRepository = tUserRepositoryParam;
        tWalletRepository = tWalletRepositoryParam;
        tExchangeRateRepository = tExchangeRateRepositoryParam;
        walletService = walletServiceParam;
        exchangeRateService = exchangeRateServiceParam;
    }

    @GetMapping("/users")
    public ResponseEntity<List<TUser>> getAllUsers() {
        List<TUser> users = tUserRepository.findAll();  
        return ResponseEntity.ok(users);
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<TUser> getUser(@PathVariable int id) {
        Optional<TUser> user = tUserRepository.findById(id); // Retrieve from database
        return user.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/exchange-rates")
    public ResponseEntity<Set<TExchangeRate>> getExchangeRates(@RequestParam String dateFrom, @RequestParam String dateTo) {
        Set<TExchangeRate> exchangeRates = exchangeRateService.getExchangeRates(dateFrom, dateTo);
        return ResponseEntity.ok(exchangeRates);
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