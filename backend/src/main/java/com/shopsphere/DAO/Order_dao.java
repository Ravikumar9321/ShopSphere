package com.shopsphere.DAO;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import com.shopsphere.Entity.Order;
import com.shopsphere.Repository.Order_Repository;

@Repository
public class Order_dao {
	@Autowired
	private Order_Repository repository;

	public Order createOrder(Order order) {
		return repository.save(order);

	}

	public List<Order> fetchAllOrder() {
		return repository.findAll();

	}

	public Optional<Order> fetchOrderById(int id) {
		return repository.findById(id);

	}

	public void deleteOrder(int id) {
		repository.deleteById(id);

	}

	public Page<Order> getOrderByPagination_Sort(int pageNumber, int pageSize, String field) {
		return repository.findAll(PageRequest.of(pageNumber, pageSize, Sort.by(field)));

	}

	public List<Order> getOrderDetailsBySessionId(String sessionId) {
		return repository.getOrderDetailsBySessionId(sessionId);

	}

}
