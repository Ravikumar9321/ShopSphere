package com.shopsphere.Entity;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.shopsphere.DTO.PaymentStatus;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.Data;
@Entity
@Data
public class Payment {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	private Double amount;
	@Enumerated(EnumType.STRING)
	private PaymentStatus status;
	@JsonFormat(pattern = "dd-MMM-yyyy h:mm a")
	 @CreationTimestamp
	    private LocalDateTime paidAt;
	 
	 @OneToOne
	 @JoinColumn
	 private Order order; 

}
