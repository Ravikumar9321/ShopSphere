package com.shopsphere.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.shopsphere.Entity.OrderItem;

public interface OrderItem_Repository extends JpaRepository<OrderItem, Integer> {

	@Query("select o.orderItems FROM Order o WHERE o.id=?1")
	List<OrderItem> getOrderItemsByOrder(String orderId);

}
