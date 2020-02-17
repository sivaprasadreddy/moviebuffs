package com.sivalabs.moviebuffs.web.controller;

import com.sivalabs.moviebuffs.common.AbstractMvcUnitTest;
import com.sivalabs.moviebuffs.entity.Order;
import com.sivalabs.moviebuffs.exception.OrderProcessingException;
import com.sivalabs.moviebuffs.service.OrderService;
import com.sivalabs.moviebuffs.web.dto.OrderConfirmationDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.sivalabs.moviebuffs.datafactory.TestDataFactory.createOrder;
import static com.sivalabs.moviebuffs.utils.TestConstants.ORDERS_COLLECTION_BASE_PATH;
import static com.sivalabs.moviebuffs.utils.TestConstants.ORDERS_SINGLE_BASE_PATH;
import static org.hamcrest.CoreMatchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willThrow;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = OrderController.class)
class OrderControllerTest extends AbstractMvcUnitTest {

    @MockBean
    OrderService orderService;

    private List<Order> orderList = null;

    @BeforeEach
    void setUp() {

        orderList = new ArrayList<>();
        orderList.add(createOrder(1L));
        orderList.add(createOrder(2L));
        orderList.add(createOrder(3L));
    }

    @Test
    void shouldFetchAllOrders() throws Exception {
        given(orderService.findAllOrders()).willReturn(this.orderList);

        this.mockMvc.perform(get(ORDERS_COLLECTION_BASE_PATH))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()", is(orderList.size())));
    }

    @Test
    void shouldFetchOrderById() throws Exception {
        Order order = this.orderList.get(0);
        given(orderService.findOrderByOrderId(order.getOrderId())).willReturn(Optional.of(order));

        this.mockMvc.perform(get(ORDERS_SINGLE_BASE_PATH, order.getOrderId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.orderId", is(order.getOrderId())));
    }

    @Test
    void shouldGet404WhileFetchingNonExistingOrder() throws Exception {
        String orderId = "1234";
        given(orderService.findOrderByOrderId(orderId)).willReturn(Optional.empty());

        this.mockMvc.perform(get(ORDERS_SINGLE_BASE_PATH, orderId))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldCreateNewOrder() throws Exception {
        Order order = this.orderList.get(0);
        OrderConfirmationDTO orderConfirmationDTO = new OrderConfirmationDTO();
        orderConfirmationDTO.setOrderId("1234");
        orderConfirmationDTO.setOrderStatus(Order.OrderStatus.NEW);
        given(orderService.createOrder(any(Order.class))).willReturn(orderConfirmationDTO);

        this.mockMvc.perform(post(ORDERS_COLLECTION_BASE_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(order)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.orderId", is(orderConfirmationDTO.getOrderId())))
                .andExpect(jsonPath("$.orderStatus", is(orderConfirmationDTO.getOrderStatus().name())))
        ;

    }

    @Test
    void shouldDeleteOrder() throws Exception {
        String orderId = "1234";
        doNothing().when(orderService).cancelOrder(orderId);

        this.mockMvc.perform(delete(ORDERS_SINGLE_BASE_PATH, orderId))
                .andExpect(status().isOk());
    }

    @Test
    void shouldNotBeAbleToDeleteOrderAfterDelivered() throws Exception {
        Order order = this.orderList.get(0);
        order.setStatus(Order.OrderStatus.DELIVERED);
        willThrow(OrderProcessingException.class).given(orderService).cancelOrder(order.getOrderId());

        this.mockMvc.perform(delete(ORDERS_SINGLE_BASE_PATH, order.getOrderId()))
                .andExpect(status().is5xxServerError());

        verify(orderService, never()).createOrder(any(Order.class));
    }

    @Test
    void shouldNotBeAbleToDeleteNonExistingOrder() throws Exception {
        String orderId = "1234";
        willThrow(OrderProcessingException.class).given(orderService).cancelOrder(orderId);

        this.mockMvc.perform(delete(ORDERS_SINGLE_BASE_PATH, orderId))
                .andExpect(status().is5xxServerError());

        verify(orderService, never()).createOrder(any(Order.class));
    }
}
