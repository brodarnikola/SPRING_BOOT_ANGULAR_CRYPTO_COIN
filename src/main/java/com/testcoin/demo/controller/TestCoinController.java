package com.testcoin.demo.controller;

import com.testcoin.demo.model.TExchangeRate;
import com.testcoin.demo.model.TUser;
import com.testcoin.demo.model.TWallet;
import com.testcoin.demo.model.response.WalletDetailsResponse;
import com.testcoin.demo.repository.TExchangeRateRepository;
import com.testcoin.demo.repository.TUserRepository;
import com.testcoin.demo.repository.TWalletRepository; 
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

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

    @GetMapping("/wallets")
    public ResponseEntity<Set<TWallet>> getWallet(@RequestParam int idUser) { 
        Set<TWallet> wallets = new HashSet<>(tWalletRepository.findByIdUser(idUser)); // Fetch from DB
        return ResponseEntity.ok(wallets);
    }

    @GetMapping("/wallets/details")
    public ResponseEntity<WalletDetailsResponse> getWalletDetails(@RequestParam int idUser) {
        Set<TWallet> wallets = new HashSet<>(tWalletRepository.findByIdUser(idUser));
        Optional<TWallet> lastPurchase = wallets.stream()
                .max(Comparator.comparing(TWallet::getCountTimeStamp));

        double totalWorth = wallets.size(); // Each token is worth 1

        WalletDetailsResponse response = new WalletDetailsResponse(wallets, totalWorth, lastPurchase.orElse(null));

        return ResponseEntity.ok(response);
    }

    private double getWalletWorth(TWallet wallet) {
        // Implement logic to calculate the worth of the wallet
        return 0.0; // Placeholder
    }

    @GetMapping("/exchange-rates")
    public ResponseEntity<Set<TExchangeRate>> getExchangeRates(@RequestParam String dateFrom, @RequestParam String dateTo) { 
        LocalDate from = LocalDate.parse(dateFrom);
        LocalDate to = LocalDate.parse(dateTo);
        Set<TExchangeRate> exchangeRates = new HashSet<>(tExchangeRateRepository.findByExcRateDateBetween(from, to)); // Fetch from DB
        return ResponseEntity.ok(exchangeRates.stream()
                .sorted(Comparator.comparing(TExchangeRate::getExcRateDate))
                .collect(Collectors.toCollection(LinkedHashSet::new)));
    }
}