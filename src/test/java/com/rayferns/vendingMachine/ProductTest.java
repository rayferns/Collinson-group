package com.rayferns.vendingMachine;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Testing product prices and names
 * Created by BPerlakiH on 05/05/2017.
 */
public class ProductTest {

    private Product product;

    @Before
    public void setUp() throws Exception {
        this.product = new Product("test name");
    }

    @After
    public void tearDown() throws Exception {
        this.product = null;
    }

    @Test
    public void testSetPriceAndName() {
        Product soda = new Product("soda");
        Assert.assertTrue("should have a positive price by default", 0 < soda.getPrice());
        soda.setPrice(0.1);
        Assert.assertTrue("should be still positive", 0 < soda.getPrice());
        String newName = "sparky mineral water";
        soda.setName(newName);
        Assert.assertEquals("name should update and retain", newName, soda.getName());
    }

    @Test
    public void testInflation() {
        for (double newPrice = 0.01; newPrice < 10; newPrice += 0.01) {
            this.product.setPrice(newPrice);
            Assert.assertEquals(this.product.getPrice(), newPrice, 0);
        }
    }

    @Test(expected = AssertionError.class)
    public void testZeroPriceByConstructor() {
        this.product = new Product("zero", 0);
    }

    @Test(expected = AssertionError.class)
    public void testSettingZeroPrice() {
        this.product.setPrice(0);
    }

    @Test(expected = AssertionError.class)
    public void testNegativePriceByConstructor() {
        this.product = new Product("sub zero", -2);
    }

    @Test(expected = AssertionError.class)
    public void testSettingNegativePrice() {
        this.product.setPrice(-0.01);
    }


}
