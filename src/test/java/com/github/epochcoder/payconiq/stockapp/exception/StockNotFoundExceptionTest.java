package com.github.epochcoder.payconiq.stockapp.exception;

import org.junit.Test;

import static org.junit.Assert.*;

public class StockNotFoundExceptionTest {

    @Test
    public void hasCorrectMessage() {
        assertEquals("Could not find stock with id: 22", new StockNotFoundException(22L).getMessage());
    }

    @Test
    public void hasCorrectId() {
        assertEquals(21L, (long) new StockNotFoundException(21L).getStockId());
    }

}