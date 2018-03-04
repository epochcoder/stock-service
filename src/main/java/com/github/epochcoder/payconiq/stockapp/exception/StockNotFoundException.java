package com.github.epochcoder.payconiq.stockapp.exception;

/**
 * Exception indicating the stock could not be found in the repository
 */
public class StockNotFoundException extends Exception {

    private Long stockId;

    public StockNotFoundException(Long id) {
        super("Could not find stock with id: " + id);
        this.stockId = id;
    }

    public Long getStockId() {
        return stockId;
    }
}
