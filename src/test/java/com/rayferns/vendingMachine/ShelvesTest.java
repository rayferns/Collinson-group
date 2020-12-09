package com.rayferns.vendingMachine;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;

/**
 * Testing various configuration of product stock and availability on the shelves
 * Created by BPerlakiH on 07/05/2017.
 */
public class ShelvesTest {

    private Shelves shelves;

    @Before
    public void setUp() throws Exception {
        shelves = new Shelves();
    }

    @After
    public void tearDown() throws Exception {
        shelves = null;
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void shouldBeEmptyOnStart() {
        shelves.isEmptyAt(0);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void negativeShelfIndex() {
        shelves.isEmptyAt(-1);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void outOfBoundsShelfIndex() {
        shelves.addShelf("water", 1.22, 1);
        shelves.isEmptyAt(1);
    }

    @Test
    public void emptyAfterUse() throws Exception {
        shelves.addShelf("water", 1.0, 1);
        shelves.addShelf("choco", 1.2, 1);
        Assert.assertFalse(shelves.isEmpty());
        shelves.releaseAProductFrom(0); //release the water
        Assert.assertFalse(shelves.isEmpty());
        shelves.releaseAProductFrom(1); //release the choco
        Assert.assertTrue(shelves.isEmpty());
    }

    @Test
    public void fillUpPeakAndRelease() {
        shelves
            .addShelf("water", 1.22, 10)        //0
            .addShelf("chocolate", 2.33, 10)    //1
            .addShelf("chocolate", 2.33, 10)    //2
            .addShelf("snack bar", 1.99, 2)    //3
            .addShelf("snack bar", 1.99, 0);    //4

        // Test if empty:
        Arrays.stream(new int[]{0, 1, 2, 3}).forEach(
            i -> Assert.assertFalse(shelves.isEmptyAt(i))
        );
        Assert.assertTrue(shelves.isEmptyAt(4));


        // 0) WATER
        testPeakAndRelease(0, "water", 1.22);

        // 1) & 2) CHOCOLATE
        Arrays.stream(new int[]{1, 2}).forEach(i -> testPeakAndRelease(i, "chocolate", 2.33));

        // 3) SNACK BAR
        testPeakAndRelease(3, "snack bar", 1.99);
    }

    private void testPeakAndRelease(int index, String expectedName, double expectedPrice) {
        Assert.assertFalse(shelves.isEmptyAt(index));
        Product productPeaked = shelves.getFirstProductFrom(index);
        Product product = shelves.releaseAProductFrom(index);
        Assert.assertEquals(productPeaked, product);
        Assert.assertEquals("product name should match", product.getName(), expectedName);
        Assert.assertEquals("product price should match", product.getPrice(), expectedPrice, 0);
    }

    @Test
    public void gradualFillUp() {
        shelves.addShelf("water", 1.22, 10);
        testPeakAndRelease(0, "water", 1.22);

        shelves.addShelf("choco locco", 2.33, 1);
        testPeakAndRelease(1, "choco locco", 2.33);
        Assert.assertTrue("shelf should become empty after removing the last product from it", shelves.isEmptyAt(1));
    }
}
