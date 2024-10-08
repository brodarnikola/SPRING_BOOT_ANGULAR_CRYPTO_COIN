package com.testcoin.demo.repository;

import com.testcoin.demo.model.TUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TUserRepository extends JpaRepository<TUser, Integer> {
}
