package com.sivalabs.moviebuffs.models;

import com.sivalabs.moviebuffs.entity.Order;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class OrderConfirmation {
    private String orderId;
    private Order.OrderStatus orderStatus;
}
