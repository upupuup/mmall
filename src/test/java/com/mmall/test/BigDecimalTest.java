package com.mmall.test;

import org.junit.Test;

import java.math.BigDecimal;

public class BigDecimalTest {
    @Test
    public void test1() {
        System.out.println(0.05 + 0.01);
        System.out.println(1.0 - 0.41);
        System.out.println(4.103 * 100);
        System.out.println(5.23 / 100);
    }

    @Test
    public void test2() {
        BigDecimal b1 = new BigDecimal(0.01);
        BigDecimal b2 = new BigDecimal(0.05);
        System.out.println(b1.add(b2));
    }

    @Test
    public void test3() {
        BigDecimal b1 = new BigDecimal("0.01");
        BigDecimal b2 = new BigDecimal("0.05");
        System.out.println(b1.add(b2));
    }
}
