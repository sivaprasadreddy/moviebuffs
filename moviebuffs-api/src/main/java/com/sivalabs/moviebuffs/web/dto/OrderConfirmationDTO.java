package com.sivalabs.moviebuffs.web.dto;

import com.sivalabs.moviebuffs.entity.Order;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class OrderConfirmationDTO {
    private String orderId;
    private Order.OrderStatus orderStatus;
}
