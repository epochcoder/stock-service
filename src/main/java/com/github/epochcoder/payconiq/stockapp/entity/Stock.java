package com.github.epochcoder.payconiq.stockapp.entity;


import com.github.epochcoder.payconiq.stockapp.domain.StockItem;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name = "stock")
public class Stock {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;
    private BigDecimal currentPrice;
    private Date lastUpdated;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getCurrentPrice() {
        return currentPrice;
    }

    public void setCurrentPrice(BigDecimal currentPrice) {
        this.currentPrice = currentPrice;
    }

    public Date getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(Date lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    @Override
    public String toString() {
        return "Stock{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", currentPrice=" + currentPrice +
                ", lastUpdated=" + lastUpdated +
                '}';
    }

    /**
     * Maps a domain stock item to a stock entity
     * @param stockItem the stock item to map
     * @return a new stock entity
     */
    public static Stock fromStockItem(final StockItem stockItem) {
        final Stock stock = new Stock();

        stock.setId(stockItem.getId());
        stock.setCurrentPrice(stockItem.getCurrentPrice());
        stock.setLastUpdated(stockItem.getLastUpdated());
        stock.setName(stockItem.getName());

        return stock;
    }
}
