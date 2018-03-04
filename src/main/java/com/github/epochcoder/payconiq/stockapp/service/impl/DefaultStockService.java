package com.github.epochcoder.payconiq.stockapp.service.impl;

import com.github.epochcoder.payconiq.stockapp.domain.StockItem;
import com.github.epochcoder.payconiq.stockapp.entity.Stock;
import com.github.epochcoder.payconiq.stockapp.exception.StockNotFoundException;
import com.github.epochcoder.payconiq.stockapp.repository.StockRepository;
import com.github.epochcoder.payconiq.stockapp.service.StockService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * The default implementation of the stock service
 * <p>
 * Currently only acts as a bridge between the controller and repository,
 * but may expand once more business logic is required
 */
@Service
public class DefaultStockService implements StockService {

    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultStockService.class);

    private final StockRepository stockRepository;

    @Autowired
    public DefaultStockService(StockRepository stockRepository) {
        this.stockRepository = stockRepository;
    }

    @Override
    public StockItem add(StockItem item) {
        LOGGER.info("Adding stock[{}] to repository", item);
        return this.persist(null, item);
    }

    @Override
    public StockItem update(Long id, StockItem item) throws StockNotFoundException {
        LOGGER.info("Updating stock[{}] in repository", item);
        if (id == null) {
            throw new StockNotFoundException(id);
        }

        return this.persist(this.retrieve(id).getId(), item);
    }

    @Override
    public StockItem retrieve(Long id) throws StockNotFoundException {
        LOGGER.info("Retrieving stock[{}] from repository", id);

        final Stock retrieved = this.stockRepository.findOne(id);
        return Optional.ofNullable(retrieved)
                .map(s -> StockItem.builder().withEntity(retrieved).build())
                .orElseThrow(() -> new StockNotFoundException(id));
    }

    @Override
    public Page<StockItem> retrieveAll(Pageable pageable) {
        LOGGER.info("Retrieving stocks with query[{}] from repository", pageable);
        final Page<Stock> retrievedList = this.stockRepository.findAll(pageable);

        return retrievedList.map(source -> StockItem.builder().withEntity(source).build());
    }

    private StockItem persist(Long id, StockItem item) {
        final Stock stock = Stock.fromStockItem(item);

        // ensure no id comes from path variable
        stock.setId(id);

        final Stock updated = this.stockRepository.save(stock);
        return StockItem.builder().withEntity(updated).build();
    }
}