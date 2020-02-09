package com.sivalabs.moviebuffs.service;

import com.sivalabs.moviebuffs.entity.Order;
import com.sivalabs.moviebuffs.repository.OrderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static com.sivalabs.moviebuffs.datafactory.TestDataFactory.createOrder;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

class OrderProcessingJobTest {

    private OrderRepository orderRepository;
    private OrderProcessingJob orderProcessingJob;

    private List<Order> orderList = null;

    @BeforeEach
    void setUp() {
        orderRepository = mock(OrderRepository.class);
        orderProcessingJob = new OrderProcessingJob(orderRepository);

        orderList = new ArrayList<>();
        orderList.add(createOrder(1L));
        orderList.add(createOrder(2L));
        orderList.add(createOrder(3L));
    }

    @Test
    void shouldProcessOrders() {
        given(orderRepository.findByStatus(Order.OrderStatus.NEW)).willReturn(orderList);

        orderProcessingJob.processOrders();

        verify(orderRepository, times(orderList.size())).save(any(Order.class));
    }

    @Test
    void shouldIgnoreIfNoOrdersToProcess() {
        given(orderRepository.findByStatus(Order.OrderStatus.NEW)).willReturn(new ArrayList<>());

        orderProcessingJob.processOrders();

        verify(orderRepository, never()).save(any(Order.class));
    }
}
