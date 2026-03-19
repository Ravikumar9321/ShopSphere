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
import com.shopsphere.DTO.ResponseStructure;
import com.shopsphere.Entity.Category;
import com.shopsphere.Exception.CategoryNotFoundException;
import com.shopsphere.Exception.InvalidRequestException;
import com.shopsphere.Exception.ResourceNotFoundException;

@Service
public class Category_Service {

	@Autowired
	private Category_dao categorydao;

	private <T> ResponseEntity<ResponseStructure<T>> buildResponse(HttpStatus status, T data, String message) {
		ResponseStructure<T> response = new ResponseStructure<T>();
		response.setStatusCode(status.value());
		response.setData(data);
		response.setMessage(message);
		return new ResponseEntity<>(response, status);
	}

	// 1)
	public ResponseEntity<ResponseStructure<Category>> createCategory(Category category) {
		if (category == null)
			throw new ResourceNotFoundException("Check entered details");
		return buildResponse(HttpStatus.CREATED, categorydao.createCategory(category), "Created Sucessfully");
	}

	// 2)
	public ResponseEntity<ResponseStructure<List<Category>>> saveAllCategory(List<Category> category) {
		for (Category check : category) {
			if (check == null)
				throw new InvalidRequestException("check entered details" + check.getName());
		}
		List<Category> savedCategories = categorydao.saveAllCategory(category);
		return buildResponse(HttpStatus.CREATED, savedCategories, "Created successfully");
	}

	// 3)
	public ResponseEntity<ResponseStructure<List<Category>>> fetchallCategory() {
		List<Category> list = categorydao.fetchAllCategory();
		if (list.size() == 0)
			throw new CategoryNotFoundException("Category  Empty");
		return buildResponse(HttpStatus.OK, list, "Category Details Retrieved");
	}

	// 4)
	public ResponseEntity<ResponseStructure<Category>> fetchCategoryById(int id) {
		Optional<Category> optional = categorydao.fetchCategoryById(id);
		if (!optional.isPresent())
			throw new CategoryNotFoundException("category details does not exist");
		return buildResponse(HttpStatus.OK, optional.get(), "Category details retrieved based on id");
	}

	// 5)
	public ResponseEntity<ResponseStructure<Category>> updateCategory(Category category) {
		if (category.getId() == null)
			throw new InvalidRequestException("please enter category id to update");
		Optional<Category> optional = categorydao.fetchCategoryById(category.getId());

		if (!optional.isPresent())
			throw new CategoryNotFoundException("category not found");

		return buildResponse(HttpStatus.OK, categorydao.createCategory(category), "successfuly updated");
	}

	// 6
	public ResponseEntity<ResponseStructure<String>> deleteCategory(int id) {
		Optional<Category> optional = categorydao.fetchCategoryById(id);
		if (!optional.isPresent())
			throw new CategoryNotFoundException("category does not exist");
		categorydao.deleteCategory(id);

		return buildResponse(HttpStatus.OK, "SUCCESS", "Deleted successfully");
	}

	// 7
	public ResponseEntity<ResponseStructure<Page<Category>>> getCategoryByPagination_Sort(int pageNumber, int pageSize,
			String field) {
		try {
			Page<Category> page = categorydao.getCategoryByPagination_Sort(pageNumber, pageSize, field);
			if (page.isEmpty()) {
				return buildResponse(HttpStatus.OK, page, "No categories found for this page");
			}
			return buildResponse(HttpStatus.OK, page, "Success");

		} catch (IllegalArgumentException | PropertyReferenceException e) {
			throw new InvalidRequestException("Invalid sort field. Allowed: id,name");
		}
	}
}
