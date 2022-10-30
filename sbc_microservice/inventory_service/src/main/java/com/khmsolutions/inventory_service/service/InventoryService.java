package com.khmsolutions.inventory_service.service;

import com.khmsolutions.inventory_service.dto.InventoryResponse;
import com.khmsolutions.inventory_service.repository.InventoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author kheiner
 * @date 25/Oct/2022
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class InventoryService {

    private final InventoryRepository inventoryRepository;

    @Transactional(readOnly = true)
    public List<InventoryResponse> isInStock(List<String> skuCode) {
        List<InventoryResponse> inventoryResponses = new ArrayList<>();
        try {
            inventoryResponses = inventoryRepository.findBySkuCodeIn(skuCode).stream()
                    .map(inventory ->
                        InventoryResponse.builder()
                                .skuCode(inventory.getSkuCode())
                                .isInStock(inventory.getQuantity() > 0)
                                .build())
                    .toList();
        } catch (Exception excp) {
            log.error(String.format("Exception: %s, Message: %s", excp.getClass().getSimpleName(), excp.getLocalizedMessage()));
        }
        return inventoryResponses;
    }
}
