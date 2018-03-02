package com.github.epochcoder.payconiq.stockapp.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import org.springframework.util.Assert;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Objects;

/**
 * The stock domain object, represents a single stock item
 */
@JsonDeserialize(builder = StockItem.Builder.class)
@JsonInclude(JsonInclude.Include.NON_NULL)
public final class StockItem {

    private final Long id;
    private final String name;
    private final BigDecimal currentPrice;
    private final Date lastUpdated;


    private StockItem(Long id, String name, BigDecimal currentPrice, Date lastUpdated) {
        this.id = id;
        this.name = name;
        this.currentPrice = currentPrice;
        this.lastUpdated = lastUpdated;
    }

    /**
     * @return the internal identifier of this stock item
     */
    public Long getId() {
        return id;
    }

    /**
     * @return the name of this stock
     */
    public String getName() {
        return name;
    }

    /**
     * @return the current price of this stock
     */
    public BigDecimal getCurrentPrice() {
        return currentPrice;
    }

    /**
     * @return the last time that this stock has been updated
     */
    public Date getLastUpdated() {
        return lastUpdated;
    }

    /**
     * @return a new builder instance to create stock items
     */
    public static Builder builder() {
        return new Builder();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        StockItem stockItem = (StockItem) o;

        // having the same name makes two stock items unique
        // not using id, since it may be saved or not
        return Objects.equals(getName(), stockItem.getName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getName());
    }

    @Override
    public String toString() {
        return "StockItem{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", currentPrice=" + currentPrice +
                ", lastUpdated=" + lastUpdated +
                '}';
    }

    @JsonPOJOBuilder
    public static final class Builder {

        private Long id;
        private String name;
        private BigDecimal currentPrice;
        private Date lastUpdated;

        public Builder withId(Long id) {
            this.id = id;
            return this;
        }

        public Builder withName(String name) {
            this.name = Objects.requireNonNull(name, "Cannot assign a null name");
            return this;
        }

        public Builder withCurrentPrice(BigDecimal currentPrice) {
            this.currentPrice = Objects.requireNonNull(currentPrice, "Cannot create a stock without a price");
            return this;
        }

        public Builder withLastUpdated(Date lastUpdated) {
            this.lastUpdated = Objects.requireNonNull(lastUpdated, "Cannot create a stock without a last updated time");
            return this;
        }

        /**
         * builds this stock item
         *
         * @return a non null validated immutable instance of a stock item
         */
        public StockItem build() {
            this.validate();

            return new StockItem(this.id, this.name, this.currentPrice, this.lastUpdated);
        }

        /**
         * validates that we have enough (valid) information to create this stock item
         */
        private void validate() {
            Assert.isTrue(this.name != null && !"".equals(this.name.trim()), "Stock item needs a valid name");
            Assert.isTrue(this.currentPrice != null, "Stock item needs a valid price");
            Assert.isTrue(this.lastUpdated != null, "Stock item needs a valid updated time");

            Assert.isTrue(this.currentPrice.compareTo(BigDecimal.ZERO) >= 0, "Stock price has to be positive");
            Assert.isTrue(this.lastUpdated.compareTo(new Date()) <= 0, "Stock price cannot be future dated");
        }
    }
}
