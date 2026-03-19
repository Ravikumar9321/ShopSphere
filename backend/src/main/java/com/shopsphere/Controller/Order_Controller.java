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
import com.shopsphere.Entity.Order;
import com.shopsphere.Service.Order_Service;

@RestController
@RequestMapping("/api/order")
@CrossOrigin(origins = "http://localhost:3000")
public class Order_Controller {

	@Autowired
	private Order_Service service;

	// 1)
	@PostMapping("/{sessionId}")
	public ResponseEntity<ResponseStructure<Order>> createOrder(@PathVariable String sessionId,
			@RequestBody Order order) {
		return service.createOrder(sessionId, order);
	}

	// 2)fetch all Order details
	@GetMapping
	public ResponseEntity<ResponseStructure<List<Order>>> fetchallOrderDetails() {
		return service.fetchallOrder();
	}

	// 3)fetch Order details by Id
	@GetMapping("/{id}")
	public ResponseEntity<ResponseStructure<Order>> fetchOrderDetailsById(@PathVariable int id) {
		return service.fetchOrderById(id);
	}

	// 4)update Order details
	@PutMapping
	public ResponseEntity<ResponseStructure<Order>> updateOrderDetails(@RequestBody Order order) {
		return service.updateOrder(order);
	}

	// 5)delete Order detail
	@DeleteMapping("/{id}")
	public ResponseEntity<ResponseStructure<String>> deleteOrderDetails(@PathVariable int id) {
		return service.deleteOrder(id);
	}

	// 6)Order details in pagination and sort format
	@GetMapping("/{pageNumber}/{pageSize}/{field}")
	public ResponseEntity<ResponseStructure<Page<Order>>> getOrderDetailsByPagenation_Sort(@PathVariable int pageNumber,
			@PathVariable int pageSize, @PathVariable String field) {
		return service.getOrderByPagination_Sort(pageNumber, pageSize, field);
	}

	// 7)get order details by session ID
	@GetMapping("/sessionId/{sessionId}")
	public ResponseEntity<ResponseStructure<List<Order>>> getOrderDetailsBySessionId(@PathVariable String sessionId) {
		return service.getOrderDetailsBySessionId(sessionId);
	}

}
