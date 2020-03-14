package com.sivalabs.moviebuffs.web.api;

import com.sivalabs.moviebuffs.common.AbstractMvcUnitTest;
import com.sivalabs.moviebuffs.core.entity.Order;
import com.sivalabs.moviebuffs.core.exception.BadRequestException;
import com.sivalabs.moviebuffs.core.service.OrderService;
import com.sivalabs.moviebuffs.web.dto.OrderConfirmationDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.sivalabs.moviebuffs.core.utils.TestConstants.ORDERS_COLLECTION_BASE_PATH;
import static com.sivalabs.moviebuffs.core.utils.TestConstants.ORDERS_SINGLE_BASE_PATH;
import static com.sivalabs.moviebuffs.datafactory.TestDataFactory.createOrder;
import static org.hamcrest.CoreMatchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willThrow;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = {OrderRestController.class})
class OrderRestControllerTest extends AbstractMvcUnitTest {

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
    @WithMockUser(roles = {"USER", "ADMIN"})
    void should_fetch_all_orders() throws Exception {
        given(orderService.findAllOrders()).willReturn(this.orderList);

        this.mockMvc.perform(get(ORDERS_COLLECTION_BASE_PATH))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()", is(orderList.size())));
    }

    @Test
    @WithMockUser
    void should_fetch_order_by_id() throws Exception {
        Order order = this.orderList.get(0);
        given(orderService.findOrderByOrderId(order.getOrderId())).willReturn(Optional.of(order));
        given(securityService.isCurrentUserHasPrivilege(anyLong())).willReturn(true);

        this.mockMvc.perform(get(ORDERS_SINGLE_BASE_PATH, order.getOrderId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.orderId", is(order.getOrderId())));
    }

    @Test
    @WithMockUser
    void should_get_404_while_fetching_non_existing_order() throws Exception {
        String orderId = "1234";
        given(orderService.findOrderByOrderId(orderId)).willReturn(Optional.empty());

        this.mockMvc.perform(get(ORDERS_SINGLE_BASE_PATH, orderId))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser
    void should_create_new_order() throws Exception {
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
    @WithMockUser
    void should_delete_order() throws Exception {
        Order order = this.orderList.get(0);
        String orderId = order.getOrderId();
        given(orderService.findOrderByOrderId(order.getOrderId())).willReturn(Optional.of(order));
        given(securityService.isCurrentUserHasPrivilege(anyLong())).willReturn(true);
        doNothing().when(orderService).cancelOrder(orderId);

        this.mockMvc.perform(delete(ORDERS_SINGLE_BASE_PATH, orderId))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser
    void should_not_be_able_to_cancel_order_after_delivered() throws Exception {
        Order order = this.orderList.get(0);
        order.setStatus(Order.OrderStatus.DELIVERED);
        given(orderService.findOrderByOrderId(order.getOrderId())).willReturn(Optional.of(order));
        given(securityService.isCurrentUserHasPrivilege(anyLong())).willReturn(true);
        willThrow(BadRequestException.class).given(orderService).cancelOrder(order.getOrderId());

        this.mockMvc.perform(delete(ORDERS_SINGLE_BASE_PATH, order.getOrderId()))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser
    void should_not_be_able_to_cancel_others_order_if_not_admin() throws Exception {
        Order order = this.orderList.get(0);
        order.setStatus(Order.OrderStatus.DELIVERED);
        given(orderService.findOrderByOrderId(order.getOrderId())).willReturn(Optional.of(order));
        given(securityService.isCurrentUserHasPrivilege(anyLong())).willReturn(false);
        //willThrow(BadRequestException.class).given(orderService).cancelOrder(order.getOrderId());

        this.mockMvc.perform(delete(ORDERS_SINGLE_BASE_PATH, order.getOrderId()))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser
    void should_not_be_able_to_cancel_non_existing_order() throws Exception {
        String orderId = "1234";
        given(orderService.findOrderByOrderId(orderId)).willReturn(Optional.empty());

        this.mockMvc.perform(delete(ORDERS_SINGLE_BASE_PATH, orderId))
                .andExpect(status().isNotFound());
    }
}
