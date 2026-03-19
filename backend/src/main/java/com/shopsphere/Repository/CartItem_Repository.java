package com.shopsphere.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.shopsphere.Entity.CartItem;

public interface CartItem_Repository extends JpaRepository<CartItem, Integer> {
	@Query("select c.cartItems from Cart c where c.sessionId=?1")
	List<CartItem> getCartitemBysessionId(String sessionId);

}
