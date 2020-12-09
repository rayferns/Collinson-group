package com.rayferns.vendingMachine.controller;

import com.rayferns.vendingMachine.display.IDisplay;
import com.rayferns.vendingMachine.display.Message;

/**
 * Handles the situation, when there's no stock at all
 * Created by BPerlakiH on 12/05/2017.
 */
public class NoStockController extends AbstractController {

    public NoStockController(IDisplay display, IOutputDelegate delegate) {
        super(display, delegate);
    }

    @Override
    public void cancel() {
        showOutOfStock();
    }

    @Override
    public void selectShelf(int index) {
        showOutOfStock();
    }

    @Override
    public void insertCoin(Double coin) {
        showOutOfStock();
        delegate.onChange(coin); //drop it out straight away
    }

    private void showOutOfStock() {
        display.showMessage(Message.getOutOfStock());
    }

}
