package com.rayferns.vendingMachine;

import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Stream;

/**
 * Testing various payment scenarios to verify change and revenue growth during purchase
 * Created by BPerlakiH on 09/05/2017.
 */
public class CoinCounterTest {

    private static final Collection<Double> DENOMINATIONS = Arrays.asList(0.1, 0.2, 0.5, 1.0, 2.0, 5.0);

    @Test
    public void available() throws Exception {
        Stream.of(0.1, 0.2, 0.3, 0.5, 0.7, 0.8, 1.0, 1.1, 1.2, 1.3, 1.5, 2.0, 5.0).forEach(d -> {
            CoinCounter counter = new CoinCounter(DENOMINATIONS, d);
            Assert.assertTrue(counter.isChangeAvailable());
        });

        Stream.of(0.0, 1.0, 2.0, 3.0, 4.0, 5.0).forEach(d -> {
            CoinCounter counter = new CoinCounter(Arrays.asList(1.0, 1.0, 1.0, 2.0), d);
            Assert.assertTrue(counter.isChangeAvailable());
        });
    }

    @Test
    public void notAvailable() throws Exception {
        Stream.of(0.01, 1.15, 8.90, 10.0).forEach(d -> {
            CoinCounter counter = new CoinCounter(DENOMINATIONS, d);
            Assert.assertFalse(counter.isChangeAvailable());
        });
    }
}
