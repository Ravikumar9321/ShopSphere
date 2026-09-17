package com.shopsphere.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.shopsphere.DTO.ResponseStructure;
import com.shopsphere.Entity.Cart;
import com.shopsphere.Service.Cart_Service;

import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/cart")
@CrossOrigin(origins = "http://localhost:3000")
@Tag(name = "Cart", description = "Cart related APIs")

public class Cart_Controller {
	@Autowired
	private Cart_Service service;

	// 1) save details
	@PostMapping
	public ResponseEntity<ResponseStructure<Cart>> saveCartDetails(@RequestBody Cart cart) {
		return service.createCart(cart);
	}

	// 2)fetch all Cart details
	@GetMapping
	public ResponseEntity<ResponseStructure<List<Cart>>> fetchallCartDetails() {
		return service.fetchallCart();
	}

	// 3)fetch Cart details by Id
	@GetMapping("/{id}")
	public ResponseEntity<ResponseStructure<Cart>> fetchCartDetailsById(@PathVariable int id) {
		return service.fetchCartById(id);
	}

	// 4)update Cart details
	@PutMapping
	public ResponseEntity<ResponseStructure<Cart>> updateCartDetails(@RequestBody Cart cart) {
		return service.updateCart(cart);
	}

	// 5)delete Cart detail
	@DeleteMapping("/{id}")
	public ResponseEntity<ResponseStructure<String>> deleteCartDetails(@PathVariable int id) {
		return service.deleteCart(id);
	}

	// 6)Cart details in pagination and sort format
	@GetMapping("/{pageNumber}/{pageSize}/{field}")
	public ResponseEntity<ResponseStructure<Page<Cart>>> getCartDetailsByPagenation_Sort(@PathVariable int pageNumber,
			@PathVariable int pageSize, @PathVariable String field) {
		return service.getCartByPagination_Sort(pageNumber, pageSize, field);
	}

	// 7)fetch Cart details by Id
	@GetMapping("/sessionId/{sessionId}")
	public ResponseEntity<ResponseStructure<Cart>> fetchCartBySessionId(@PathVariable String sessionId) {
		return service.fetchCartBySessionId(sessionId);
	}
}
