package com.sivalabs.moviebuffs.core.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.math.BigDecimal;
import java.util.Set;
import javax.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
@Table(name = "orders")
@EqualsAndHashCode(
        of = {"id"},
        callSuper = false)
public class Order {

    @Id
    @SequenceGenerator(
            name = "order_id_generator",
            sequenceName = "order_id_seq",
            allocationSize = 1)
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

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by")
    private User createdBy;

    public enum OrderStatus {
        NEW,
        DELIVERED,
        CANCELLED,
        ERROR
    }

    public BigDecimal getTotalAmount() {
        BigDecimal amount = new BigDecimal("0.0");
        for (OrderItem orderItem : items) {
            amount = amount.add(orderItem.getSubTotal());
        }
        return amount;
    }
}
