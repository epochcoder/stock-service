package com.github.epochcoder.payconiq.stockapp.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Exception indicating the stock could not be found in the repository
 */
@ResponseStatus(HttpStatus.NOT_FOUND)
public class StockNotFoundException extends Exception {

    private Long stockId;

    public StockNotFoundException(Long id) {
        super("Could not find stock with id: " + id);
        this.stockId = id;
    }

    public Long getStockId() {
        return this.stockId;
    }
}
