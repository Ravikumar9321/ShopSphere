package com.shopsphere.DAO;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import com.shopsphere.Entity.Cart;
import com.shopsphere.Repository.Cart_Repository;

@Repository
public class Cart_dao {

	@Autowired
	private Cart_Repository cartRepo;

	public Cart createCart(Cart cart) {
		return cartRepo.save(cart);
	}

	public List<Cart> fetchAllCart() {
		return cartRepo.findAll();

	}

	public Optional<Cart> fetchCartById(int id) {
		return cartRepo.findById(id);

	}

	public void deleteCart(int id) {
		cartRepo.deleteById(id);

	}

	public Page<Cart> getCartByPagination_Sort(int pageNumber, int pageSize, String field) {
		return cartRepo.findAll(PageRequest.of(pageNumber, pageSize, Sort.by(field)));

	}

	public Optional<Cart> fetchCartBySessionId(String sessionId) {
		return cartRepo.fetchCartBySessionId(sessionId);

	}

}
