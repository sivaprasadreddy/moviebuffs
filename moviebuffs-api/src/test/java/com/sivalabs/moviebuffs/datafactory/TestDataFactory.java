package com.sivalabs.moviebuffs.datafactory;

import com.sivalabs.moviebuffs.entity.Order;
import com.sivalabs.moviebuffs.entity.OrderItem;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class TestDataFactory {

    public static Order createOrder(Long id) {
        Order order = new Order();
        order.setId(id);
        order.setOrderId(UUID.randomUUID().toString());
        order.setStatus(Order.OrderStatus.NEW);
        order.setCustomerName("customer 1");
        order.setCustomerEmail("customer1@gmail.com");
        order.setCreditCardNumber("1111111111111");
        order.setCvv("123");
        order.setDeliveryAddress("Hyderabad");

        Set<OrderItem> items = new HashSet<>();
        items.add(createOrderItem(order));
        order.setItems(items);
        return order;
    }

    public static OrderItem createOrderItem(Order order) {
        OrderItem item = new OrderItem();
        item.setProductCode("P001");
        item.setProductPrice(BigDecimal.TEN);
        item.setQuantity(1);
        item.setOrder(order);
        return item;
    }
}
