package com.testcoin.demo.controller;

import com.testcoin.demo.model.TExchangeRate;
import com.testcoin.demo.model.TUser;
import com.testcoin.demo.model.TWallet;
import com.testcoin.demo.repository.TExchangeRateRepository;
import com.testcoin.demo.repository.TUserRepository;
import com.testcoin.demo.repository.TWalletRepository;
import com.testcoin.demo.service.TestCoinService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@RestController
@RequestMapping("/api/coin")
public class TestCoinController {

//    @Autowired
    private final TUserRepository tUserRepository;
    private final TWalletRepository tWalletRepository;
    private final TExchangeRateRepository tExchangeRateRepository;

    public TestCoinController(  TUserRepository tUserRepositoryParam,
                              TWalletRepository tWalletRepositoryParam, TExchangeRateRepository tExchangeRateRepositoryParam) {
        tUserRepository = tUserRepositoryParam;
        tWalletRepository = tWalletRepositoryParam;
        tExchangeRateRepository = tExchangeRateRepositoryParam;
    }

    @GetMapping("/user/{id}")
    public ResponseEntity<TUser> getUser(@PathVariable int id) {
        Optional<TUser> user = tUserRepository.findById(id); // Retrieve from database
        return user.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/wallet")
    public ResponseEntity<Set<TWallet>> getWallet(@RequestParam int idUser) {
//        Set<TWallet> wallets = testCoinService.getWallet(idUser);
//        return ResponseEntity.ok(wallets);
        Set<TWallet> wallets = new HashSet<>(tWalletRepository.findByIdUser(idUser)); // Fetch from DB
        return ResponseEntity.ok(wallets);
    }

    @GetMapping("/exchange-rates")
    public ResponseEntity<Set<TExchangeRate>> getExchangeRates(@RequestParam String dateFrom, @RequestParam String dateTo) {
//        LocalDate from = LocalDate.parse(dateFrom);
//        LocalDate to = LocalDate.parse(dateTo);
//        Set<TExchangeRate> exchangeRates = testCoinService.getExchangeRates(from, to);
//        return ResponseEntity.ok(exchangeRates);
        LocalDate from = LocalDate.parse(dateFrom);
        LocalDate to = LocalDate.parse(dateTo);
        Set<TExchangeRate> exchangeRates = new HashSet<>(tExchangeRateRepository.findByExcRateDateBetween(from, to)); // Fetch from DB
        return ResponseEntity.ok(exchangeRates);
    }
}