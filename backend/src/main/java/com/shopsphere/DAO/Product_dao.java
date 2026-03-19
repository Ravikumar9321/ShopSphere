package com.shopsphere.DAO;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import com.shopsphere.Entity.Product;
import com.shopsphere.Repository.Product_Repository;

@Repository
public class Product_dao {

	@Autowired
	private Product_Repository productRepo;

	public Product createProduct(Product product) {
		return productRepo.save(product);
	}

	public List<Product> saveAllProduct(List<Product> product) {
		return productRepo.saveAll(product);

	}

	public List<Product> fetchAllProduct() {
		return productRepo.findAll();

	}

	public Optional<Product> fetchProductById(int id) {
		return productRepo.findById(id);

	}

	public void deleteProduct(int id) {
		productRepo.deleteById(id);

	}

	public Page<Product> getProductByPagination_Sort(int pageNumber, int pageSize, String field) {
		return productRepo.findAll(PageRequest.of(pageNumber, pageSize, Sort.by(field)));

	}

	public Product updateProduct(Product product) {
		// TODO Auto-generated method stub
		return productRepo.save(product);
	}

	public List<Product> getProductByCategoryName(String categoryName) {
		return productRepo.getProductByCategoryName(categoryName);

	}

}
