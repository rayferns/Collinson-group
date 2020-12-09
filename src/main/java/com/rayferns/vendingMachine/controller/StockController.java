package com.rayferns.vendingMachine.controller;

import com.rayferns.vendingMachine.ITransactionDelegate;
import com.rayferns.vendingMachine.Product;
import com.rayferns.vendingMachine.Shelves;
import com.rayferns.vendingMachine.Transaction;
import com.rayferns.vendingMachine.display.IDisplay;
import com.rayferns.vendingMachine.display.Message;

/**
 * Handles the most common case, when there's a selection and there's at least 1 product in stock
 * Created by BPerlakiH on 12/05/2017.
 */
public class StockController extends AbstractController {

    private final Shelves shelves;
    private Transaction transaction;
    private int selectedShelf;

    public StockController(IDisplay display, IOutputDelegate delegate, Shelves shelves) {
        super(display, delegate);
        this.shelves = shelves;
    }

    @Override
    public void cancel() {
        transaction.cancel();
        display.showMessage(Message.getCancel());
        dispatchAllChange();
    }

    @Override
    public void selectShelf(int index) {
        selectedShelf = index;
        if (shelves.isEmptyAt(selectedShelf)) {
            display.showMessage(Message.getOutOfStock());
        } else {
            Double productPrice = shelves.getPriceAt(index);
            if (transaction == null) {
                ITransactionDelegate transactionDelegate = getTransactionDelegate();
                transaction = new Transaction(productPrice, transactionDelegate);
            } else {
                transaction.setProductPrice(productPrice);
            }
            display.showPrice(productPrice);
        }
    }

    @Override
    public void insertCoin(Double coin) {
        transaction.addCoin(coin);
        dispatchAllChange();
    }

    private void dispatchAllChange() {
        transaction.getChange().forEach(delegate::onChange);
    }

    private ITransactionDelegate getTransactionDelegate() {
        return new ITransactionDelegate() {

            @Override
            public void onContinue() {
                display.showPrice(transaction.getDuePrice());
            }

            @Override
            public void onComplete() {
                Product product = shelves.releaseAProductFrom(selectedShelf);
                delegate.onProduct(product);
                display.showMessage(Message.getThankYou());
            }

            @Override
            public void onOutOfChange() {
                display.showMessage(Message.getOutOfChange());
            }

        };
    }

}
