package com.shopsphere.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.shopsphere.DTO.ResponseStructure;
import com.shopsphere.Entity.OrderItem;
import com.shopsphere.Service.OrderItem_Service;

import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/orderitem")
@CrossOrigin(origins = "http://localhost:3000")
@Tag(name = "OrderItem", description = "OrderItem related APIs")

public class OrderItem_Controller {

	@Autowired
	private OrderItem_Service service;

	// 1)fetch all OrderItem details
	@GetMapping
	public ResponseEntity<ResponseStructure<List<OrderItem>>> fetchallOrderItemDetails() {
		return service.fetchallOrderItem();
	}

	// 2)fetch OrderItem details by Id
	@GetMapping("/{id}")
	public ResponseEntity<ResponseStructure<OrderItem>> fetchOrderItemDetailsById(@PathVariable int id) {
		return service.fetchOrderItemById(id);
	}

	// 3)update OrderItem details
	@PutMapping
	public ResponseEntity<ResponseStructure<OrderItem>> updateOrderItemDetails(@RequestBody OrderItem OrderItem) {
		return service.updateOrderItem(OrderItem);
	}

	// 4)delete OrderItem detail
	@DeleteMapping("/{id}")
	public ResponseEntity<ResponseStructure<String>> deleteOrderItemDetails(@PathVariable int id) {
		return service.deleteOrderItem(id);
	}

	// 5)OrderItem details in pagination and sort format
	@GetMapping("/{pageNumber}/{pageSize}/{field}")
	public ResponseEntity<ResponseStructure<Page<OrderItem>>> getOrderItemDetailsByPagenation_Sort(
			@PathVariable int pageNumber, @PathVariable int pageSize, @PathVariable String field) {
		return service.getOrderItemByPagination_Sort(pageNumber, pageSize, field);
	}

	// 6)get OrderItem details by sessionId
	@GetMapping("/order/{orderId}")
	public ResponseEntity<ResponseStructure<List<OrderItem>>> getOrderItemsByOrder(@PathVariable String orderId) {
		return service.getOrderItemsByOrder(orderId);
	}

}
