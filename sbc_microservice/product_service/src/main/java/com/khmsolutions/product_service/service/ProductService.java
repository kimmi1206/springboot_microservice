package com.khmsolutions.product_service.service;

import com.khmsolutions.product_service.dto.ProductRequest;
import com.khmsolutions.product_service.dto.ProductResponse;
import com.khmsolutions.product_service.model.Product;
import com.khmsolutions.product_service.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author kheiner
 * @date 24/Oct/2022
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class ProductService {

    private final ProductRepository productRepository;

    public void createProduct(ProductRequest productRequest) {
        try {
            Product product = Product.builder()
                    .name(productRequest.getName())
                    .description(productRequest.getDescription())
                    .price(productRequest.getPrice())
                    .build();
            productRepository.save(product);
            log.info("Product {} is saved", product.getId());
        } catch (Exception excp) {
            log.error(String.format("Exception: %s, Message: %s", excp.getClass().getSimpleName(), excp.getLocalizedMessage()));
        }
    }

    public List<ProductResponse> getAllProducts() {
        List<Product> products = new ArrayList<>();
        try {
            products = productRepository.findAll();
        } catch (Exception excp) {
            log.error(String.format("Exception: %s, Message: %s", excp.getClass().getSimpleName(), excp.getLocalizedMessage()));
        }
        // return products.stream().map(this::mapToProductResponse).toList();
        return products.stream().map(this::mapToProductResponse).collect(Collectors.toList());
    }

    private ProductResponse mapToProductResponse(Product product) {
        log.info("Getting All Products at -ProductService-");
        if(product == null) return new ProductResponse();
        return ProductResponse.builder()
                .id(product.getId())
                .name(product.getName())
                .description(product.getDescription())
                .price(product.getPrice())
                .build();
    }
}
