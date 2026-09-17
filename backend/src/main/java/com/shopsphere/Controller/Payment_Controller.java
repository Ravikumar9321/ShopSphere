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
import com.shopsphere.Entity.Payment;
import com.shopsphere.Service.Payment_Service;

import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/payment")
@CrossOrigin(origins = "http://localhost:3000")
@Tag(name = "Payment", description = "Payment related APIs")

public class Payment_Controller {
	@Autowired
	private Payment_Service service;

	// 1) save details
	@PostMapping("/order/{orderId}")
	public ResponseEntity<ResponseStructure<Payment>> savePaymentDetails(@PathVariable Integer orderId,
			@RequestBody Payment payment) {
		return service.createPayment(orderId, payment);
	}

	// 2)fetch all Payment details
	@GetMapping
	public ResponseEntity<ResponseStructure<List<Payment>>> fetchallPaymentDetails() {
		return service.fetchallPayment();
	}

	// 3)fetch Payment details by Id
	@GetMapping("/{id}")
	public ResponseEntity<ResponseStructure<Payment>> fetchPaymentDetailsById(@PathVariable Integer id) {
		return service.fetchPaymentById(id);
	}

	// 4)update Payment details
	@PutMapping
	public ResponseEntity<ResponseStructure<Payment>> updatePaymentDetails(@RequestBody Payment payment) {
		return service.updatePayment(payment);
	}

	// 5)delete Payment detail
	@DeleteMapping("/{id}")
	public ResponseEntity<ResponseStructure<String>> deletePaymentDetails(@PathVariable Integer id) {
		return service.deletePayment(id);
	}

	// 6)Payment details in pagination and sort format
	@GetMapping("/{pageNumber}/{pageSize}/{field}")
	public ResponseEntity<ResponseStructure<Page<Payment>>> getPaymentDetailsByPagenation_Sort(
			@PathVariable int pageNumber, @PathVariable int pageSize, @PathVariable String field) {
		return service.getPaymentByPagination_Sort(pageNumber, pageSize, field);
	}

	// 7)get payment details by order id
	@GetMapping("/order/{orderId}")
	public ResponseEntity<ResponseStructure<Payment>> getPaymentByOrderId(@PathVariable Integer orderId) {
		return service.getPaymentByOrderId(orderId);
	}

}
