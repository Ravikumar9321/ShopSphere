package com.shopsphere.Service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.mapping.PropertyReferenceException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.shopsphere.DAO.Category_dao;
import com.shopsphere.DAO.Product_dao;
import com.shopsphere.DTO.ResponseStructure;
import com.shopsphere.Entity.Category;
import com.shopsphere.Entity.Product;
import com.shopsphere.Exception.CategoryNotFoundException;
import com.shopsphere.Exception.InvalidRequestException;
import com.shopsphere.Exception.ProductNotFoundException;
import com.shopsphere.Exception.ResourceNotFoundException;

import jakarta.transaction.Transactional;

@Service
public class Product_Service {
	@Autowired
	private Category_dao cdao;
	@Autowired
	private Product_dao productdao;

	private <T> ResponseEntity<ResponseStructure<T>> buildResponse(HttpStatus status, T data, String message) {
		ResponseStructure<T> response = new ResponseStructure<T>();
		response.setStatusCode(status.value());
		response.setData(data);
		response.setMessage(message);
		return new ResponseEntity<>(response, status);
	}

	// 1)
	public ResponseEntity<ResponseStructure<Product>> createProduct(Product product) {

		if (product.getName() == null)
			throw new ResourceNotFoundException("Check entered details");

		Optional<Category> optional = cdao.fetchCategoryById(product.getCategory().getId());
		if (!optional.isPresent())
			throw new CategoryNotFoundException("Category does not exist");

		product.setCategory(optional.get());
		return buildResponse(HttpStatus.CREATED, productdao.createProduct(product), "Created Sucessfully");
	}

	// 2)
	@Transactional
	public ResponseEntity<ResponseStructure<List<Product>>> saveAllProduct(List<Product> product) {
		if (product.size() <= 0)
			throw new InvalidRequestException("Invalid Enter Details");
		List<Category> list = cdao.fetchAllCategory();
		boolean categoryFound = false;
		for (Product p : product) {
			for (Category exist : list) {
				if (exist.getId().equals(p.getCategory().getId())) {
					p.setCategory(exist);
					categoryFound = true;
					break;
				}

			}
			if (!categoryFound)
				throw new CategoryNotFoundException(
						"Category  " + p.getCategory().getId() + " not exist for " + p.getName());
		}

		List<Product> savedProducts = productdao.saveAllProduct(product);
		return buildResponse(HttpStatus.CREATED, savedProducts, "Created successfully");
	}

	// 3)
	public ResponseEntity<ResponseStructure<List<Product>>> fetchallProduct() {
		List<Product> list = productdao.fetchAllProduct();
		if (list.size() == 0)
			throw new ProductNotFoundException("Product  Empty");
		return buildResponse(HttpStatus.OK, list, "Product Details Retrieved");
	}

	// 4)
	public ResponseEntity<ResponseStructure<Product>> fetchProductById(int id) {
		Optional<Product> optional = productdao.fetchProductById(id);
		if (!optional.isPresent())
			throw new ProductNotFoundException("Product details does not exist");
		return buildResponse(HttpStatus.OK, optional.get(), "Product details retrieved based on id");
	}

	// 5)
	@Transactional
	public ResponseEntity<ResponseStructure<Product>> updateProduct(Product Product) {
		if (Product.getId() == null)
			throw new InvalidRequestException("please enter Product id to update");
		Optional<Product> optional = productdao.fetchProductById(Product.getId());

		if (!optional.isPresent())
			throw new ProductNotFoundException("Product not found");

		return buildResponse(HttpStatus.OK, productdao.updateProduct(Product), "successfuly updated");
	}

	// 6
	public ResponseEntity<ResponseStructure<String>> deleteProduct(int id) {
		Optional<Product> optional = productdao.fetchProductById(id);
		if (!optional.isPresent())
			throw new ProductNotFoundException("Product does not exist");
		productdao.deleteProduct(id);

		return buildResponse(HttpStatus.OK, "SUCCESS", "Deleted successfully");
	}

	// 7
	public ResponseEntity<ResponseStructure<Page<Product>>> getProductByPagination_Sort(int pageNumber, int pageSize,
			String field) {
		try {
			Page<Product> page = productdao.getProductByPagination_Sort(pageNumber, pageSize, field);
			if (page.isEmpty()) {
				return buildResponse(HttpStatus.OK, page, "No categories found for this page");
			}
			return buildResponse(HttpStatus.OK, page, "Success");

		} catch (IllegalArgumentException | PropertyReferenceException e) {
			throw new InvalidRequestException("Invalid sort field. Allowed: id,name, description,stockQuantity,price");
		}
	}

	public ResponseEntity<ResponseStructure<List<Product>>> getProductByCategoryName(String categoryName) {
		List<Product> list = productdao.getProductByCategoryName(categoryName);
		if (list.size() <= 0)
			throw new ProductNotFoundException("Products Empty with " + categoryName + " Category");
		return buildResponse(HttpStatus.OK, list, "Products Founded");
	}
}
