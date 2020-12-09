package com.rayferns.vendingMachine;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

/**
 * Calculates product purchases including the change that should be given back
 * Assuming that all change is picked up, before new coins are inserted
 * (it's a separation of concern, we calculate things up until that point and no further)
 * Created by BPerlakiH on 08/05/2017.
 */
public class Transaction {

    static final Collection<Double> DEFAULT_DENOMINATIONS = Arrays.asList(0.1, 0.2, 0.5, 1.0, 2.0, 5.0);
    /**
     * A cash register that contains the coins we already have before the transaction
     */
    final ArrayList<Double> till = new ArrayList<>(DEFAULT_DENOMINATIONS); //start with a non-empty till
    private final ArrayList<Double> change;
    private final ArrayList<Double> coins;
    private final Collection<Double> denominations;
    private final ITransactionDelegate delegate;
    private double price;
    private double duePrice;

    /**
     * @param productPrice - The total price of the transaction / purchase
     */
    public Transaction(double productPrice, ITransactionDelegate delegate) {
        this.delegate = delegate;
        denominations = DEFAULT_DENOMINATIONS;
        price = productPrice;
        duePrice = price;
        change = new ArrayList<>();
        coins = new ArrayList<>();
    }

    public void setProductPrice(double productPrice) {
        price = productPrice;
        processCoins();
    }

    /**
     * Get the remaining amount to be paid
     */
    public double getDuePrice() {
        return duePrice;
    }

    /**
     * @return the total price of the product
     */
    double getTotalPrice() {
        return price;
    }

    /**
     * Cancel the transaction, return the change if any
     */
    public void cancel() {
        change.addAll(coins);
        coins.clear();
    }

    /**
     * Assuming that the change is "picked up" right away before new coins are inserted
     *
     * @return the change in coins
     */
    public Collection<Double> getChange() {
        Collection<Double> returnChanges = new ArrayList<>(change);
        change.clear();
        return returnChanges;
    }

    /**
     * Add new coins to the transaction
     * Invalid coins fall straight out as change
     * Assumes that the change is "picked up" before new coins are inserted
     *
     * @param value - the face value of the coin
     */
    public void addCoin(double value) {
        change.clear();
        if (isValidCoin(value)) {
            coins.add(value);
            processCoins();
        } else {
            //return the coin, it should fly through without stopping
            change.add(value);
        }
    }

    private void processCoins() {
        Double coinsTotal = getCoinsTotal();
        if (coinsTotal < price) {
            duePrice = price - coinsTotal;
            //the transaction is still ongoing, keep collecting coins, do nothing else
            delegate.onContinue();
            return;
        }
        if (coinsTotal == price) {
            //we got the exact amount, no change is necessary
            duePrice = 0;
            till.addAll(coins);
            coins.clear();
            change.clear();
            delegate.onComplete();
            return;
        }
        //we have overpayment, change is due:
        duePrice = 0;

        //check if we can give the rest from the all coins available:
        ArrayList<Double> allCoins = new ArrayList<>(coins);
        allCoins.addAll(till);
        CoinCounter counter = new CoinCounter(allCoins, coinsTotal - price);
        if (counter.isChangeAvailable()) {
            coins.clear();
            change.clear();
            change.addAll(counter.getChange());
            till.clear();
            till.addAll(counter.getRemainingCoins());
            delegate.onComplete();
        } else {
            //there's no way we can give back the change, roll back the transaction:
            cancel();
            delegate.onOutOfChange();
        }
    }

    /**
     * @return - the total value of coins inserted
     */
    private Double getCoinsTotal() {
        return coins.stream().reduce(0.0, Double::sum);
    }

    private boolean isValidCoin(double value) {
        return denominations.contains(value);
    }


}
