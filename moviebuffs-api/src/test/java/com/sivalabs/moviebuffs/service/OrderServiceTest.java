package com.sivalabs.moviebuffs.service;

import com.sivalabs.moviebuffs.entity.Order;
import com.sivalabs.moviebuffs.exception.OrderNotFoundException;
import com.sivalabs.moviebuffs.exception.OrderProcessingException;
import com.sivalabs.moviebuffs.repository.OrderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static com.sivalabs.moviebuffs.datafactory.TestDataFactory.createOrder;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

class OrderServiceTest {

    private OrderRepository orderRepository;
    private OrderService orderService;

    @BeforeEach
    void setUp() {
        orderRepository = mock(OrderRepository.class);
        orderService = new OrderService(orderRepository);
    }

    @Test
    void shouldThrowExceptionWhenCancellingNonExistingOrder() {
        String orderId = "1234";
        given(orderRepository.findByOrderId(orderId)).willReturn(Optional.empty());
        assertThrows(OrderNotFoundException.class, () -> orderService.cancelOrder(orderId));
    }

    @Test
    void shouldThrowExceptionWhenCancellingDeliveredOrder() {
        String orderId = "1234";
        Order order = createOrder(1L);
        order.setStatus(Order.OrderStatus.DELIVERED);
        given(orderRepository.findByOrderId(orderId)).willReturn(Optional.of(order));
        assertThrows(OrderProcessingException.class, () -> orderService.cancelOrder(orderId));
    }

}
