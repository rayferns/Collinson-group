package com.rayferns.vendingMachine;

import com.rayferns.vendingMachine.display.Display;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.stream.Stream;

public class VendingMachineTest {

    private Shelves shelves;
    private Display display;
    private VendingMachine machine;

    @Before
    public void setUp() throws Exception {
        shelves = new Shelves();
        shelves.addShelf("test water", 1.90, 1);
        shelves.addShelf("empty bottle", 0.90, 0);
        display = new Display();
        machine = new VendingMachine(this.display, shelves);
    }

    @After
    public void tearDown() throws Exception {
        shelves = null;
        display = null;
        machine = null;
    }

    @Test
    public void noStock() throws Exception {
        shelves.releaseAProductFrom(0); //empty out the shelf first
        machine.selectShelf(0);
        Stream.of(0, 1).forEach(index -> {
            machine.selectShelf(index);
            assertShowsOutOfStock();
        });
    }

    @Test
    public void withStock() throws Exception {
        machine.selectShelf(0);
        assertShowsDueAmount(1.90);
        machine.selectShelf(1);
        assertShowsOutOfStock();
    }

    @Test
    public void withStockNothingSelected() throws Exception {
        machine.insertCoin(2.00);
        assertShouldSelectDisplayed();
        assertTotalChange(2.00);
    }

    @Test
    public void cancel() throws Exception {
        //start transaction
        machine.selectShelf(0);
        machine.insertCoin(1.00);
        assertShowsDueAmount(0.90);
        machine.insertCoin(0.20);
        assertShowsDueAmount(0.70);

        //cancel trans.
        machine.cancel();
        assertShowsCancel();
        assertTotalChange(1.20);
    }

    @Test
    public void complete() throws Exception {
        machine.selectShelf(0);
        assertShowsDueAmount(1.90);
        machine.insertCoin(0.50);
        assertShowsDueAmount(1.40);
        machine.insertCoin(0.50);
        assertShowsDueAmount(0.90);
        machine.insertCoin(1.00);
        assertTotalChange(0.10);
        assertShowsThankYou();
        assertTotalChange(0.00); //make sure change can be collected only once
        Product prod = machine.getProduct();
        Assert.assertEquals("product price should match", 1.90, prod.getPrice(), 0.01);
        Assert.assertEquals("product name should match", "test water", prod.getName());
        prod = machine.getProduct();
        Assert.assertNull("product should be accessible only once", prod);

        machine.selectShelf(0);
        assertShowsOutOfStock();
    }

    /**
     * Make sure we have only 1 chance to collect our change
     * eg. we can't get our change twice or more
     */
    @Test
    public void outOfChange() throws Exception {
        shelves.addShelf("fizzy bubble", 1.90, 10);
        //do a normal purchase
        machine.selectShelf(2);
        assertShowsDueAmount(1.9);
        machine.insertCoin(2.00);
        Assert.assertEquals("we should get the product we paid for", machine.getProduct().getName(), "fizzy bubble");
        assertTotalChange(0.1);

        //now it should run out of change (0.1):
        machine.selectShelf(2);
        assertShowsDueAmount(1.9);
        machine.insertCoin(2.00);
        assertShowsOutOfChange();
        assertTotalChange(2.00);

        //although it should be able to give an alternative change (0.5)
        machine.selectShelf(2);
        assertShowsDueAmount(1.9);
        machine.insertCoin(0.2);
        machine.insertCoin(0.2);
        assertShowsDueAmount(1.5);
        machine.insertCoin(2.0);
        assertTotalChange(0.5);
        assertShowsThankYou();

        //and it should be able to give change from the user's own coins only as well


    }

    /**
     * Test if the machine can give the change from the user's coin only
     */
    @Test
    public void changeFromInput() throws Exception {
        shelves.addShelf("fizzy bubble", 1.90, 10);
        //do a normal purchase
        machine.selectShelf(2);
        assertShowsDueAmount(1.9);
        machine.insertCoin(2.00);
        assertTotalChange(0.1);


    }

    private void assertShouldSelectDisplayed() {
        Assert.assertTrue("should display nothing selected", display.getMessage().toLowerCase().contains("please select"));
    }

    private void assertTotalChange(Double expectedTotal) {
        Double totalChange = machine.getChange().stream().reduce(0.00, Double::sum);
        Assert.assertEquals("we should get our change back", expectedTotal, totalChange);
    }

    private void assertShowsDueAmount(Double expectedAmount) {
        Assert.assertEquals("it should display the due amount", String.format("%.2f", expectedAmount), this.display.getMessage());
    }

    private void assertShowsOutOfStock() {
        Assert.assertTrue("should be out of stock", display.getMessage().toLowerCase().contains("out of stock"));
    }

    private void assertShowsThankYou() {
        Assert.assertEquals("should display thank you", "Thank you", display.getMessage());
    }

    private void assertShowsCancel() {
        Assert.assertEquals("should display cancel", "Canceling...", display.getMessage());
    }

    private void assertShowsOutOfChange() {
        Assert.assertEquals("should display out of change", "Out of change", display.getMessage());
    }

}
