package com.testcoin.demo.model.response;

import java.util.Set;

import com.testcoin.demo.model.TWallet;

public class WalletDetailsResponse {
    private Set<TWallet> wallets;
    private double totalWorth;
    private TWallet lastPurchase;

    public WalletDetailsResponse(Set<TWallet> wallets, double totalWorth, TWallet lastPurchase) {
        this.wallets = wallets;
        this.totalWorth = totalWorth;
        this.lastPurchase = lastPurchase;
    }

    public Set<TWallet> getWallets() {
        return wallets;
    }

    public void setWallets(Set<TWallet> wallets) {
        this.wallets = wallets;
    }

    public double getTotalWorth() {
        return totalWorth;
    }

    public void setTotalWorth(double totalWorth) {
        this.totalWorth = totalWorth;
    }

    public TWallet getLastPurchase() {
        return lastPurchase;
    }

    public void setLastPurchase(TWallet lastPurchase) {
        this.lastPurchase = lastPurchase;
    }
}
