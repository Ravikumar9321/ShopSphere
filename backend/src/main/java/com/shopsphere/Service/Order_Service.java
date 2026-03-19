package com.shopsphere.Service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.mapping.PropertyReferenceException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.shopsphere.DAO.CartItem_dao;
import com.shopsphere.DAO.Order_dao;
import com.shopsphere.DAO.Product_dao;
import com.shopsphere.DTO.Orderstatus;
import com.shopsphere.DTO.ResponseStructure;
import com.shopsphere.Entity.CartItem;
import com.shopsphere.Entity.Order;
import com.shopsphere.Entity.OrderItem;
import com.shopsphere.Entity.Product;
import com.shopsphere.Exception.InvalidRequestException;
import com.shopsphere.Exception.ProductNotFoundException;
import com.shopsphere.Exception.ResourceNotFoundException;

@Service
public class Order_Service {
	@Autowired
	private CartItem_dao cdao;
	@Autowired
	private Product_dao productdao;
	@Autowired
	private Order_dao orderdao;

	private <T> ResponseEntity<ResponseStructure<T>> buildResponse(HttpStatus status, T data, String message) {
		ResponseStructure<T> response = new ResponseStructure<T>();
		response.setStatusCode(status.value());
		response.setData(data);
		response.setMessage(message);
		return ResponseEntity.status(status).body(response);
	}

	@Transactional
	public ResponseEntity<ResponseStructure<Order>> createOrder(String sessionId, Order order) {
		List<CartItem> cartItems = cdao.getCartitemBysessionId(sessionId);
		if (cartItems.isEmpty())
			throw new InvalidRequestException("No items found in cart for session: " + sessionId);

		List<OrderItem> orderItems = new ArrayList<OrderItem>();

		double totalPrice = 0.0;

		for (CartItem item : cartItems) {
			Product product = productdao.fetchProductById(item.getProduct().getId())
					.orElseThrow(() -> new ProductNotFoundException("Product not found: " + item.getProduct().getId()));

			OrderItem orderitem = new OrderItem();
			orderitem.setProduct(product);
			orderitem.setPrice(item.getProduct().getPrice());
			orderitem.setQuantity(item.getQuantity());
			// add orderitem
			orderItems.add(orderitem);

			totalPrice += orderitem.getPrice() * item.getQuantity();
			product.setStockQuantity(product.getStockQuantity() - item.getQuantity());
			productdao.updateProduct(product);

		}
		order.setTotalAmount(totalPrice);
		order.setStatus(Orderstatus.PENDING);
		order.setOrderItems(orderItems);
		order.setSessionId(sessionId);

		Order savedOrder = orderdao.createOrder(order);
		for (OrderItem oi : orderItems) {
			oi.setOrder(savedOrder);
		}
		cdao.deleteCartItems(cartItems);

		return buildResponse(HttpStatus.CREATED, savedOrder, "Order placed successfully");
	}

	// 3)
	public ResponseEntity<ResponseStructure<List<Order>>> fetchallOrder() {
		List<Order> list = orderdao.fetchAllOrder();
		if (list.size() <= 0)
			throw new ResourceNotFoundException("Order  Empty");
		return buildResponse(HttpStatus.OK, list, "Order Details Retrieved");
	}

	// 4)
	public ResponseEntity<ResponseStructure<Order>> fetchOrderById(int id) {
		Order order = orderdao.fetchOrderById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Order details does not exist"));

		return buildResponse(HttpStatus.OK, order, "Order details retrieved based on id");
	}

	// 5)
	public ResponseEntity<ResponseStructure<Order>> updateOrder(Order Order) {

		if (Order.getId() == null)
			throw new InvalidRequestException("please enter Order id to update");
		orderdao.fetchOrderById(Order.getId()).orElseThrow(() -> new ResourceNotFoundException("Order not found"));

		return buildResponse(HttpStatus.OK, orderdao.createOrder(Order), "successfuly updated");
	}

	// 6
	public ResponseEntity<ResponseStructure<String>> deleteOrder(int id) {
		orderdao.fetchOrderById(id).orElseThrow(() -> new ResourceNotFoundException("Order does not exist"));

		orderdao.deleteOrder(id);

		return buildResponse(HttpStatus.OK, "SUCCESS", "Deleted successfully");
	}

	// 7
	public ResponseEntity<ResponseStructure<Page<Order>>> getOrderByPagination_Sort(int pageNumber, int pageSize,
			String field) {
		try {
			Page<Order> page = orderdao.getOrderByPagination_Sort(pageNumber, pageSize, field);
			if (page.isEmpty()) {
				return buildResponse(HttpStatus.OK, page, "No categories found for this page");
			}
			return buildResponse(HttpStatus.OK, page, "Success");

		} catch (IllegalArgumentException | PropertyReferenceException e) {
			throw new InvalidRequestException("Invalid sort field. Allowed: id,totalAmount,status");
		}
	}

	public ResponseEntity<ResponseStructure<List<Order>>> getOrderDetailsBySessionId(String sessionId) {
		List<Order> list = orderdao.getOrderDetailsBySessionId(sessionId);

		if (list.size() <= 0)
			throw new ResourceNotFoundException("Order details not found with session " + sessionId);
		return buildResponse(HttpStatus.OK, list, "Retrieved Successfully");
	}

}
