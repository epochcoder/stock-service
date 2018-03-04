package com.github.epochcoder.payconiq.stockapp.service;

import com.github.epochcoder.payconiq.stockapp.domain.StockItem;
import com.github.epochcoder.payconiq.stockapp.exception.StockNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * This service is responsible for handling all business logic related to stocks
 *
 * Currently there is not a lot of business logic and this could have been done directly in the controller
 * But this is better for possible future expansion
 */
public interface StockService {

    /**
     * Adds the specified stock item to the application
     * @param item the stock item to add in the application
     * @return the newly created stock item
     */
    StockItem add(StockItem item);

    /**
     * Updates the stock item with the specified id in the application
     * @param id the stock item to update in the application
     * @param item the updated information for the stock item
     * @return the updated stock item
     */
    StockItem update(Long id, StockItem item);

    /**
     * Gets the stock item with the specified id from the application
     * @param id the identifier of the stock to retrieve
     * @return the retrieved stock item
     * @throws StockNotFoundException if the stock could not be found
     */
    StockItem retrieve(Long id) throws StockNotFoundException;

    /**
     * Gets a page of stock items that satisfy the specified requested range
     * @param pageable the pageable range to retrieve
     * @return the retrieved stock items
     */
    Page<StockItem> retrieve(Pageable pageable);

}