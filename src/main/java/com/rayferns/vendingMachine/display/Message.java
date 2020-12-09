package com.rayferns.vendingMachine.display;

/**
 * A collection of constant messages
 * Created by BPerlakiH on 19/05/2017.
 */
public class Message {

    private static final String OUT_OF_STOCK = "Out of stock";
    private static final String OUT_OF_CHANGE = "Out of change";
    private static final String SELECTION_REQUIRED = "Please select a product first";
    private static final String THANK_YOU = "Thank you";
    private static final String CANCEL = "Canceling...";

    public static String getOutOfStock() {
        return OUT_OF_STOCK;
    }

    public static String getOutOfChange() {
        return OUT_OF_CHANGE;
    }

    public static String getSelectionRequired() {
        return SELECTION_REQUIRED;
    }

    public static String getThankYou() {
        return THANK_YOU;
    }

    public static String getCancel() {
        return CANCEL;
    }
}
