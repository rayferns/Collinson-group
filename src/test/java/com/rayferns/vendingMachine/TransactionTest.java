package com.rayferns.vendingMachine;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Stream;

/**
 * Testing various transaction scenarios that could occur during product purchase
 * Created by BPerlakiH on 08/05/2017.
 */
public class TransactionTest {

    private final double productPrice = 19.90;
    private Transaction trans;
    private TransactionDelegateTester delegate;

    @Before
    public void setUp() throws Exception {
        delegate = new TransactionDelegateTester();
        trans = new Transaction(productPrice, delegate);
        trans.till.addAll(Transaction.DEFAULT_DENOMINATIONS); ///add each possible coin twice
    }

    @After
    public void tearDown() throws Exception {
        trans = null;
    }

    @Test
    public void initialState() throws Exception {
        assertDuePrice(productPrice);
        Assert.assertTrue("there should be no change at start", trans.getChange().isEmpty());
        assertOngoing();
    }

    @Test
    public void invalidCoins() throws Exception {
        trans.addCoin(0.3);
        assertOnlyOneChange(0.3);
        assertDuePrice(productPrice);
        assertOngoing();

        trans.addCoin(0.44);
        assertOnlyOneChange(0.44);
        assertDuePrice(productPrice);

        //add a valid one and and invalid one
        trans.addCoin(0.1);
        trans.addCoin(0.55); //invalid
        assertOnlyOneChange(0.55);
        assertDuePrice(productPrice - 0.1);
        assertOngoing();
    }

    @Test
    public void validCoins() throws Exception {
        //add all possible valid coins:
        Transaction.DEFAULT_DENOMINATIONS.forEach(d -> trans.addCoin(d));
        assertNoChange();
    }

    @Test
    public void exactPrice() throws Exception {
        double totalInTill = getTotalInTill();
        addCoins(5.0, 3);
        assertDuePrice(productPrice - 15.0);
        addCoins(2.0, 2);
        assertDuePrice(productPrice - 19.0);
        Stream.of(0.5, 0.2, 0.1, 0.1).forEach(trans::addCoin);
        assertDuePrice(0.0);
        assertComplete();
        assertNoChange();
        assertTotalInTill(totalInTill + 19.9);
    }

    @Test
    public void overPayments() throws Exception {
        double totalInTill = getTotalInTill();
        addCoins(5.0, 4);
        assertDuePrice(0.0);
        assertTotalChangeEquals(0.1);
        assertTotalInTill(totalInTill + 19.9);
        assertComplete();
    }

    @Test
    public void cancel() throws Exception {
        trans.cancel();
        assertNoChange();
        assertOngoing();
        Stream.of(0.1, 0.2, 0.3, 0.5).forEach(trans::addCoin); //0.3 is invalid
        trans.cancel();
        assertTotalChangeEquals(0.1 + 0.2 + 0.5);
    }

    @Test
    public void validateChangeAmount() throws Exception {
        Stream.of(5.0, 5.0, 5.0, 5.0).forEach(trans::addCoin);
        assertComplete();
        Assert.assertEquals("the change + price of product should equal the amount paid", 20.0, trans.getTotalPrice() + getTotalChange(), 0.01);
    }

    @Test
    public void outOfChange() throws Exception {
        Collection<Double> smallTill = Arrays.asList(0.2, 0.5, 1.0, 5.0, 5.0);
        double initialTillTotal = smallTill.stream().reduce(0.0, Double::sum);
        trans = new Transaction(0.9, delegate);
        trans.till.clear();
        trans.till.addAll(smallTill);

        trans.addCoin(2.0);
        //we should get back 1.10 but that's not really possible from these coins
        //therefore we should get back our 2.0 coin
        assertOnlyOneChange(2.0);
        assertOutOfChange();

        assertTotalInTill(initialTillTotal);

        //it should be able to give another change for an alternative payment though
        trans.setProductPrice(1.9);
        trans.addCoin(0.2);
        trans.addCoin(0.2);
        trans.addCoin(2.0);
        //we paid 2.4 and expecting to get back 0.5
        assertOnlyOneChange(0.5);

        //now we should run out of 0.5s as well
        trans.setProductPrice(1.9);
        trans.addCoin(0.2);
        trans.addCoin(0.2);
        trans.addCoin(2.0);
        assertOutOfChange();
        assertTotalChangeEquals(2.4);
    }

    @Test
    public void alternativeChange() throws Exception {
        Collection<Double> usedTill = Arrays.asList(0.1, 0.2, 0.5, 1.0, 2.0, 5.0);
        trans = new Transaction(1.9, delegate);
        trans.till.clear();
        trans.till.addAll(usedTill);

        trans.addCoin(0.2);
        trans.addCoin(0.2);
        trans.addCoin(2.0);
        //we paid 2.40 we should get back 0.5:
        assertOnlyOneChange(0.5);
        assertComplete();
    }

    /**
     * The user changes his mind and picks another product, which is cheaper
     */
    @Test
    public void updatePrice() throws Exception {
        trans.addCoin(2.0);
        trans.addCoin(1.0);
        trans.addCoin(5.0);
        //the user changes his mind, and picks a cheaper product
        trans.setProductPrice(2.0);

        assertDuePrice(0.0);
        assertTotalChangeEquals(6.0);
        assertComplete();
    }

    private void addCoins(double value, int times) {
        for (int i = 0; i < times; i++) {
            trans.addCoin(value);
        }
    }

    private void assertNoChange() {
        Assert.assertEquals("there should be no change given", 0, trans.getChange().size());
    }

    private double getTotalInTill() {
        return trans.till.stream().reduce(0.0, Double::sum);
    }

    private double getTotalChange() {
        return trans.getChange().stream().reduce(0.0, Double::sum);
    }

    private void assertTotalChangeEquals(double expectedValue) {
        Assert.assertEquals("the amount of change should match", expectedValue, getTotalChange(), 0.01);
    }

    private void assertOnlyOneChange(double value) {
        Collection<Double> change = trans.getChange();
        Assert.assertEquals("should give back only 1 coin", 1, change.size());
        Assert.assertTrue(String.format("it should be the one we inserted (%.2f)", value), change.contains(value));
    }

    private void assertDuePrice(double expectedValue) {
        Assert.assertEquals("due price should be updated", expectedValue, trans.getDuePrice(), 0.01);
    }

    private void assertOutOfChange() {
        Assert.assertFalse("the transaction should roll back", delegate.isComplete());
        Assert.assertTrue("the transaction should be in the out of change state", delegate.isOutOfChange());
    }

    private void assertOngoing() {
        Assert.assertTrue("the transaction should be ongoing", delegate.isOngoing());
    }

    private void assertComplete() {
        Assert.assertTrue("the transaction should be complete", delegate.isComplete());
    }

    private void assertTotalInTill(Double expected) {
        Assert.assertEquals("we should see cash flowing into the till after a purchase", expected, getTotalInTill(), 0.01);
    }


}
