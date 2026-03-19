package com.shopsphere.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.shopsphere.Entity.Cart;

public interface Cart_Repository extends JpaRepository<Cart, Integer> {

	@Query("select c from Cart c where c.sessionId=?1")
	Optional<Cart> fetchCartBySessionId(String sessionId);

}
