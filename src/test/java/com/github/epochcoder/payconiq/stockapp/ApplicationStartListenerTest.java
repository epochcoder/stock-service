package com.github.epochcoder.payconiq.stockapp;

import com.github.epochcoder.payconiq.stockapp.entity.Stock;
import com.github.epochcoder.payconiq.stockapp.repository.StockRepository;
import org.hamcrest.Matchers;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)
public class ApplicationStartListenerTest {

    private static final int AMT_STOCKS = 4;

    @Autowired
    private StockRepository stockRepository;

    @Test
    public void contextLoadsAndPopulatesData() {
        assertThat(AMT_STOCKS + " Stocks should have been loaded",
                stockRepository.findAll(), Matchers.iterableWithSize(AMT_STOCKS));

        // spot check one stock for accuracy
        final Stock stock = stockRepository.findOne(1L);

        assertNotNull("First stock was null", stock);
        assertThat("Stock has incorrect name", stock.getName(), is(equalTo("GOOGLE")));
        assertThat("Stock has incorrect price", stock.getCurrentPrice(), is(equalTo(new BigDecimal("1071.41"))));
        assertThat("Stock has incorrect time", formatDate(stock.getLastUpdated()), is(equalTo("2018-03-02 14:06:01")));
    }

    private static String formatDate(final Date date) {
        assertNotNull("Date from data source was null", date);

        final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        sdf.setTimeZone(TimeZone.getTimeZone("GMT"));

        return sdf.format(date);
    }
}