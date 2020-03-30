package com.sivalabs.moviebuffs.datafactory;

import com.sivalabs.moviebuffs.core.entity.*;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.sivalabs.moviebuffs.importer.mappers.CsvRowMapperUtils.toSlug;

public class TestDataFactory {

	public static User createUser() {
		String uuid = UUID.randomUUID().toString();
		return createUser(uuid + "@gmail.com", uuid);
	}

	public static User createUser(String email) {
		String uuid = UUID.randomUUID().toString();
		return createUser(email, uuid);
	}

	public static User createUser(String email, String password) {
		User user = new User();
		user.setName("someuser");
		user.setEmail(email);
		user.setPassword(password);
		return user;
	}

	public static Movie createMovie(String title, String... genreNames) {
		Movie movie = new Movie();
		movie.setTitle(title);
		movie.setGenres(Arrays.stream(genreNames).map(g -> {
			Genre genre = new Genre();
			genre.setName(g);
			genre.setSlug(toSlug(g));
			return genre;
		}).collect(Collectors.toSet()));
		return movie;
	}

	public static Order createOrder(Long id) {
		Order order = new Order();
		order.setId(id);
		order.setOrderId(UUID.randomUUID().toString());
		order.setStatus(Order.OrderStatus.NEW);
		order.setCustomerName("customer 1");
		order.setCustomerEmail("customer1@gmail.com");
		order.setCreditCardNumber("1111111111111");
		order.setCvv("123");
		order.setDeliveryAddress("Hyderabad");

		Set<OrderItem> items = new HashSet<>();
		items.add(createOrderItem(order));
		order.setItems(items);

		User createdBy = new User();
		createdBy.setId(1L);
		order.setCreatedBy(createdBy);
		return order;
	}

	public static OrderItem createOrderItem(Order order) {
		OrderItem item = new OrderItem();
		item.setProductCode("P001");
		item.setProductName("Some Product");
		item.setProductPrice(BigDecimal.TEN);
		item.setQuantity(1);
		item.setOrder(order);
		return item;
	}

}
