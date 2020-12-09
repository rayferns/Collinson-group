package com.rayferns.vendingMachine;

import java.util.AbstractCollection;
import java.util.LinkedList;

/**
 * A collection of shelves boxed into this class
 * Each shelf can contain only one type of product!
 * Created by BPerlakiH on 07/05/2017.
 */
public class Shelves {

    private final LinkedList<LinkedList<Product>> shelves = new LinkedList<>();

    public Shelves addShelf(String productName, double productPrice, int quantity) {
        LinkedList<Product> newShelf = new LinkedList<>();
        for (int i = 0; i < quantity; i++) {
            newShelf.add(new Product(productName, productPrice));
        }
        shelves.add(newShelf);
        return this;
    }

    /**
     * @return - checks if the shelves are completely empty
     */
    public Boolean isEmpty() {
        return shelves.isEmpty() || shelves.stream().allMatch(AbstractCollection::isEmpty);
    }

    public Boolean isEmptyAt(int index) throws IndexOutOfBoundsException {
        return shelves.get(index).isEmpty();
    }

    /**
     * Permanently removes a product from the given shelf, and returns it (or null, if shelf is empty)
     *
     * @return the top product or null, if shelf is empty
     */
    public Product releaseAProductFrom(int index) throws IndexOutOfBoundsException {
        if (isEmptyAt(index)) {
            return null;
        }
        return shelves.get(index).remove(0);
    }

    public double getPriceAt(int index) throws IndexOutOfBoundsException {
        Product firstProduct = getFirstProductFrom(index);
        if (firstProduct != null) {
            return firstProduct.getPrice();
        }
        throw new IndexOutOfBoundsException();
    }

    /**
     * Returns (without removing) the top product from the given shelf (or null, if shelf is empty)
     *
     * @return the top product or null, if shelf is empty
     **/
    Product getFirstProductFrom(int index) throws IndexOutOfBoundsException {
        if (isEmptyAt(index)) {
            return null;
        }
        return shelves.get(index).get(0);
    }

}
