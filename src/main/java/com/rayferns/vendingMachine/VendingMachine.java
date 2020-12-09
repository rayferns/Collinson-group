package com.rayferns.vendingMachine;

import com.rayferns.vendingMachine.controller.*;
import com.rayferns.vendingMachine.display.IDisplay;
import com.rayferns.vendingMachine.display.Message;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Vending machine implementation
 * Created by BPerlakiH on 19/05/2017.
 */
class VendingMachine implements IInput {

    private final Shelves shelves;
    private final IDisplay display;
    private final ArrayList<Double> change;
    private final IInput noStockController;
    private final IInput noSelectionController;
    private final IOutputDelegate delegate;
    private IInput stockController;
    private boolean isProductSelected = false;
    private Product productReleased;

    public VendingMachine(IDisplay display, Shelves shelves) {
        this.display = display;
        this.shelves = shelves;
        change = new ArrayList<>();
        delegate = buildDelegate();
        noStockController = new NoStockController(display, delegate);
        noSelectionController = new NoProductSelectionController(display, delegate);
    }


    /**
     * Grab your change here, it can only be done once
     * any further call will return an empty collection
     *
     * @return - the due change or invalid coins if any
     */
    Collection<Double> getChange() {
        Collection<Double> returnChanges = new ArrayList<>(change);
        change.clear();
        return returnChanges;
    }

    /**
     * Grab your purchased product here, it can be done only once
     * further attempts will result in null
     *
     * @return - the purchased product if any
     */
    public Product getProduct() {
        if (productReleased == null) {
            return null;
        }
        Product prod = productReleased;
        productReleased = null;
        return prod;
    }


    //INPUT
    @Override
    public void cancel() {
        getController().cancel(); //!important order
        isProductSelected = false;
    }

    @Override
    public void selectShelf(int index) {
        isProductSelected = true;
        if (stockController == null) {
            stockController = new StockController(display, delegate, shelves);
        }
        getController().selectShelf(index);
    }

    @Override
    public void insertCoin(Double coin) {
        getController().insertCoin(coin);
    }


    //PRIVATE

    /**
     * Switch between various controllers depending on stock and product selection
     *
     * @return - one of the controller types handling the input
     */
    private IInput getController() {
        if (this.shelves.isEmpty()) {
            return noStockController;
        } else if (!isProductSelected) {
            return noSelectionController;
        } else {
            return stockController;
        }
    }

    /**
     * Get a delegate that we can use 'in-line'
     *
     * @return IOutputDelegate - delegate solution to handle change and product dispatches
     */
    private IOutputDelegate buildDelegate() {
        return new IOutputDelegate() {
            @Override
            public void onChange(Double coin) {
                change.add(coin);
            }

            @Override
            public void onProduct(Product product) {
                isProductSelected = false;
                productReleased = product;
                display.showMessage(Message.getThankYou());
            }
        };
    }


}
