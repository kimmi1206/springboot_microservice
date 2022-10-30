package com.khmsolutions.inventory_service.repository;

import com.khmsolutions.inventory_service.model.Inventory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/**
 * @author kheiner
 * @date 25/Oct/2022
 */
public interface InventoryRepository extends JpaRepository<Inventory, Long> {
    // public Optional<Inventory> findBySkuCode(String skuCode);

    // findByFieldIn(Iterable<Role> role)   or  findAllByFieldIn(Iterable<Role> role)
    public List<Inventory> findBySkuCodeIn(List<String> skuCode);
}
