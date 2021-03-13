package com.sivalabs.moviebuffs.core.repository;

import com.sivalabs.moviebuffs.core.entity.Order;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {

    Optional<Order> findByOrderId(String orderId);

    List<Order> findByStatus(Order.OrderStatus status);
}
