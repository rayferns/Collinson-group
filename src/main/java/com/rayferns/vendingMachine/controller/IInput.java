package com.rayferns.vendingMachine.controller;

/**
 * An input controller interface
 * Created by BPerlakiH on 18/05/2017.
 */
public interface IInput {

    void cancel();

    void selectShelf(int index);

    void insertCoin(Double coin);

}
