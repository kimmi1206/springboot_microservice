package com.khmsolutions.order_service.repository;

import com.khmsolutions.order_service.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author kheiner
 * @date 25/Oct/2022
 */
public interface OrderRepository extends JpaRepository<Order, Long> {
}
