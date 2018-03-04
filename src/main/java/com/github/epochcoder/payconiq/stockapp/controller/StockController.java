package com.github.epochcoder.payconiq.stockapp.controller;

import com.github.epochcoder.payconiq.stockapp.domain.StockItem;
import com.github.epochcoder.payconiq.stockapp.exception.StockNotFoundException;
import com.github.epochcoder.payconiq.stockapp.service.StockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * The stock controller is the public facing endpoint for the REST based stock service
 */
@RestController
public class StockController {

    private final StockService stockService;

    @Autowired
    public StockController(StockService stockService) {
        this.stockService = stockService;
    }

    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(value = "/api/stocks", method = {RequestMethod.GET}, produces = {"application/json"})
    List<StockItem> getAll(@RequestParam(value = "page", required = false, defaultValue = "0") Integer page,
                           @RequestParam(value = "size", required = false, defaultValue = "50") Integer size) {
        return this.stockService.retrieveAll(new PageRequest(page, size)).getContent();
    }

    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(value = "/api/stocks/{stockId}", method = {RequestMethod.GET}, produces = {"application/json"})
    StockItem getSingle(@PathVariable Long stockId) throws StockNotFoundException {
        return this.stockService.retrieve(stockId);
    }

    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(value = "/api/stocks/{stockId}", method = {RequestMethod.PUT}, produces = {"application/json"})
    StockItem update(@PathVariable Long stockId, @RequestBody StockItem item) throws StockNotFoundException {
        return this.stockService.update(stockId, item);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @RequestMapping(value = "/api/stocks", method = {RequestMethod.POST}, produces = {"application/json"})
    StockItem create(@RequestBody StockItem item) {
        return this.stockService.add(item);
    }
}