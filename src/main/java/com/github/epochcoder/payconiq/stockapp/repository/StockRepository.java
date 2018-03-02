package com.github.epochcoder.payconiq.stockapp.repository;

import com.github.epochcoder.payconiq.stockapp.entity.Stock;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StockRepository extends PagingAndSortingRepository<Stock, Long> {

}
