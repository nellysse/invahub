package com.inventory.api.service;

import com.inventory.api.model.Product;
import com.inventory.api.model.Stock;
import com.inventory.api.repository.StockRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StockServiceTest {

    @Mock
    private StockRepository stockRepository;

    @InjectMocks
    private StockService stockService;

    private Product testProduct;
    private Stock testStock;

    @BeforeEach
    void setUp() {
        testProduct = Product.builder()
                .id(1L)
                .name("Test Product")
                .sku("TEST-001")
                .description("Test Description")
                .price(new BigDecimal("99.99"))
                .build();

        testStock = Stock.builder()
                .id(1L)
                .product(testProduct)
                .quantity(100)
                .build();
    }

    @Test
    void updateStock_ValidQuantity_ShouldUpdateSuccessfully() {
        // Arrange
        Long productId = 1L;
        Integer newQuantity = 150;
        
        when(stockRepository.findByProductId(productId)).thenReturn(Optional.of(testStock));
        when(stockRepository.save(any(Stock.class))).thenReturn(testStock);

        // Act
        Stock result = stockService.updateStock(productId, newQuantity);

        // Assert
        assertNotNull(result);
        assertEquals(newQuantity, result.getQuantity());
        verify(stockRepository, times(1)).findByProductId(productId);
        verify(stockRepository, times(1)).save(testStock);
    }

    @Test
    void updateStock_NegativeQuantity_ShouldThrowException() {
        // Arrange
        Long productId = 1L;
        Integer negativeQuantity = -10;

        // Act & Assert
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> stockService.updateStock(productId, negativeQuantity)
        );

        assertEquals("Stock quantity cannot be negative", exception.getMessage());
        verify(stockRepository, never()).save(any());
    }

    @Test
    void updateStock_NonExistentProduct_ShouldThrowException() {
        // Arrange
        Long productId = 999L;
        Integer newQuantity = 50;
        
        when(stockRepository.findByProductId(productId)).thenReturn(Optional.empty());

        // Act & Assert
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> stockService.updateStock(productId, newQuantity)
        );

        assertTrue(exception.getMessage().contains("Stock not found"));
        verify(stockRepository, times(1)).findByProductId(productId);
        verify(stockRepository, never()).save(any());
    }

    @Test
    void getStockByProductId_ExistingProduct_ShouldReturnStock() {
        // Arrange
        Long productId = 1L;
        when(stockRepository.findByProductId(productId)).thenReturn(Optional.of(testStock));

        // Act
        Stock result = stockService.getStockByProductId(productId);

        // Assert
        assertNotNull(result);
        assertEquals(testStock.getQuantity(), result.getQuantity());
        assertEquals(testStock.getProduct().getId(), result.getProduct().getId());
        verify(stockRepository, times(1)).findByProductId(productId);
    }

    @Test
    void getStockByProductId_NonExistentProduct_ShouldThrowException() {
        // Arrange
        Long productId = 999L;
        when(stockRepository.findByProductId(productId)).thenReturn(Optional.empty());

        // Act & Assert
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> stockService.getStockByProductId(productId)
        );

        assertTrue(exception.getMessage().contains("Stock not found"));
        verify(stockRepository, times(1)).findByProductId(productId);
    }

    @Test
    void updateStock_ZeroQuantity_ShouldUpdateSuccessfully() {
        // Arrange
        Long productId = 1L;
        Integer zeroQuantity = 0;
        
        when(stockRepository.findByProductId(productId)).thenReturn(Optional.of(testStock));
        when(stockRepository.save(any(Stock.class))).thenReturn(testStock);

        // Act
        Stock result = stockService.updateStock(productId, zeroQuantity);

        // Assert
        assertNotNull(result);
        assertEquals(zeroQuantity, result.getQuantity());
        verify(stockRepository, times(1)).save(testStock);
    }
}
