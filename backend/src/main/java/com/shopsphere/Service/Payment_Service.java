package com.shopsphere.Service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.mapping.PropertyReferenceException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.shopsphere.DAO.Order_dao;
import com.shopsphere.DAO.Payment_dao;
import com.shopsphere.DTO.Orderstatus;
import com.shopsphere.DTO.PaymentStatus;
import com.shopsphere.DTO.ResponseStructure;
import com.shopsphere.Entity.Order;
import com.shopsphere.Entity.Payment;
import com.shopsphere.Exception.InvalidRequestException;
import com.shopsphere.Exception.ResourceNotFoundException;

import jakarta.transaction.Transactional;

@Service
public class Payment_Service {

	@Autowired
	private Payment_dao paymentdao;
	@Autowired
	private Order_dao orderdao;

	private <T> ResponseEntity<ResponseStructure<T>> buildResponse(HttpStatus status, T data, String message) {
		ResponseStructure<T> response = new ResponseStructure<T>();
		response.setStatusCode(status.value());
		response.setData(data);
		response.setMessage(message);
		return new ResponseEntity<>(response, status);
	}

	// 1)
	public ResponseEntity<ResponseStructure<Payment>> createPayment(Integer orderId, Payment payment) {

		if (payment == null)
			throw new ResourceNotFoundException("Check entered details");

		Order order = orderdao.fetchOrderById(orderId)
				.orElseThrow(() -> new ResourceNotFoundException("Order details not found"));

		Optional<Payment> existingPayment = paymentdao.getPaymentByOrderId(orderId);
		if (existingPayment.isPresent() && existingPayment.get().getStatus() == PaymentStatus.COMPLETED) {
			throw new InvalidRequestException("Payment already completed");
		}

		payment.setAmount(order.getTotalAmount());
		payment.setOrder(order);
		payment.setStatus(PaymentStatus.COMPLETED);

		Payment savedPayment = paymentdao.createPayment(payment);

		order.setStatus(Orderstatus.PAID);
		orderdao.createOrder(order);

		return buildResponse(HttpStatus.CREATED, savedPayment, "Payment completed successfully");
	}

	// 3)
	public ResponseEntity<ResponseStructure<List<Payment>>> fetchallPayment() {
		List<Payment> list = paymentdao.fetchAllPayment();
		if (list.size() <= 0)
			throw new ResourceNotFoundException("Payment  Empty");
		return buildResponse(HttpStatus.OK, list, "Payment Details Retrieved");
	}

	// 4)
	public ResponseEntity<ResponseStructure<Payment>> fetchPaymentById(int id) {
		Payment payment = paymentdao.fetchPaymentById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Payment details does not exist"));

		return buildResponse(HttpStatus.OK, payment, "Payment details retrieved based on id");
	}

	// 5)
	public ResponseEntity<ResponseStructure<Payment>> updatePayment(Payment Payment) {
		if (Payment.getId() == null)
			throw new InvalidRequestException("please enter Payment id to update");
		paymentdao.fetchPaymentById(Payment.getId())
				.orElseThrow(() -> new ResourceNotFoundException("Payment not found"));

		return buildResponse(HttpStatus.OK, paymentdao.createPayment(Payment), "successfuly updated");
	}

	// 6
	@Transactional
	public ResponseEntity<ResponseStructure<String>> deletePayment(Integer id) {

		System.out.println("Deleting payment ID: " + id);
		Payment payment = paymentdao.fetchPaymentById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Payment does not exist"));

		paymentdao.deletePayment(payment);

		return buildResponse(HttpStatus.OK, "SUCCESS", "Payment deleted successfully");
	}

	// 7
	public ResponseEntity<ResponseStructure<Page<Payment>>> getPaymentByPagination_Sort(int pageNumber, int pageSize,
			String field) {
		try {
			Page<Payment> page = paymentdao.getPaymentByPagination_Sort(pageNumber, pageSize, field);
			if (page.isEmpty()) {
				return buildResponse(HttpStatus.OK, page, "No categories found for this page");
			}
			return buildResponse(HttpStatus.OK, page, "Success");

		} catch (IllegalArgumentException | PropertyReferenceException e) {
			throw new InvalidRequestException("Invalid sort field. Allowed: id,name,status");
		}
	}

	public ResponseEntity<ResponseStructure<Payment>> getPaymentByOrderId(Integer orderId) {
		orderdao.fetchOrderById(orderId).orElseThrow(() -> new ResourceNotFoundException("Order Not Found"));
		Payment payment = paymentdao.getPaymentByOrderId(orderId)
				.orElseThrow(() -> new ResourceNotFoundException("Payment is not done"));

		return buildResponse(HttpStatus.OK, payment, "Payment retrieved");
	}
}
