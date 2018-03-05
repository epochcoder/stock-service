package com.github.epochcoder.payconiq.stockapp.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.epochcoder.payconiq.stockapp.Application;
import com.github.epochcoder.payconiq.stockapp.domain.StockItem;
import com.github.epochcoder.payconiq.stockapp.exception.StockNotFoundException;
import com.github.epochcoder.payconiq.stockapp.service.StockService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@WebAppConfiguration
@ContextConfiguration(classes = {Application.class, StockControllerTest.class})
@RunWith(SpringJUnit4ClassRunner.class)
public class StockControllerTest {

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private StockService stockService;

    private MockMvc mvc;

    @Bean
    public StockService stockService() {
        return mock(StockService.class);
    }

    @Before
    public void prepareStocks() {
        this.mvc = MockMvcBuilders
                .webAppContextSetup(this.context)
                .build();

        reset(this.stockService);
    }

    @Test
    public void getAllStocks() throws Exception {
        final List<StockItem> content = new ArrayList<>();
        content.add(createStockItem(1L));
        content.add(createStockItem(2L));

        when(this.stockService.retrieveAll(any(PageRequest.class))).thenReturn(new PageImpl<>(content));

        mvc.perform(get("/api/stocks"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith("application/json"))
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].currentPrice").value("1"))
                .andExpect(jsonPath("$[1].currentPrice").value("2"))
                .andExpect(jsonPath("$[0].name").value("NAME_1"))
                .andExpect(jsonPath("$[1].name").value("NAME_2"));
    }

    @Test
    public void getSingleStock() throws Exception {
        when(this.stockService.retrieve(any(Long.class))).thenReturn(createStockItem(1L));

        mvc.perform(get("/api/stocks/1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith("application/json"))
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("NAME_1"))
                .andExpect(jsonPath("$.currentPrice").value(1L))
                .andExpect(jsonPath("$.lastUpdated").value(notNullValue(Long.class)));
    }

    @Test
    public void getInvalidStock() throws Exception {
        when(this.stockService.retrieve(any(Long.class))).thenThrow(new StockNotFoundException(1L));

        mvc.perform(get("/api/stocks/1"))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    public void createStock() throws Exception {
        final StockItem item = createStockItem(1L);
        final String content = this.mapper.writeValueAsString(item);

        when(this.stockService.add(any(StockItem.class))).thenReturn(item);

        mvc.perform(post("/api/stocks")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(content))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("NAME_1"))
                .andExpect(jsonPath("$.currentPrice").value(1));
    }

    @Test
    public void createInvalidStock() throws Exception {
        final String badContent = "{\"id\":1,\"name\":\"NAME_1\",\"currentPrice\":-1}";

        mvc.perform(post("/api/stocks")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(badContent))
                .andDo(print())
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void updateStock() throws Exception {
        final StockItem item = createStockItem(1L);
        final StockItem updated = StockItem.builder()
                .withId(1L)
                .withName("UPDATED")
                .withLastUpdated(new Date())
                .withCurrentPrice(new BigDecimal("321"))
                .build();

        when(this.stockService.retrieve(any(Long.class))).thenReturn(item);
        when(this.stockService.update(any(Long.class), any(StockItem.class))).thenReturn(updated);

        final String content = this.mapper.writeValueAsString(item);

        mvc.perform(put("/api/stocks/1")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(content))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("UPDATED"))
                .andExpect(jsonPath("$.currentPrice").value(321));
    }

    private static StockItem createStockItem(final Long id) {
        return StockItem.builder()
                .withId(id)
                .withName("NAME_" + id)
                .withCurrentPrice(new BigDecimal(id))
                .withLastUpdated(new Date())
                .build();
    }
}