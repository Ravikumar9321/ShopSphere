package com.shopsphere.Service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.shopsphere.DAO.CartItem_dao;
import com.shopsphere.DAO.Cart_dao;
import com.shopsphere.DAO.Product_dao;
import com.shopsphere.DTO.ResponseStructure;
import com.shopsphere.Entity.Cart;
import com.shopsphere.Entity.CartItem;
import com.shopsphere.Entity.Product;
import com.shopsphere.Exception.CartItemNotFoundException;
import com.shopsphere.Exception.CartNotFoundException;
import com.shopsphere.Exception.InvalidRequestException;
import com.shopsphere.Exception.ProductNotFoundException;
import com.shopsphere.Exception.ResourceNotFoundException;

@Service
public class CartItem_Service {
	@Autowired
	private CartItem_dao cdao;

	@Autowired
	private Product_dao productdao;
	@Autowired
	private Cart_dao cartdao;

	private void validateCartQuantity(Integer quantity, Integer stockQuantity) {
		Optional.ofNullable(quantity).filter(q -> q >= 1 && q <= 10)
				.orElseThrow(() -> new InvalidRequestException("Quantity must be between 1-10"));

		if (quantity > stockQuantity) {
			throw new InvalidRequestException("Not enough stock. Available: " + stockQuantity);
		}
	}

	private <T> ResponseEntity<ResponseStructure<T>> buildResponse(HttpStatus status, T data, String message) {
		ResponseStructure<T> response = new ResponseStructure<T>();
		response.setStatusCode(status.value());
		response.setData(data);
		response.setMessage(message);
		return ResponseEntity.status(status).body(response);
	}

	public ResponseEntity<ResponseStructure<CartItem>> addCart(String sessionId, CartItem cartItem) {

		Cart cart = cartdao.fetchCartBySessionId(sessionId)
				.orElseThrow(() -> new CartNotFoundException("Enter the registered session id"));

		Product product = productdao.fetchProductById(cartItem.getProduct().getId())
				.orElseThrow(() -> new ProductNotFoundException("Product not found"));

		validateCartQuantity(cartItem.getQuantity(), product.getStockQuantity());
		// Reduce stock capacity
	    int newStock = product.getStockQuantity() - cartItem.getQuantity();
	    product.setStockQuantity(newStock);
	    productdao.updateProduct(product);
	    
		cartItem.setProduct(product);
		cartItem.setCart(cart);

		CartItem savedCartItem = cdao.addCart(cartItem);

		return buildResponse(HttpStatus.CREATED, savedCartItem, "Product added with session " + sessionId);
	}

	public ResponseEntity<ResponseStructure<List<CartItem>>> fetchallCartItem() {
		List<CartItem> list = cdao.fetchallCartItem();
		if (list.size() <= 0)
			throw new CartItemNotFoundException("CartItem Empty");

		return buildResponse(HttpStatus.OK, list, "Retrieved successfully");
	}

	public ResponseEntity<ResponseStructure<CartItem>> fetchCartItemById(int id) {
		CartItem cartitem = cdao.fetchCartItemById(id)
				.orElseThrow(() -> new CartItemNotFoundException("Details does not exist"));

		return buildResponse(HttpStatus.OK, cartitem, "Succesfully retrieved");
	}

	public ResponseEntity<ResponseStructure<CartItem>> updateCartItem(CartItem cartItem) {
		if (cartItem.getId() == null)
			throw new InvalidRequestException("cartitem id is mandatory to update");

		cdao.fetchCartItemById(cartItem.getId())
				.orElseThrow(() -> new ResourceNotFoundException("Details does not exist"));

		return buildResponse(HttpStatus.OK, cartItem, "Succesfully retrieved");

	}

	public ResponseEntity<ResponseStructure<String>> deleteCartItem(int id) {
		cdao.fetchCartItemById(id).orElseThrow(() -> new ResourceNotFoundException("Details does not exist"));
		cdao.deleteCartItem(id);
		return buildResponse(HttpStatus.OK, "SUCCESS", "Deleted");
	}

	public ResponseEntity<ResponseStructure<List<CartItem>>> getCartitemBysessionId(String sessionId) {
		List<CartItem> list = cdao.getCartitemBysessionId(sessionId);
		if (list.stream().findAny().isEmpty()) {
			throw new CartItemNotFoundException("Cart not found for session: " + sessionId);
		}
		return buildResponse(HttpStatus.OK, list, "Retrieved successfully");
	}

}
