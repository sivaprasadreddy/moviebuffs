package com.sivalabs.moviebuffs.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Set;

@Setter
@Getter
@Entity
@Table(name = "orders")
public class Order {

    @Id
    @SequenceGenerator(name = "order_id_generator", sequenceName = "order_id_seq", allocationSize = 1)
    @GeneratedValue(generator = "order_id_generator")
    private Long id;
    private String orderId;
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "order")
    private Set<OrderItem> items;
    private String customerName;
    private String customerEmail;
    private String deliveryAddress;
    private String creditCardNumber;
    private String cvv;
    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    public enum OrderStatus {
        NEW, DELIVERED, CANCELLED, ERROR
    }
}
