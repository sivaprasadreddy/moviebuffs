package com.sivalabs.moviebuffs.core.service;

import com.sivalabs.moviebuffs.core.entity.Order;
import com.sivalabs.moviebuffs.core.exception.BadRequestException;
import com.sivalabs.moviebuffs.core.exception.ResourceNotFoundException;
import com.sivalabs.moviebuffs.core.repository.OrderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static com.sivalabs.moviebuffs.datafactory.TestDataFactory.createOrder;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

	@Mock
	private OrderRepository orderRepository;

	@InjectMocks
	private OrderService orderService;

	String orderId;

	@BeforeEach
	void setUp() {
		orderId = "1234";
	}

	@Test
	void should_throw_exception_when_cancelling_non_existing_order() {
		given(orderRepository.findByOrderId(orderId)).willReturn(Optional.empty());

		assertThrows(ResourceNotFoundException.class, () -> orderService.cancelOrder(orderId));
	}

	@Test
	void should_throw_exception_when_cancelling_delivered_order() {
		Order order = createOrder(1L);
		order.setStatus(Order.OrderStatus.DELIVERED);
		given(orderRepository.findByOrderId(orderId)).willReturn(Optional.of(order));

		assertThrows(BadRequestException.class, () -> orderService.cancelOrder(orderId));
	}

}
