package com.shopsphere.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.shopsphere.Entity.Payment;

public interface Payment_Repository extends JpaRepository<Payment, Integer> {

	@Query("select o.payment from Order o where o.id=?1")
	Optional<Payment> getPaymentByOrderId(Integer orderId);

}
