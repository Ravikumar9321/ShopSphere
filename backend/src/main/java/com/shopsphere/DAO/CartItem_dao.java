package com.shopsphere.DAO;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.shopsphere.Entity.CartItem;
import com.shopsphere.Repository.CartItem_Repository;

@Repository
public class CartItem_dao {
	@Autowired
	private CartItem_Repository repository;

	public CartItem addCart(CartItem cartitem) {

		return repository.save(cartitem);
	}

	public List<CartItem> fetchallCartItem() {
		return repository.findAll();
	}

	public Optional<CartItem> fetchCartItemById(int id) {
		return repository.findById(id);

	}

	public void deleteCartItem(int id) {
		repository.deleteById(id);

	}

	public void deleteCartItems(List<CartItem> cartItems) {
		repository.deleteAll(cartItems);

	}

	public List<CartItem> getCartitemBysessionId(String sessionId) {
		return repository.getCartitemBysessionId(sessionId);

	}

}
