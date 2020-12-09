package com.rayferns.vendingMachine.controller;

import com.rayferns.vendingMachine.display.IDisplay;
import com.rayferns.vendingMachine.display.Message;

/**
 * A controller for the case when no product has been selected yet
 * Created by BPerlakiH on 15/05/2017.
 */
public class NoProductSelectionController extends AbstractController {

    public NoProductSelectionController(IDisplay display, IOutputDelegate delegate) {
        super(display, delegate);
    }

    @Override
    public void cancel() {
        display.clearScreen();
    }

    @Override
    public void selectShelf(int index) {
        // this should be not be reached at all
    }

    @Override
    public void insertCoin(Double coin) {
        delegate.onChange(coin);
        display.showMessage(Message.getSelectionRequired());
    }

}
