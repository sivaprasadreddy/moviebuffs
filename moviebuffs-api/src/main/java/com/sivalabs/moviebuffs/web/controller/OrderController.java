package com.sivalabs.moviebuffs.web.controller;

import com.sivalabs.moviebuffs.entity.Order;
import com.sivalabs.moviebuffs.service.OrderService;
import com.sivalabs.moviebuffs.web.dto.OrderConfirmationDTO;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
@Slf4j
public class OrderController {

    private final OrderService orderService;
    private final Counter ordersCounter;

    public OrderController(MeterRegistry registry, OrderService orderService) {
        this.orderService = orderService;
        this.ordersCounter = registry.counter("counter.orders");
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public OrderConfirmationDTO createOrder(@RequestBody Order order) {
        OrderConfirmationDTO confirmation = orderService.createOrder(order);
        ordersCounter.increment();
        return confirmation;
    }

    @GetMapping
    public List<Order> getAllOrders() {
        return this.orderService.findAllOrders();
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<Order> getOrderByOrderId(@PathVariable String orderId) {
        log.info("Getting order by id: {}", orderId);
        return orderService.findOrderByOrderId(orderId)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{orderId}")
    public void cancelOrder(@PathVariable String orderId) {
        log.info("Cancelling order by id: {}", orderId);
        this.orderService.cancelOrder(orderId);
    }
}
