package com.sivalabs.moviebuffs.web.controller;

import com.sivalabs.moviebuffs.common.AbstractIntegrationTest;
import com.sivalabs.moviebuffs.entity.Order;
import com.sivalabs.moviebuffs.repository.OrderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.zalando.problem.ProblemModule;
import org.zalando.problem.violations.ConstraintViolationProblemModule;

import java.util.ArrayList;
import java.util.List;

import static com.sivalabs.moviebuffs.datafactory.TestDataFactory.createOrder;
import static com.sivalabs.moviebuffs.utils.TestConstants.ORDERS_COLLECTION_BASE_PATH;
import static com.sivalabs.moviebuffs.utils.TestConstants.ORDERS_SINGLE_BASE_PATH;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class OrderControllerIT  extends AbstractIntegrationTest {

    @Autowired
    private OrderRepository orderRepository;

    private List<Order> orderList = null;

    @BeforeEach
    void setUp() {
        objectMapper.registerModule(new ProblemModule());
        objectMapper.registerModule(new ConstraintViolationProblemModule());

        orderList = new ArrayList<>();
        orderList.add(createOrder(1L));
        orderList.add(createOrder(2L));
        orderList.add(createOrder(3L));

        orderList = orderRepository.saveAll(orderList);
    }

    @Test
    void shouldFetchAllOrders() throws Exception {
        this.mockMvc.perform(get(ORDERS_COLLECTION_BASE_PATH))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()", is(orderList.size())));
    }

    @Test
    void shouldFetchOrderById() throws Exception {
        Order order = this.orderList.get(0);

        this.mockMvc.perform(get(ORDERS_SINGLE_BASE_PATH, order.getOrderId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.orderId", is(order.getOrderId())));
    }

    @Test
    void shouldCreateNewOrder() throws Exception {
        Order order = this.orderList.get(0);

        this.mockMvc.perform(post(ORDERS_COLLECTION_BASE_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(order)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.orderId", notNullValue()))
                .andExpect(jsonPath("$.orderStatus", is(Order.OrderStatus.NEW.name())))
        ;

    }

    @Test
    void shouldDeleteOrder() throws Exception {
        String orderId = orderList.get(0).getOrderId();

        this.mockMvc.perform(delete(ORDERS_SINGLE_BASE_PATH, orderId))
                .andExpect(status().isOk());
    }
}
