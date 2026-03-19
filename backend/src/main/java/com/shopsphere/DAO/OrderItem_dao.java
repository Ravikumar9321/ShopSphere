package com.shopsphere.DAO;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import com.shopsphere.Entity.OrderItem;
import com.shopsphere.Repository.OrderItem_Repository;

@Repository
public class OrderItem_dao {
	@Autowired
	private OrderItem_Repository repository;

	public OrderItem creatOrderItem(OrderItem OrderItem) {
		return repository.save(OrderItem);

	}

	public List<OrderItem> fetchAllOrderItem() {
		return repository.findAll();

	}

	public Optional<OrderItem> fetchOrderItemById(int id) {
		return repository.findById(id);

	}

	public void deleteOrderItem(int id) {
		repository.deleteById(id);

	}

	public Page<OrderItem> getOrderItemByPagination_Sort(int pageNumber, int pageSize, String field) {
		return repository.findAll(PageRequest.of(pageNumber, pageSize, Sort.by(field).ascending()));

	}

	public List<OrderItem> getOrderItemsByOrder(String orderId) {
		return repository.getOrderItemsByOrder(orderId);

	}

}
