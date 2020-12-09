package com.rayferns.vendingMachine.display;

/**
 * Interface representing a general display approach
 * Created by BPerlakiH on 14/05/2017.
 */
public interface IDisplay {

    String getMessage();

    void showPrice(double price);

    void clearScreen();

    void showMessage(String msg);

}
