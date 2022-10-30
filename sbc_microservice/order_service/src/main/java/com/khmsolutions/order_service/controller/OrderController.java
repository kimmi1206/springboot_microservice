package com.khmsolutions.order_service.controller;

import com.khmsolutions.order_service.dto.OrderRequest;
import com.khmsolutions.order_service.service.OrderService;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import io.github.resilience4j.timelimiter.annotation.TimeLimiter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.CompletableFuture;

/**
 * @author kheiner
 * @date 25/Oct/2022
 */
@RestController
@RequestMapping("/api/order")
@RequiredArgsConstructor
@CircuitBreaker(name = "inventory", fallbackMethod = "fallbackMethod")
@Slf4j
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    //@TimeLimiter(name = "inventory")
    @Retry(name = "inventory")
    public String placeOrder(@RequestBody OrderRequest orderRequest){
        return orderService.placeOrder(orderRequest);
    }

    public String fallbackMethod(OrderRequest orderRequest, RuntimeException runtimeException) {
        log.info("{},{}", orderRequest.toString(),runtimeException.getLocalizedMessage());
        return "Something went wrong, try again after some time";
    }
}
