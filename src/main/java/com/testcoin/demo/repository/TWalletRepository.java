package com.testcoin.demo.repository;

import com.testcoin.demo.model.TWallet;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TWalletRepository extends JpaRepository<TWallet, Integer> {
    List<TWallet> findByIdUser(int idUser);  // Fetch wallets by user ID
}
