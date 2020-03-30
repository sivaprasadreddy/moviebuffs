package com.sivalabs.moviebuffs.web.controller;

import com.sivalabs.moviebuffs.core.entity.Order;
import com.sivalabs.moviebuffs.core.entity.OrderItem;
import com.sivalabs.moviebuffs.core.entity.User;
import com.sivalabs.moviebuffs.core.model.OrderConfirmationDTO;
import com.sivalabs.moviebuffs.core.service.MovieService;
import com.sivalabs.moviebuffs.core.service.OrderService;
import com.sivalabs.moviebuffs.core.service.SecurityService;
import com.sivalabs.moviebuffs.web.dto.Cart;
import com.sivalabs.moviebuffs.web.dto.LineItem;
import com.sivalabs.moviebuffs.web.dto.MovieDTO;
import com.sivalabs.moviebuffs.web.dto.OrderDTO;
import com.sivalabs.moviebuffs.web.mappers.MovieDTOMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.*;

@Controller
@RequiredArgsConstructor
public class CartController extends BaseController {

	private final MovieService movieService;

	private final SecurityService securityService;

	private final OrderService orderService;

	private final MovieDTOMapper movieDTOMapper;

	@GetMapping(value = "/cart")
	public String showCart(HttpServletRequest request, Model model) {
		Cart cart = getOrCreateCart(request);
		model.addAttribute("cart", cart);
		return "cart";
	}

	@GetMapping(value = "/cart/checkout")
	public String checkout(@Valid @ModelAttribute("order") OrderDTO order, BindingResult result, Model model,
			RedirectAttributes redirectAttributes, HttpServletRequest request) {
		Cart cart = getOrCreateCart(request);
		if (result.hasErrors()) {
			model.addAttribute("cart", cart);
			model.addAttribute("order", order);
			return "cart";
		}
		User user = securityService.loginUser();
		Order newOrder = new Order();
		newOrder.setCustomerName(order.getCustomerName());
		newOrder.setCustomerEmail(order.getCustomerEmail());
		newOrder.setStatus(Order.OrderStatus.NEW);
		newOrder.setOrderId(UUID.randomUUID().toString());
		newOrder.setCreditCardNumber(order.getCreditCardNumber());
		newOrder.setCvv(order.getCvv());
		newOrder.setDeliveryAddress(order.getDeliveryAddress());
		Set<OrderItem> items = new HashSet<>();
		for (LineItem lineItem : cart.getItems()) {
			OrderItem orderItem = new OrderItem();
			orderItem.setOrder(newOrder);
			orderItem.setProductCode(lineItem.getProduct().getImdbId());
			orderItem.setProductName(lineItem.getProduct().getTitle());
			orderItem.setProductPrice(lineItem.getProduct().getPrice());
			orderItem.setQuantity(lineItem.getQuantity());
			items.add(orderItem);
		}
		newOrder.setItems(items);
		newOrder.setCreatedBy(user);

		OrderConfirmationDTO orderConfirmation = orderService.createOrder(newOrder);
		redirectAttributes.addFlashAttribute("orderConfirmation", orderConfirmation);

		request.getSession().removeAttribute("CART_KEY");

		return "redirect:/orders/" + orderConfirmation.getOrderId();
	}

	@GetMapping(value = "/orders/{orderId}")
	public String viewOrder(@PathVariable(value = "orderId") String orderId, Model model) {
		Order order = orderService.findOrderByOrderId(orderId).orElse(null);
		model.addAttribute("order", order);
		return "order";
	}

	@GetMapping(value = "/cart/items/count")
	@ResponseBody
	public Map<String, Object> getCartItemCount(HttpServletRequest request) {
		Cart cart = getOrCreateCart(request);
		int itemCount = cart.getItemCount();
		Map<String, Object> map = new HashMap<>();
		map.put("count", itemCount);
		return map;
	}

	@PostMapping(value = "/cart/items")
	@ResponseBody
	public Cart addToCart(@RequestBody MovieDTO product, HttpServletRequest request) {
		Cart cart = getOrCreateCart(request);
		MovieDTO p = movieService.findMovieById(product.getId()).map(movieDTOMapper::map).orElse(null);
		cart.addItem(p);
		return cart;
	}

	@PutMapping(value = "/cart/items")
	@ResponseBody
	public Cart updateCartItem(@RequestBody LineItem item, HttpServletRequest request, HttpServletResponse response) {
		Cart cart = getOrCreateCart(request);
		if (item.getQuantity() <= 0) {
			Long sku = item.getProduct().getId();
			cart.removeItem(sku);
		}
		else {
			cart.updateItemQuantity(item.getProduct(), item.getQuantity());
		}
		return cart;
	}

	@DeleteMapping(value = "/cart/items/{id}")
	@ResponseBody
	public Cart removeCartItem(@PathVariable("id") Long id, HttpServletRequest request) {
		Cart cart = getOrCreateCart(request);
		cart.removeItem(id);
		return cart;
	}

	@DeleteMapping(value = "/cart")
	@ResponseBody
	public Cart clearCart(HttpServletRequest request) {
		Cart cart = getOrCreateCart(request);
		cart.setItems(new ArrayList<>());
		return cart;
	}

}
