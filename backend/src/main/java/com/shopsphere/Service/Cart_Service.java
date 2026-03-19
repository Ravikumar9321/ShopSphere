package com.shopsphere.Service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.mapping.PropertyReferenceException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.shopsphere.DAO.Cart_dao;
import com.shopsphere.DTO.ResponseStructure;
import com.shopsphere.Entity.Cart;
import com.shopsphere.Exception.CartNotFoundException;
import com.shopsphere.Exception.InvalidRequestException;

@Service
public class Cart_Service {

	@Autowired
	private Cart_dao cartdao;

	private <T> ResponseEntity<ResponseStructure<T>> buildResponse(HttpStatus status, T data, String message) {
		ResponseStructure<T> response = new ResponseStructure<T>();
		response.setStatusCode(status.value());
		response.setData(data);
		response.setMessage(message);
		return new ResponseEntity<>(response, status);
	}

	public ResponseEntity<ResponseStructure<Cart>> createCart(Cart cart) {
		String sessionId = cart.getSessionId();
		boolean match = cartdao.fetchAllCart().stream().anyMatch(c -> sessionId.equals(c.getSessionId()));
		if (match)
			throw new DataIntegrityViolationException("session id must be unique");

		if (sessionId == null || sessionId.trim().isEmpty() || sessionId.length() > 20) {
			throw new InvalidRequestException("SessionId must be 1-20 characters long");
		}

		return buildResponse(HttpStatus.CREATED, cartdao.createCart(cart),
				" Cart Added Sucessfully with " + cart.getSessionId());
	}

	public ResponseEntity<ResponseStructure<List<Cart>>> fetchallCart() {
		List<Cart> list = cartdao.fetchAllCart();
		if (list.size() == 0)
			throw new CartNotFoundException("Cart  Empty");
		return buildResponse(HttpStatus.OK, list, "Cart Details Retrieved");
	}

	public ResponseEntity<ResponseStructure<Cart>> fetchCartById(int id) {
		Cart cart = cartdao.fetchCartById(id)
				.orElseThrow(() -> new CartNotFoundException("Cart details does not exist"));

		return buildResponse(HttpStatus.OK, cart, "Cart details retrieved based on id");
	}

	public ResponseEntity<ResponseStructure<Cart>> updateCart(Cart cart) {
		if (cart.getId() == null)
			throw new InvalidRequestException("please enter Cart id to update");
		cartdao.fetchCartById(cart.getId()).orElseThrow(() -> new CartNotFoundException("Cart not found"));

		return buildResponse(HttpStatus.OK, cartdao.createCart(cart), "successfuly updated");
	}

	public ResponseEntity<ResponseStructure<String>> deleteCart(int id) {
		Optional<Cart> optional = cartdao.fetchCartById(id);
		if (!optional.isPresent())
			throw new CartNotFoundException("Cart does not exist");
		cartdao.deleteCart(id);

		return buildResponse(HttpStatus.OK, "SUCCESS", "Deleted successfully");
	}

	public ResponseEntity<ResponseStructure<Page<Cart>>> getCartByPagination_Sort(int pageNumber, int pageSize,
			String field) {
		try {
			Page<Cart> page = cartdao.getCartByPagination_Sort(pageNumber, pageSize, field);
			if (page.isEmpty()) {
				return buildResponse(HttpStatus.OK, page, "No categories found for this page");
			}
			return buildResponse(HttpStatus.OK, page, "Success");

		} catch (IllegalArgumentException | PropertyReferenceException e) {
			throw new InvalidRequestException("Invalid sort field. Allowed: id,name");
		}
	}

	public ResponseEntity<ResponseStructure<Cart>> fetchCartBySessionId(String sessionId) {
		Cart cart = cartdao.fetchCartBySessionId(sessionId)
				.orElseThrow(() -> new CartNotFoundException("Cart  does not exist with session  " + sessionId));

		return buildResponse(HttpStatus.OK, cart, "Cart details retrieved");
	}
}
