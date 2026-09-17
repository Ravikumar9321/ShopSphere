package com.shopsphere.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
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
import com.shopsphere.Entity.CartItem;
import com.shopsphere.Service.CartItem_Service;

import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/cartitem")
@CrossOrigin(origins = "http://localhost:3000")
@Tag(name = "CartItem", description = "CartItem related APIs")

public class CartItem_Controller {

	@Autowired
	private CartItem_Service service;

	// 1)add Cart
	@PostMapping("/{sessionId}")
	public ResponseEntity<ResponseStructure<CartItem>> AddCart(@PathVariable String sessionId,
			@RequestBody CartItem cartitem) {
		return service.addCart(sessionId, cartitem);
	}

	// 2)fetch all CartItem details
	@GetMapping
	public ResponseEntity<ResponseStructure<List<CartItem>>> fetchallCartItemDetails() {
		return service.fetchallCartItem();
	}

	// 3)fetch CartItem details by Id
	@GetMapping("/{id}")
	public ResponseEntity<ResponseStructure<CartItem>> fetchCartItemDetailsById(@PathVariable int id) {
		return service.fetchCartItemById(id);
	}

	// 4)update CartItem details
	@PutMapping
	public ResponseEntity<ResponseStructure<CartItem>> updateCartItemDetails(@RequestBody CartItem cartItem) {
		return service.updateCartItem(cartItem);
	}

	// 5)delete CartItem detail
	@DeleteMapping("/{id}")
	public ResponseEntity<ResponseStructure<String>> deleteCartItemDetails(@PathVariable int id) {
		return service.deleteCartItem(id);
	}

	// 6)get cartItem by sessionId
	@GetMapping("/sessionId/{sessionId}")
	public ResponseEntity<ResponseStructure<List<CartItem>>> getCartitemBysessionId(@PathVariable String sessionId) {
		return service.getCartitemBysessionId(sessionId);
	}

}
