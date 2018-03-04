package com.github.epochcoder.payconiq.stockapp.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.epochcoder.payconiq.stockapp.Application;
import com.github.epochcoder.payconiq.stockapp.domain.StockItem;
import com.github.epochcoder.payconiq.stockapp.repository.StockRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.math.BigDecimal;
import java.util.Date;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@WebAppConfiguration
@ContextConfiguration(classes = {Application.class})
@RunWith(SpringJUnit4ClassRunner.class)
public class StockControllerTest {

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private StockRepository stockRepository;

    private MockMvc mvc;

    @Before
    public void prepareStocks() {
        this.mvc = MockMvcBuilders
                .webAppContextSetup(this.context)
                .build();
    }

    @Test
    public void getAllStocks() throws Exception {
        mvc.perform(get("/api/stocks"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith("application/json"))
                .andExpect(jsonPath("$.length()").value(4));
    }

    @Test
    public void getSingleStock() throws Exception {
        mvc.perform(get("/api/stocks/1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith("application/json"))
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("GOOGLE"))
                .andExpect(jsonPath("$.currentPrice").value(1071.41))
                .andExpect(jsonPath("$.lastUpdated").value(1519999561796L));
    }

    @Test
    public void createStock() throws Exception {
        final String content = this.mapper.writeValueAsString(StockItem.builder()
                .withName("HI")
                .withLastUpdated(new Date())
                .withCurrentPrice(new BigDecimal("123"))
                .build());

        mvc.perform(post("/api/stocks")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(content))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("HI"))
                .andExpect(jsonPath("$.currentPrice").value(123));

        this.stockRepository.delete(5L);
    }

    @Test
    public void updateStock() throws Exception {
        final String content = this.mapper.writeValueAsString(StockItem.builder()
                .withName("UPDATED")
                .withLastUpdated(new Date())
                .withCurrentPrice(new BigDecimal("321"))
                .build());

        mvc.perform(put("/api/stocks/2")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(content))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("UPDATED"))
                .andExpect(jsonPath("$.currentPrice").value(321));

        mvc.perform(get("/api/stocks"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith("application/json"))
                .andExpect(jsonPath("$.length()").value(4));
    }
}