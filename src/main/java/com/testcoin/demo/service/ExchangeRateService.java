package com.testcoin.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.testcoin.demo.model.TExchangeRate;
import com.testcoin.demo.repository.TExchangeRateRepository;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ExchangeRateService {

    @Autowired
    private TExchangeRateRepository tExchangeRateRepository;

    public Set<TExchangeRate> getExchangeRates(String dateFrom, String dateTo) {
        LocalDate from = LocalDate.parse(dateFrom);
        LocalDate to = LocalDate.parse(dateTo);
        Set<TExchangeRate> exchangeRates = new HashSet<>(tExchangeRateRepository.findByExcRateDateBetween(from, to));
        return exchangeRates.stream()
                .sorted(Comparator.comparing(TExchangeRate::getExcRateDate))
                .collect(Collectors.toCollection(LinkedHashSet::new));
    }
}
