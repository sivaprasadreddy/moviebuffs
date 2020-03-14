package com.sivalabs.moviebuffs.web.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import java.io.Serializable;

@Setter
@Getter
public class OrderDTO implements Serializable
{
    private static final long serialVersionUID = 1L;

    @NotEmpty(message="Customer Name is required")
    private String customerName;

    @NotEmpty(message="Customer email is required")
    @Email
    private String customerEmail;

    @NotEmpty(message="DeliveryAddress is required")
    private String deliveryAddress;

    @NotEmpty(message="Credit Card Number is required")
    private String creditCardNumber;

    @NotEmpty(message="CVV is required")
    private String cvv;

}
