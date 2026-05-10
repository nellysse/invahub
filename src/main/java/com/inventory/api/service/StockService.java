package com.inventory.api.service;

import com.inventory.api.model.Stock;
import com.inventory.api.repository.StockRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class StockService {

    private final StockRepository stockRepository;

    @Transactional
    public Stock updateStock(Long productId, Integer newQuantity) {
        if (newQuantity < 0) {
            throw new IllegalArgumentException("Stock quantity cannot be negative");
        }

        Stock stock = stockRepository.findByProductId(productId)
                .orElseThrow(() -> new IllegalArgumentException("Stock not found for product id: " + productId));

        stock.setQuantity(newQuantity);
        return stockRepository.save(stock);
    }

    public Stock getStockByProductId(Long productId) {
        return stockRepository.findByProductId(productId)
                .orElseThrow(() -> new IllegalArgumentException("Stock not found for product id: " + productId));
    }
}
