package com.inventory.api.controller;

import com.inventory.api.dto.ApiResponse;
import com.inventory.api.repository.ProductRepository;
import com.inventory.api.repository.UserRepository;
import com.inventory.api.repository.StockRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/dashboard")
@RequiredArgsConstructor
public class DashboardController {

    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final StockRepository stockRepository;

    @GetMapping("/stats")
    @PreAuthorize("hasAnyRole('USER', 'MANAGER', 'ADMIN')")
    public ResponseEntity<ApiResponse> getStats() {
        long totalProducts = productRepository.count();
        long totalUsers = userRepository.count();
        long lowStockProducts = stockRepository.findAll().stream()
                .filter(stock -> stock.getQuantity() < 10) // Arbitrary threshold
                .count();

        Map<String, Long> stats = new HashMap<>();
        stats.put("totalProducts", totalProducts);
        stats.put("totalUsers", totalUsers);
        stats.put("lowStockProducts", lowStockProducts);

        return ResponseEntity.ok(ApiResponse.builder()
                .success(true)
                .message("Dashboard stats retrieved successfully")
                .data(stats)
                .build());
    }
}
