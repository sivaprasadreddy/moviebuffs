package com.sivalabs.moviebuffs.core.model;

import com.sivalabs.moviebuffs.core.entity.Order;
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
