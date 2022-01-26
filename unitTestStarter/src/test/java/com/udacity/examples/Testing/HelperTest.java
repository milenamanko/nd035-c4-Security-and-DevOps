package com.udacity.examples.Testing;

import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.List;

public class HelperTest {

    @Test
    public void test() {
        Assert.assertEquals("a", "a");
    }

    @Test
    public void getCountTest() {
        List<String> empNames = Arrays.asList("Carol", "Peter", "Julia");
        Assert.assertEquals(3, Helper.getCount(empNames));
    }

    @Test
    public void getStatsTest() {
        List<Integer> expYears = Arrays.asList(25, 4, 1,9, 16);
        Assert.assertEquals(25, Helper.getStats(expYears).getMax());
    }

    @Test
    public void getStringsOfLength3() {
        List<String> empNames = Arrays.asList("Carol", "Peter", "Joe", "Julia");
        Assert.assertEquals(1, Helper.getStringsOfLength3(empNames));
    }

    @Test
    public void getSquareList() {
        List<Integer> expYears = Arrays.asList(25, 4, 1,9, 16);
        Assert.assertEquals(Arrays.asList(25*25, 4*4, 1*1, 9*9, 16*16), Helper.getSquareList(expYears));
    }

    @Test
    public void getMergedListTest() {
        List<String> empName = Arrays.asList("Carol", "Peter", "Julia");
        Assert.assertEquals("Carol, Peter, Julia", Helper.getMergedList(empName));
    }

    @Test
    public void getFilteredListTest() {
        List<String> empName = Arrays.asList("Carol", "Peter", "Julia", "");
        Assert.assertEquals(Arrays.asList("Carol", "Peter", "Julia"), Helper.getFilteredList(empName));
    }
}
