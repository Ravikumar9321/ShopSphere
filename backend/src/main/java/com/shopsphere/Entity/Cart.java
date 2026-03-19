package com.shopsphere.Entity;

import java.time.LocalDateTime;
import java.util.List;

import org.hibernate.annotations.CreationTimestamp;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.Data;

@Entity
@Data
public class Cart {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	private String sessionId;
	@JsonFormat(pattern = "dd-MMM-yyyy h:mm a")
	@CreationTimestamp
	private LocalDateTime createdAt;

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "cart")
	@JsonIgnore
	private List<CartItem> cartItems;

}
