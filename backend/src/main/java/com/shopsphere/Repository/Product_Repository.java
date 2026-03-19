package com.shopsphere.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.shopsphere.Entity.Product;

public interface Product_Repository extends JpaRepository<Product, Integer> {

	@Query("select c.products from Category c where c.name=?1")
	List<Product> getProductByCategoryName(String categoryName);

}
