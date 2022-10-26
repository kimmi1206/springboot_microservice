package com.khmsolutions.product_service.repository;

import com.khmsolutions.product_service.model.Product;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * @author kheiner
 * @date 24/Oct/2022
 */
public interface ProductRepository extends MongoRepository<Product, String> {
}
