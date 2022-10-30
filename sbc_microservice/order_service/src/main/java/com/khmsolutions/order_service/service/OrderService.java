package com.khmsolutions.order_service.service;

import com.khmsolutions.order_service.dto.InventoryResponse;
import com.khmsolutions.order_service.dto.OrderLineItemsDto;
import com.khmsolutions.order_service.dto.OrderRequest;
import com.khmsolutions.order_service.event.OrderPlacedEvent;
import com.khmsolutions.order_service.model.Order;
import com.khmsolutions.order_service.model.OrderLineItems;
import com.khmsolutions.order_service.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.sleuth.Span;
import org.springframework.cloud.sleuth.Tracer;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * @author kheiner
 * @date 25/Oct/2022
 */
@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class OrderService {

    private final OrderRepository orderRepository;
    private final WebClient.Builder webClientBuilder;
    private final Tracer tracer;
    private final KafkaTemplate<String, OrderPlacedEvent> kafkaTemplate;

    public String placeOrder(OrderRequest orderRequest) {
        Order order = new Order();
        order.setOrderNumber(UUID.randomUUID().toString());

        List<OrderLineItems> orderLineItems = orderRequest.getOrderLineItemsDtoList()
                .stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());

        order.setOrderLineItemsList(orderLineItems);

        List<String> skuCodes = order.getOrderLineItemsList().stream()
                .map(OrderLineItems::getSkuCode)
                .toList();

        // CALL Inventory Service, and place order if product is in stock
        Span inventoryServiceLookup = tracer.nextSpan().name("InventoryServiceLookup");

        try (Tracer.SpanInScope spanInScope = tracer.withSpan(inventoryServiceLookup.start())) {
            try {
                InventoryResponse[] inventoryResponsesArray = webClientBuilder.build().get()
                        .uri("http://inventory-service/api/inventory",
                                uriBuilder ->
                                        uriBuilder.queryParam("skuCode", skuCodes)
                                                .build())
                        .retrieve()
                        .bodyToMono(InventoryResponse[].class)
                        .block();

                boolean allItemsInStock =
                        Arrays.stream(inventoryResponsesArray)
                                .allMatch(InventoryResponse::isInStock);

                if (allItemsInStock) {
                    orderRepository.save(order);
                    kafkaTemplate.send("notificationTopic",
                            new OrderPlacedEvent(order.getOrderNumber()));
                    return "Order Placed Successfully";
                } else {
                    throw new IllegalArgumentException("Product is not in stock");
                }
            } catch (Exception excp) {
                String msg = String.format(
                        "Exception: %s, Message: %s",
                        excp.getClass().getSimpleName(),
                        excp.getLocalizedMessage());
                log.error(msg);
                return msg;
            }
        } finally {
            inventoryServiceLookup.end();
        }
    }

    private OrderLineItems mapToDto(OrderLineItemsDto orderLineItemsDto) {
        OrderLineItems orderLineItems = new OrderLineItems();

        orderLineItems.setPrice(orderLineItemsDto.getPrice());
        orderLineItems.setQuantity(orderLineItemsDto.getQuantity());
        orderLineItems.setSkuCode(orderLineItemsDto.getSkuCode());

        return orderLineItems;
    }
}
