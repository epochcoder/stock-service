package com.github.epochcoder.payconiq.stockapp.service.impl;

import com.github.epochcoder.payconiq.stockapp.domain.StockItem;
import com.github.epochcoder.payconiq.stockapp.service.StockService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DefaultStockService implements StockService {

    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultStockService.class);

    @Override
    public StockItem add(StockItem item) {
        return null;
    }

    @Override
    public StockItem update(long id, StockItem item) {
        return null;
    }

    @Override
    public StockItem retrieve(long id) {
        return null;
    }

    @Override
    public List<StockItem> retrieve(Pageable pageable) {
        return null;
    }

    @Override
    public boolean remove(long id) {
        return false;
    }

}
