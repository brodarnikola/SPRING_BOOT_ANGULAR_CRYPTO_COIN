package com.testcoin.demo.repository;

import com.testcoin.demo.model.TExchangeRate;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface TExchangeRateRepository extends JpaRepository<TExchangeRate, Integer> {
    List<TExchangeRate> findByExcRateDateBetween(LocalDate from, LocalDate to); // Fet
}
