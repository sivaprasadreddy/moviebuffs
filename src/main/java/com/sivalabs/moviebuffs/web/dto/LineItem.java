package com.sivalabs.moviebuffs.web.dto;

import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class LineItem {

    private MovieDTO product;

    private int quantity;

    public BigDecimal getSubTotal() {
        return product.getPrice().multiply(new BigDecimal(quantity));
    }
}
