package com.sivalabs.moviebuffs.jobs;

import static com.sivalabs.moviebuffs.datafactory.TestDataFactory.createOrder;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

import com.sivalabs.moviebuffs.core.entity.Order;
import com.sivalabs.moviebuffs.core.service.OrderService;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class OrderProcessingJobTest {

    @Mock private OrderService orderService;

    @InjectMocks private OrderProcessingJob orderProcessingJob;

    private List<Order> orderList = null;

    @BeforeEach
    void setUp() {
        orderList = new ArrayList<>();
        orderList.add(createOrder(1L));
        orderList.add(createOrder(2L));
        orderList.add(createOrder(3L));
    }

    @Test
    void should_process_orders() {
        given(orderService.findOrdersByStatus(Order.OrderStatus.NEW)).willReturn(orderList);

        orderProcessingJob.processOrders();

        verify(orderService, times(orderList.size())).updateOrder(any(Order.class));
    }

    @Test
    void should_ignore_if_no_orders_to_process() {
        given(orderService.findOrdersByStatus(Order.OrderStatus.NEW)).willReturn(new ArrayList<>());

        orderProcessingJob.processOrders();

        verify(orderService, never()).updateOrder(any(Order.class));
    }
}
