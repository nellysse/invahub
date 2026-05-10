package com.inventory.api.controller;

import com.inventory.api.dto.ApiResponse;
import com.inventory.api.dto.StockUpdateRequest;
import com.inventory.api.model.Stock;
import com.inventory.api.service.StockService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/stock")
@RequiredArgsConstructor
public class StockController {

    private final StockService stockService;

    @PutMapping("/{productId}")
    @PreAuthorize("hasAnyRole('MANAGER', 'ADMIN')")
    public ResponseEntity<ApiResponse> updateStock(
            @PathVariable Long productId,
            @Valid @RequestBody StockUpdateRequest request) {
        try {
            Stock stock = stockService.updateStock(productId, request.getQuantity());
            return ResponseEntity.ok(ApiResponse.builder()
                    .success(true)
                    .message("Stock updated successfully")
                    .data(stock)
                    .build());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.builder()
                    .success(false)
                    .message("Failed to update stock: " + e.getMessage())
                    .build());
        }
    }

    @GetMapping("/{productId}")
    @PreAuthorize("hasAnyRole('USER', 'MANAGER', 'ADMIN')")
    public ResponseEntity<ApiResponse> getStock(@PathVariable Long productId) {
        try {
            Stock stock = stockService.getStockByProductId(productId);
            return ResponseEntity.ok(ApiResponse.builder()
                    .success(true)
                    .message("Stock retrieved successfully")
                    .data(stock)
                    .build());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.builder()
                    .success(false)
                    .message("Failed to retrieve stock: " + e.getMessage())
                    .build());
        }
    }
}
