package com.shopsphere.Entity;


import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.Data;

@Data
@Entity
public class Product {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
	private String name;
	private String description;
	private Double price;
	private Integer stockQuantity;
	private String imageUrl;
	
	@JoinColumn
	@ManyToOne
	private Category category;
	 
	@JsonIgnore
	@OneToMany(cascade = CascadeType.ALL,mappedBy = "product")
	private List<CartItem> cartItems;
	
	@JsonIgnore
	@OneToMany(cascade = CascadeType.ALL,mappedBy = "product")
	private List<OrderItem> orderItems;
    
}
