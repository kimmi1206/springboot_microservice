package com.khmsolutions.inventory_service.controller;

import com.khmsolutions.inventory_service.dto.InventoryResponse;
import com.khmsolutions.inventory_service.service.InventoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author kheiner
 * @date 25/Oct/2022
 */
@RestController
@RequestMapping("/api/inventory")
@RequiredArgsConstructor
public class InventoryController {

    private final InventoryService inventoryService;

    // http://localhost:8082/api/inventory/iphone-13,iphone-13-red
    // http://localhost:8082/api/inventory?skuCode=iphone-13&skuCode=iphone-13-red

    //@GetMapping("/{sku-code}")
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    // public boolean isInStock(@PathVariable("sku-code") String skuCode) {
    public List<InventoryResponse> isInStock(@RequestParam List<String> skuCode) {
        return inventoryService.isInStock(skuCode);
    }
}
