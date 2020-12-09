package com.rayferns.vendingMachine.display;

/**
 * The display of the vending machine, supporting price and error messages
 * Created by BPerlakiH on 06/05/2017.
 */
public class Display implements IDisplay {

    private String message = "";

    /**
     * The default method to read the current message from the display
     *
     * @return the default message
     */
    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public void showPrice(double price) {
        showMessage(String.format("%.2f", price));
    }

    @Override
    public void showMessage(String msg) {
        message = msg;
    }

    @Override
    public void clearScreen() {
        showMessage("");
    }
}
