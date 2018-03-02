package com.github.epochcoder.payconiq.stockapp;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.epochcoder.payconiq.stockapp.domain.StockItem;
import com.github.epochcoder.payconiq.stockapp.entity.Stock;
import com.github.epochcoder.payconiq.stockapp.repository.StockRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

/**
 * This class is responsible for listening to application startup events.
 *
 * It is used to populate the underlying data source with initial data
 */
@Component
public class ApplicationStartListener {

    private static final Logger LOGGER = LoggerFactory.getLogger(ApplicationStartListener.class);

    private final StockRepository stockRepository;
    private final ResourceLoader resourceLoader;
    private final ObjectMapper objectMapper;

    @Autowired
    public ApplicationStartListener(StockRepository stockRepository, ResourceLoader resourceLoader, ObjectMapper objectMapper) {
        this.stockRepository = stockRepository;
        this.resourceLoader = resourceLoader;
        this.objectMapper = objectMapper;
    }

    @EventListener(ContextRefreshedEvent.class)
    public void contextRefreshedEvent() throws IOException {
        this.populateDataSource();
    }

    private void populateDataSource() throws IOException {
        LOGGER.info("Checking if data source has been populated already...");
        if (this.stockRepository.count() == 0) {
            LOGGER.info("Loading and adding initial data for stock application...");

            final Resource resource = this.resourceLoader.getResource("classpath:/db/init-data.json");
            final List<StockItem> stockItems = this.objectMapper.readValue(
                    resource.getFile(), new TypeReference<List<StockItem>>(){});

            // map and save the stock items to the repository
            this.stockRepository.save(stockItems.stream()
                    .map(Stock::fromStockItem).collect(Collectors.toList()));
        }
    }
}
