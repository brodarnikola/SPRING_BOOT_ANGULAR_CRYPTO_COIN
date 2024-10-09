package com.testcoin.demo.service;

import com.github.javafaker.Faker;
import com.testcoin.demo.model.TExchangeRate;
import com.testcoin.demo.model.TUser;
import com.testcoin.demo.model.TWallet;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class TestCoinService {

    private final Faker faker = new Faker();

    public TUser getUser(int id) {
        TUser user = new TUser();
        user.setId(id);
        user.setFullName(faker.name().fullName());
        user.setAddress(faker.address().fullAddress());
        user.setPostal(faker.address().zipCode());
        user.setCity(faker.address().city());
        user.setCoinValue(0); // Can be computed from wallet later
        return user;
    }

    public Set<TWallet> getWallet(int idUser) {
        Set<TWallet> wallets = new HashSet<>();
//        for (int i = 0; i < 500; i++) {
        for (int i = 0; i < 500; i++) {
            TWallet wallet = new TWallet();
            wallet.setIdUser(idUser);
            wallet.setCountTimeStamp(LocalDateTime.now().minusDays(faker.number().numberBetween(1, 100)));
            wallets.add(wallet);
        }
        return wallets;
    }

    public Set<TExchangeRate> getExchangeRates(LocalDate from, LocalDate to) {
        Set<TExchangeRate> rates = new HashSet<>();
        LocalDate currentDate = from;
        while (!currentDate.isAfter(to)) {
            TExchangeRate rate = new TExchangeRate();
            rate.setExcRateDate(currentDate);
            rate.setExcRateEur(faker.number().randomDouble(2, 60, 135) / 100);
            rate.setExcRateUsd(faker.number().randomDouble(2, 115, 180) / 100);
            rate.setExcRateGbp(faker.number().randomDouble(2, 20, 70) / 100);
            rates.add(rate);
            currentDate = currentDate.plusDays(1);
        }
        return rates;
    }
}