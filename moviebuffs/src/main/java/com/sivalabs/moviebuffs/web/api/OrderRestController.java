package com.sivalabs.moviebuffs.web.api;

import com.sivalabs.moviebuffs.core.entity.Order;
import com.sivalabs.moviebuffs.core.exception.BadRequestException;
import com.sivalabs.moviebuffs.core.model.OrderConfirmationDTO;
import com.sivalabs.moviebuffs.core.service.OrderService;
import com.sivalabs.moviebuffs.core.service.SecurityService;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
@Slf4j
@PreAuthorize("hasRole('ROLE_USER')")
public class OrderRestController {

	private final OrderService orderService;

	private final SecurityService securityService;

	private final Counter ordersCounter;

	public OrderRestController(MeterRegistry registry, OrderService orderService, SecurityService securityService) {
		this.ordersCounter = registry.counter("counter.orders");
		this.orderService = orderService;
		this.securityService = securityService;
	}

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public OrderConfirmationDTO createOrder(@RequestBody Order order) {
		order.setCreatedBy(securityService.loginUser());
		OrderConfirmationDTO confirmation = orderService.createOrder(order);
		ordersCounter.increment();
		return confirmation;
	}

	@GetMapping
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public List<Order> getAllOrders() {
		return this.orderService.findAllOrders();
	}

	@GetMapping("/{orderId}")
	public ResponseEntity<Order> getOrderByOrderId(@PathVariable String orderId) {
		log.info("Getting order by id: {}", orderId);
		return orderService.findOrderByOrderId(orderId)
				.filter(order -> securityService.isCurrentUserHasPrivilege(order.getCreatedBy().getId()))
				.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
	}

	@DeleteMapping("/{orderId}")
	public ResponseEntity<String> cancelOrder(@PathVariable String orderId) {
		log.info("Cancelling order by id: {}", orderId);
		return orderService.findOrderByOrderId(orderId).map(order -> {
			if (!securityService.isCurrentUserHasPrivilege(order.getCreatedBy().getId())) {
				throw new BadRequestException("You can't mess with other user order");
			}
			this.orderService.cancelOrder(orderId);
			log.info("Cancelled order by id: {}", orderId);
			return ResponseEntity.ok("success");
		}).orElseGet(() -> ResponseEntity.notFound().build());
	}

}
