package com.rayferns.vendingMachine;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Aims to calculate if a given list of coins can be divided up to cover a given amount and get the change
 * On success it divides up the coins into change and remainingCoins
 * Created by BPerlakiH on 09/05/2017.
 */
class CoinCounter {

    private final List<Double> coins;
    private final ArrayList<Double> change = new ArrayList<>();
    private final double amount;
    private List<Double> remainingCoins = new ArrayList<>();
    private boolean isChangeAvailable = false;

    CoinCounter(Collection<Double> coinsAvailable, double totalAmount) {
        amount = totalAmount;
        coins = new ArrayList<>(coinsAvailable);
        Collections.sort(coins);
        Collections.reverse(coins);
        calculate();
    }

    boolean isChangeAvailable() {
        return isChangeAvailable;
    }

    ArrayList<Double> getChange() {
        return change;
    }

    List<Double> getRemainingCoins() {
        return remainingCoins;
    }

    private void calculate() {
        remainingCoins = new ArrayList<>();
        int target = (int) Math.round(amount * 100);
        for (Double coin : coins) {
            int coinUnit = (int) Math.round(coin * 100);
            if (coinUnit <= target) {
                change.add(coinUnit / 100.0);
                target -= coinUnit;
            } else {
                remainingCoins.add(coinUnit / 100.0);
            }
        }
        isChangeAvailable = (target == 0);
        if (!isChangeAvailable) {
            remainingCoins = coins;
        }
    }


}
