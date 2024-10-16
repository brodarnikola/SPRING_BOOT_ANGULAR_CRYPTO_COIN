package com.testcoin.demo.task;

import com.testcoin.demo.model.TExchangeRate;
import com.testcoin.demo.model.TUser;
import com.testcoin.demo.model.TWallet;
import com.testcoin.demo.repository.TExchangeRateRepository;
import com.testcoin.demo.repository.TUserRepository;
import com.testcoin.demo.repository.TWalletRepository;
import com.testcoin.demo.service.InitialCoinService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
public class InitialCoinImport implements Runnable {

    private static final int THREAD_POOL_SIZE = 10;

    private static final Logger log = LoggerFactory.getLogger(InitialCoinImport.class);


    @Autowired
    private InitialCoinService initialCoinService;

    @Autowired
    private TUserRepository tUserRepository;

    @Autowired
    private TWalletRepository tWalletRepository;

    @Autowired
    private TExchangeRateRepository tExchangeRateRepository;

//    @PostConstruct
//    public void init() {
//
//    }

    @Override
    public void run() {
        initialDataImport();
    }

    public void initialDataImport() {

        // Run data loading in a separate thread pool
        ExecutorService executor = Executors.newFixedThreadPool(THREAD_POOL_SIZE);

        // Fake data generation tasks
        List<Runnable> tasks = new ArrayList<>();

        tasks.add(this::generateUsers);
        tasks.add(this::generateWallets);
        tasks.add(this::generateExchangeRates);

        // Initialize CountDownLatch with the number of tasks
        CountDownLatch latch = new CountDownLatch(tasks.size());

        // Submit each task to the thread pool
        for (Runnable task : tasks) {
            executor.submit(() -> {
                try {
                    task.run();
                } finally {
                    latch.countDown(); // Decrement latch count when the task is done
                }
            });
        }

        try {
            latch.await(); // Wait for all tasks to complete
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        executor.shutdown(); // Shutdown the executor

    }



    private void generateUsers() {
        for (int i = 0; i < 3; i++) { // Generate 100 users
            TUser user = initialCoinService.getUser(i + 1);
            tUserRepository.save(user);
        }
    }

    private void generateWallets() {
        for (int i = 0; i < 3; i++) { // Generate 500 wallets
            Set<TWallet> wallets = initialCoinService.getWallet(i + 1);
            tWalletRepository.saveAll(wallets);
        }
    }

    private void generateExchangeRates() {
        LocalDate fromDate = LocalDate.now().minusYears(2); // Last 2 years
        LocalDate toDate = LocalDate.now();
        Set<TExchangeRate> rates = initialCoinService.getExchangeRates(fromDate, toDate);
        tExchangeRateRepository.saveAll(rates);
    }


}
