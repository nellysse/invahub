package com.inventory.api.service;

import com.inventory.api.dto.ProductRequest;
import com.inventory.api.dto.ProductResponse;
import com.inventory.api.model.Product;
import com.inventory.api.model.Stock;
import com.inventory.api.repository.ProductRepository;
import com.inventory.api.repository.StockRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final StockRepository stockRepository;

    @Transactional
    public ProductResponse createProduct(ProductRequest request) {
        if (productRepository.existsBySku(request.getSku())) {
            throw new IllegalArgumentException("Product with SKU " + request.getSku() + " already exists");
        }

        Product product = Product.builder()
                .name(request.getName())
                .sku(request.getSku())
                .category(request.getCategory())
                .description(request.getDescription())
                .price(request.getPrice())
                .build();

        Product savedProduct = productRepository.save(product);

        Stock stock = Stock.builder()
                .product(savedProduct)
                .quantity(request.getInitialQuantity())
                .build();

        stockRepository.save(stock);

        return mapToResponse(savedProduct, stock);
    }

    @Transactional
    public ProductResponse updateProduct(Long id, ProductRequest request) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Product not found with id: " + id));

        // Check SKU uniqueness if changed
        if (!product.getSku().equals(request.getSku()) && 
            productRepository.existsBySku(request.getSku())) {
            throw new IllegalArgumentException("Product with SKU " + request.getSku() + " already exists");
        }

        product.setName(request.getName());
        product.setSku(request.getSku());
        product.setCategory(request.getCategory());
        product.setDescription(request.getDescription());
        product.setPrice(request.getPrice());

        Product updatedProduct = productRepository.save(product);
        Stock stock = stockRepository.findByProductId(id)
                .orElseThrow(() -> new IllegalArgumentException("Stock not found for product"));

        return mapToResponse(updatedProduct, stock);
    }

    @Transactional
    public void deleteProduct(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Product not found with id: " + id));
        
        productRepository.delete(product);
    }

    public ProductResponse getProductById(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Product not found with id: " + id));
        
        Stock stock = stockRepository.findByProductId(id)
                .orElseThrow(() -> new IllegalArgumentException("Stock not found for product"));

        return mapToResponse(product, stock);
    }

    public List<ProductResponse> getAllProducts() {
        List<Product> products = productRepository.findAll();
        return products.stream()
                .map(product -> {
                    Stock stock = stockRepository.findByProductId(product.getId())
                            .orElse(Stock.builder().quantity(0).build());
                    return mapToResponse(product, stock);
                })
                .collect(Collectors.toList());
    }

    public List<ProductResponse> searchProducts(String name) {
        List<Product> products = productRepository.findByNameContainingIgnoreCase(name);
        return products.stream()
                .map(product -> {
                    Stock stock = stockRepository.findByProductId(product.getId())
                            .orElse(Stock.builder().quantity(0).build());
                    return mapToResponse(product, stock);
                })
                .collect(Collectors.toList());
    }

    private ProductResponse mapToResponse(Product product, Stock stock) {
        return ProductResponse.builder()
                .id(product.getId())
                .name(product.getName())
                .sku(product.getSku())
                .category(product.getCategory())
                .description(product.getDescription())
                .price(product.getPrice())
                .stockQuantity(stock != null ? stock.getQuantity() : 0)
                .createdAt(product.getCreatedAt())
                .build();
    }
}
