package com.rayferns.vendingMachine;

/**
 * Product represents a purchasable good with a larger than zero price and a name
 * Created by BPerlakiH on 05/05/2017.
 */
public class Product {

    private double price = 1.0;
    private String name = "";

    Product(String name) {
        setName(name);
    }

    /**
     * @param name - product name, currently serves as an identifier as well
     * @param price - larger than zero price of the product
     */
    Product(String name, double price) {
        setName(name);
        setPrice(price);
    }

    String getName() {
        return name;
    }

    void setName(String name) {
        this.name = name;
    }

    double getPrice() {
        return price;
    }

    void setPrice(double price) {
        assert (0 < price);
        this.price = price;
    }


}
