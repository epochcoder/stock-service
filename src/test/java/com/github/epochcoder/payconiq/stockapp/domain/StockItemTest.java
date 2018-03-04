package com.github.epochcoder.payconiq.stockapp.domain;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.epochcoder.payconiq.stockapp.entity.Stock;
import org.junit.Test;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Date;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

public class StockItemTest {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    private static final String JSON = "{\"id\":1,\"name\":\"BTC\",\"currentPrice\":0,\"lastUpdated\":%s}";

    @Test
    public void buildAndSerialize() throws JsonProcessingException {
        final Date date = new Date();
        final String json = OBJECT_MAPPER.writeValueAsString(StockItem.builder()
                .withId(1L)
                .withName("BTC")
                .withLastUpdated(date)
                .withCurrentPrice(BigDecimal.ZERO)
                .build());

        final String expected = String.format(JSON, date.getTime());
        assertThat("Json serialized incorrectly!", json, is(equalTo(expected)));
    }

    @Test
    public void deserializeAndBuild() throws IOException {
        final Date date = new Date();
        final String jsonItem = String.format(JSON, date.getTime());
        final StockItem deserializedItem = OBJECT_MAPPER.readValue(jsonItem, StockItem.class);
        final StockItem item = StockItem.builder()
                .withId(1L)
                .withName("BTC")
                .withLastUpdated(date)
                .withCurrentPrice(BigDecimal.ZERO)
                .build();

        assertThat("Object deserialized incorrectly!", deserializedItem, is(equalTo(item)));
    }


    @Test(expected = IllegalArgumentException.class)
    public void needsArguments() {
        StockItem.builder().build();
    }

    @Test(expected = IllegalArgumentException.class)
    public void needsPrice() {
        StockItem.builder()
                .withName("BTC")
                .withLastUpdated(new Date())
                .build();
    }

    @Test(expected = IllegalArgumentException.class)
    public void needsPositivePrice() {
        StockItem.builder()
                .withName("BTC")
                .withLastUpdated(new Date())
                .withCurrentPrice(new BigDecimal("-1"))
                .build();
    }

    @Test(expected = IllegalArgumentException.class)
    public void needsLastUpdated() {
        StockItem.builder()
                .withName("BTC")
                .withCurrentPrice(BigDecimal.ONE)
                .build();
    }

    @Test(expected = IllegalArgumentException.class)
    public void needsValidLastUpdated() {
        StockItem.builder()
                .withName("BTC")
                .withLastUpdated(new Date(System.currentTimeMillis() + 1000))
                .withCurrentPrice(BigDecimal.ZERO)
                .build();
    }

    @Test(expected = IllegalArgumentException.class)
    public void needsName() {
        StockItem.builder()
                .withLastUpdated(new Date())
                .withCurrentPrice(BigDecimal.ONE)
                .build();
    }

    @Test(expected = IllegalArgumentException.class)
    public void needsValidName() {
        StockItem.builder()
                .withName("    ")
                .withLastUpdated(new Date())
                .withCurrentPrice(BigDecimal.ONE)
                .build();
    }

    @Test(expected = NullPointerException.class)
    public void doesNotAcceptNullPrice() {
        StockItem.builder()
                .withName("BTC")
                .withLastUpdated(new Date())
                .withCurrentPrice(null)
                .build();
    }

    @Test(expected = NullPointerException.class)
    public void doesNotAcceptNullLastUpdated() {
        StockItem.builder()
                .withName("BTC")
                .withLastUpdated(null)
                .withCurrentPrice(BigDecimal.ONE)
                .build();
    }

    @Test(expected = NullPointerException.class)
    public void doesNotAcceptNullName() {
        StockItem.builder()
                .withName(null)
                .withLastUpdated(new Date())
                .withCurrentPrice(BigDecimal.ONE)
                .build();
    }

    @Test
    public void buildsWithEntity() {
        final Stock stockEntity = new Stock();
        stockEntity.setId(1L);
        stockEntity.setCurrentPrice(BigDecimal.ZERO);
        stockEntity.setLastUpdated(new Date());
        stockEntity.setName("ENTS");

        StockItem item = StockItem.builder()
                .withEntity(stockEntity)
                .build();

        assertEquals(item.getName(), stockEntity.getName());
        assertEquals(item.getId(), stockEntity.getId());
        assertEquals(item.getLastUpdated(), stockEntity.getLastUpdated());
        assertEquals(item.getCurrentPrice(), stockEntity.getCurrentPrice());
    }
}