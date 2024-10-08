package com.testcoin.demo.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Entity
public class TWallet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private int idUser;
    private String coinToken = UUID.randomUUID().toString();
    private LocalDateTime countTimeStamp;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdUser() {
        return idUser;
    }

    public void setIdUser(int idUser) {
        this.idUser = idUser;
    }

    public String getCoinToken() {
        return coinToken;
    }

    public void setCoinToken(String coinToken) {
        this.coinToken = coinToken;
    }

    public LocalDateTime getCountTimeStamp() {
        return countTimeStamp;
    }

    public void setCountTimeStamp(LocalDateTime countTimeStamp) {
        this.countTimeStamp = countTimeStamp;
    }
}