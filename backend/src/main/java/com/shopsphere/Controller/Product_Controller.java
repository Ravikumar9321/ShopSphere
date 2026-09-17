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
import com.shopsphere.Entity.Product;
import com.shopsphere.Service.Product_Service;

import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/product")
@CrossOrigin(origins = "http://localhost:3000")
@Tag(name = "Product", description = "Product related APIs")

public class Product_Controller {
	@Autowired
	private Product_Service service;

	// 1) save details
	@PostMapping
	public ResponseEntity<ResponseStructure<Product>> saveProductDetails(@RequestBody Product product) {
		return service.createProduct(product);
	}

	// 2)
	@PostMapping("/bulk")
	public ResponseEntity<ResponseStructure<List<Product>>> createMultiProduct(@RequestBody List<Product> product) {
		return service.saveAllProduct(product);
	}

	// 3)fetch all Product details
	@GetMapping
	public ResponseEntity<ResponseStructure<List<Product>>> fetchallProductDetails() {
		return service.fetchallProduct();
	}

	// 4)fetch Product details by Id
	@GetMapping("/{id}")
	public ResponseEntity<ResponseStructure<Product>> fetchProductDetailsById(@PathVariable int id) {
		return service.fetchProductById(id);
	}

	// 5)update Product details
	@PutMapping
	public ResponseEntity<ResponseStructure<Product>> updateProductDetails(@RequestBody Product product) {
		return service.updateProduct(product);
	}

	// 6)delete Product detail
	@DeleteMapping("/{id}")
	public ResponseEntity<ResponseStructure<String>> deleteProductDetails(@PathVariable int id) {
		return service.deleteProduct(id);
	}

	// 7)Product details in pagination and sort format
	@GetMapping("/{pageNumber}/{pageSize}/{field}")
	public ResponseEntity<ResponseStructure<Page<Product>>> getProductDetailsByPagenation_Sort(
			@PathVariable int pageNumber, @PathVariable int pageSize, @PathVariable String field) {
		return service.getProductByPagination_Sort(pageNumber, pageSize, field);
	}

	// 8) get Product by category name
	@GetMapping("/category/{categoryName}")
	public ResponseEntity<ResponseStructure<List<Product>>> getProductByCategoryName(
			@PathVariable String categoryName) {
		return service.getProductByCategoryName(categoryName);
	}

}
