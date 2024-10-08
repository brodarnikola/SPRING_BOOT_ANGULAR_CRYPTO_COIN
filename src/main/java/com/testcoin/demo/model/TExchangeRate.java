package com.testcoin.demo.model;

import lombok.Data;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.time.LocalDate;

@Data
@Entity
public class TExchangeRate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private LocalDate excRateDate;
    private double excRateEur;
    private double excRateUsd;
    private double excRateGbp;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public LocalDate getExcRateDate() {
        return excRateDate;
    }

    public void setExcRateDate(LocalDate excRateDate) {
        this.excRateDate = excRateDate;
    }

    public double getExcRateEur() {
        return excRateEur;
    }

    public void setExcRateEur(double excRateEur) {
        this.excRateEur = excRateEur;
    }

    public double getExcRateUsd() {
        return excRateUsd;
    }

    public void setExcRateUsd(double excRateUsd) {
        this.excRateUsd = excRateUsd;
    }

    public double getExcRateGbp() {
        return excRateGbp;
    }

    public void setExcRateGbp(double excRateGbp) {
        this.excRateGbp = excRateGbp;
    }
}