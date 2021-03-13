package com.sivalabs.moviebuffs.core.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.math.BigDecimal;
import javax.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
@Table(name = "order_items")
@EqualsAndHashCode(
        exclude = {"order"},
        callSuper = false)
public class OrderItem {

    @Id
    @SequenceGenerator(
            name = "order_item_id_generator",
            sequenceName = "order_item_id_seq",
            allocationSize = 1)
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

    public BigDecimal getSubTotal() {
        return productPrice.multiply(new BigDecimal(quantity));
    }
}
