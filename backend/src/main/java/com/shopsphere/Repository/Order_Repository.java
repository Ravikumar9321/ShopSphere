package com.shopsphere.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.shopsphere.Entity.Order;

public interface Order_Repository extends JpaRepository<Order, Integer> {

	@Query("SELECT o FROM Order o WHERE o.sessionId = ?1")
	List<Order> getOrderDetailsBySessionId(String sessionId);

}
