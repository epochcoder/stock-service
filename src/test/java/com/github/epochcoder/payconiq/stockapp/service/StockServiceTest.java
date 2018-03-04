package com.github.epochcoder.payconiq.stockapp.service;

import com.github.epochcoder.payconiq.stockapp.domain.StockItem;
import com.github.epochcoder.payconiq.stockapp.entity.Stock;
import com.github.epochcoder.payconiq.stockapp.exception.StockNotFoundException;
import com.github.epochcoder.payconiq.stockapp.repository.StockRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)
public class StockServiceTest {

    private static final String STOCK_NAME = "TESTSTK";
    private static final String STOCK_PRICE = "123.321";

    @Autowired
    private StockRepository stockRepository;

    @Autowired
    private StockService stockService;

    private StockItem toCreate;
    private StockItem created;

    @Before
    public void prepareDataSource() {
        this.stockRepository.deleteAll();

        assertThat("Repository was not cleared!", this.stockRepository.count(), is(equalTo(0L)));

        this.toCreate = createStockItem(STOCK_NAME, STOCK_PRICE);
        this.created = this.stockService.add(toCreate);

        assertEqualTo(this.toCreate, this.created);
        assertTrue("Cannot find stock we just created", this.stockRepository.exists(this.created.getId()));
    }

    @Test
    public void updateStock() {
        final StockItem toUpdate = createStockItem(this.created.getId(), "NWSTK", "123.123");
        final StockItem updated = this.stockService.update(toUpdate.getId(), toUpdate);

        assertEqualTo(toUpdate, updated);
        assertThat("Stock repo should still have only one entry",
                this.stockRepository.count(), is(equalTo(1L)));
    }

    @Test(expected = IllegalArgumentException.class)
    public void createEmptyStock() {
        this.stockService.add(StockItem.builder().build());
    }

    @Test(expected = StockNotFoundException.class)
    public void getInvalidStock() throws StockNotFoundException {
        this.stockService.retrieve(-1L);
    }

    @Test
    public void retrieveOneStock() throws StockNotFoundException {
        StockItem item = this.stockService.retrieve(this.created.getId());
        assertEqualTo(this.toCreate, item);
    }

    @Test
    public void retrieveRangeOfStocks() {
        this.stockRepository.deleteAll();

        final int amount = 100;
        final List<StockItem> toVerify = new ArrayList<>(amount);

        for (int i = 1; i <=  amount; i++) {
            toVerify.add(this.stockService.add(createStockItem("TST_" + i, String.valueOf(i))));
        }

        int i = 0;
        final Page<StockItem> page = this.stockService.retrieve(new PageRequest(0, amount));
        for (StockItem stockItem : page) {
            assertEqualTo(toVerify.get(i++), stockItem);
        }
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

    private static void assertEqualTo(final StockItem expected, final StockItem actual) {
        assertThat("Stock was null", actual, is(notNullValue()));
        assertThat("Stock should have identifier", actual.getId(), is(notNullValue()));
        assertThat("Stock has incorrect name", actual.getName(), is(equalTo(expected.getName())));
        assertThat("Stock has incorrect price", actual.getCurrentPrice(), is(equalTo(expected.getCurrentPrice())));
        assertThat("Stock should have last updated date", actual.getLastUpdated(), is(notNullValue()));

        if (expected.getId() != null) {
            assertThat("Stock should have last correct identifier", actual.getId(), is(equalTo(expected.getId())));
        }
    }
}