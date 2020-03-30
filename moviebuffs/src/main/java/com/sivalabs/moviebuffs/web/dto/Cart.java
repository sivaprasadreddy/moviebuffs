package com.sivalabs.moviebuffs.web.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class Cart {

	@Setter
	@Getter
	private List<LineItem> items;

	public Cart() {
		items = new ArrayList<>();
	}

	public void addItem(MovieDTO product) {
		for (LineItem lineItem : items) {
			if (lineItem.getProduct().getId().equals(product.getId())) {
				lineItem.setQuantity(lineItem.getQuantity() + 1);
				return;
			}
		}
		LineItem item = new LineItem(product, 1);
		this.items.add(item);
	}

	public void updateItemQuantity(MovieDTO product, int quantity) {
		for (LineItem lineItem : items) {
			if (lineItem.getProduct().getId().equals(product.getId())) {
				lineItem.setQuantity(quantity);
			}
		}
	}

	public void removeItem(Long id) {
		LineItem item = null;
		for (LineItem lineItem : items) {
			if (lineItem.getProduct().getId().equals(id)) {
				item = lineItem;
				break;
			}
		}
		if (item != null) {
			items.remove(item);
		}
	}

	public void clearItems() {
		items = new ArrayList<>();
	}

	public int getItemCount() {
		int count = 0;
		for (LineItem lineItem : items) {
			count += lineItem.getQuantity();
		}
		return count;
	}

	public BigDecimal getTotalAmount() {
		BigDecimal amount = new BigDecimal("0.0");
		for (LineItem lineItem : items) {
			amount = amount.add(lineItem.getSubTotal());
		}
		return amount;
	}

}
