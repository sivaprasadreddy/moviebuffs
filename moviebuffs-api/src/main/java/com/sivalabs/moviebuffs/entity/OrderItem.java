package com.sivalabs.moviebuffs.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;

@Setter
@Getter
@Entity
@Table(name = "order_items")
@EqualsAndHashCode(exclude = {"order"}, callSuper = false)
public class OrderItem {
    @Id
    @SequenceGenerator(name = "order_item_id_generator", sequenceName = "order_item_id_seq", allocationSize = 1)
    @GeneratedValue(generator = "order_item_id_generator")
    private Long id;
    private String productCode;
    private String productName;
    private BigDecimal productPrice;
    private Integer quantity;
    @ManyToOne(optional = false)
    @JoinColumn(name = "order_id")
    @JsonIgnore
    private Order order;
}
