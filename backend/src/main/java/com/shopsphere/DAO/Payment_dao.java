package com.shopsphere.DAO;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import com.shopsphere.Entity.Payment;
import com.shopsphere.Repository.Payment_Repository;

@Repository
public class Payment_dao {

	@Autowired
	private Payment_Repository repository;

	public Payment createPayment(Payment payment) {
		return repository.save(payment);
	}

	public List<Payment> fetchAllPayment() {
		return repository.findAll();

	}

	public Optional<Payment> fetchPaymentById(int id) {
		return repository.findById(id);

	}

	public void deletePayment(Payment payment) {
		repository.delete(payment);
	}

	public Page<Payment> getPaymentByPagination_Sort(int pageNumber, int pageSize, String field) {
		return repository.findAll(PageRequest.of(pageNumber, pageSize, Sort.by(field)));

	}

	public Optional<Payment> getPaymentByOrderId(Integer orderId) {
		return repository.getPaymentByOrderId(orderId);

	}

}
