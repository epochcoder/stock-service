package com.github.epochcoder.payconiq.stockapp.controller;

import com.github.epochcoder.payconiq.stockapp.service.StockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class StockController {

    private final StockService stockService;


    @Autowired
    public StockController(StockService stockService) {
        this.stockService = stockService;
    }


}
