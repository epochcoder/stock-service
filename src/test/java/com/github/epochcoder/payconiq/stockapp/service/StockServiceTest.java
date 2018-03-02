package com.github.epochcoder.payconiq.stockapp.service;

import com.github.epochcoder.payconiq.stockapp.domain.StockItem;
import com.github.epochcoder.payconiq.stockapp.repository.StockRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.math.BigDecimal;
import java.util.Date;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;

@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)
public class StockServiceTest {

    private static final String STOCK_NAME = "TESTSTK";
    private static final String STOCK_PRICE = "123.321";

    @Autowired
    private StockRepository stockRepository;

    @Autowired
    private StockService stockService;


    @Before
    public void clearDataSource() {
        this.stockRepository.deleteAll();

        assertThat("Repository was not cleared!", this.stockRepository.count(), is(equalTo(0L)));
    }

    @Test
    public void addStock() {
        final StockItem toCreate = createStockItem(STOCK_NAME, STOCK_PRICE);
        final StockItem created = this.stockService.add(toCreate);

        assertThat("Stock was not created", created, is(notNullValue()));
        assertThat("Stock has incorrect name", created.getName(), is(equalTo(toCreate.getName())));
        assertThat("Stock has incorrect price", created, is(equalTo(toCreate.getCurrentPrice())));
        assertThat("Stock should have identifier", created.getId(), is(notNullValue()));
        assertThat("Stock should have last updated date", created.getLastUpdated(), is(notNullValue()));
    }

    // negative cases
    // invalid price, date for update
    // invalid id for retrieval, deletion
    // empty request for creation

    @Test
    public void updateStock() {
        // always override stock item id with path variable id
    }

    @Test
    public void retrieveOneStock() {

    }

    @Test
    public void retrieveRangeOfStocks() {

    }

    @Test
    public void removeOneStock() {

    }

    private static StockItem createStockItem(final String name, final String price) {
        return createStockItem(null, name, price);
    }

    private static StockItem createStockItem(final Long id, final String name, final String price) {
        return StockItem.builder()
                .withId(id)
                .withName(name)
                .withLastUpdated(new Date())
                .withCurrentPrice(new BigDecimal(price))
                .build();
    }
}