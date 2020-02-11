package com.sivalabs.moviebuffs.repository;

import com.sivalabs.moviebuffs.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long> {

    Optional<Order> findByOrderId(String orderId);

    List<Order> findByStatus(Order.OrderStatus status);
}
