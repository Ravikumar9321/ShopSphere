package com.shopsphere.Entity;

import java.time.LocalDateTime;
import java.util.List;

import org.hibernate.annotations.CreationTimestamp;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.shopsphere.DTO.Orderstatus;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name = "shop_orders")
public class Order {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@JsonFormat(pattern = "dd-MMM-yyyy h:mm a")
	@CreationTimestamp
	private LocalDateTime orderDate;
	private Double totalAmount;
	@Enumerated(EnumType.STRING)
	private Orderstatus status;

	private String sessionId;

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "order")
	@JsonIgnore
	private List<OrderItem> orderItems;

	@JsonIgnore
	@OneToOne(cascade = CascadeType.ALL, mappedBy = "order")
	private Payment payment;

}
