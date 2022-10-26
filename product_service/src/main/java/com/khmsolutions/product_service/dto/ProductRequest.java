package com.khmsolutions.product_service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * @author kheiner
 * @date 24/Oct/2022
 */
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data // getter, setter, equals, hash
public class ProductRequest {
    private String name;
    private String description;
    private BigDecimal price;
}
