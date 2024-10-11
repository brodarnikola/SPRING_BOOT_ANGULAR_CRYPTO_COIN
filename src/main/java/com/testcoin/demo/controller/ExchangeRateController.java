package com.testcoin.demo.controller;

import com.testcoin.demo.model.TExchangeRate;
import com.testcoin.demo.service.ExchangeRateService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;

@RestController
@RequestMapping("/api/coin")
public class ExchangeRateController {

    private ExchangeRateService exchangeRateService;

    public ExchangeRateController(
                                ExchangeRateService exchangeRateServiceParam) {
        exchangeRateService = exchangeRateServiceParam;
    }

    @GetMapping("/exchange-rates")
    public ResponseEntity<Set<TExchangeRate>> getExchangeRates(@RequestParam String dateFrom, @RequestParam String dateTo) {
        Set<TExchangeRate> exchangeRates = exchangeRateService.getExchangeRates(dateFrom, dateTo);
        return ResponseEntity.ok(exchangeRates);
    }

    @GetMapping("/exchange-rates-paginated")
    public ResponseEntity<Page<TExchangeRate>> getPaginatedExchangeRates(
            @RequestParam String dateFrom,
            @RequestParam String dateTo,
            @RequestParam int page,
            @RequestParam int size) {

        Pageable pageable = PageRequest.of(page, size);
        Page<TExchangeRate> paginatedRates = exchangeRateService.getPaginatedExchangeRates(dateFrom, dateTo, pageable);
        return ResponseEntity.ok(paginatedRates);
    }
}