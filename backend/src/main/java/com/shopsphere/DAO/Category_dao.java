package com.shopsphere.DAO;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import com.shopsphere.Entity.Category;
import com.shopsphere.Repository.Category_Repository;

@Repository
public class Category_dao {

	@Autowired
	private Category_Repository categoryRepo;
	
	public Category createCategory(Category category) {
		return categoryRepo.save(category);
	}

	public List<Category> saveAllCategory(List<Category> category) {
		return categoryRepo.saveAll(category);
		
	}

	public List<Category> fetchAllCategory() {
		return categoryRepo.findAll();
		
	}

	public Optional<Category> fetchCategoryById(int id) {
		return categoryRepo.findById(id);
		
	}
	public void deleteCategory(int id) {
		categoryRepo.deleteById(id);
		
	}


	public Page<Category> getCategoryByPagination_Sort(int pageNumber, int pageSize, String field) {
		return categoryRepo.findAll(PageRequest.of(pageNumber, pageSize,Sort.by(field)));
		
	}

	
}
