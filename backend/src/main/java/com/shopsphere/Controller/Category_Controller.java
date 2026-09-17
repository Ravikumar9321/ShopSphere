package com.shopsphere.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.shopsphere.DTO.ResponseStructure;
import com.shopsphere.Entity.Category;
import com.shopsphere.Service.Category_Service;

import io.swagger.v3.oas.annotations.tags.Tag;


@RestController
@RequestMapping("/api/category")
@CrossOrigin(origins = "http://localhost:3000")
@Tag(name = "Category", description = "Category related APIs")
public class Category_Controller {
	@Autowired
	 private Category_Service service;
	   
	//1) save details
		@PostMapping
		public ResponseEntity<ResponseStructure<Category>> saveCategoryDetails(@RequestBody Category category) {
			return service.createCategory(category);
		}
		//2)
		@PostMapping("/bulk")
		public ResponseEntity<ResponseStructure<List<Category>>> createMultiCategory(@RequestBody List<Category> category){
			return service.saveAllCategory(category);
		}
		
	    //3)fetch all Category details
		@GetMapping
		public ResponseEntity<ResponseStructure<List<Category>>> fetchallCategoryDetails() {
			return service.fetchallCategory();
		}
		
	    //4)fetch  Category details by Id
		@GetMapping("/{id}")
		public ResponseEntity<ResponseStructure<Category>> fetchCategoryDetailsById(@PathVariable int id) {
			return service.fetchCategoryById(id);
		}
		//5)update Category details
		@PutMapping
		public ResponseEntity<ResponseStructure<Category>> updateCategoryDetails(@RequestBody Category category){
			return service.updateCategory(category);
		}
		//6)delete Category detail
		@DeleteMapping("/{id}")
		public ResponseEntity<ResponseStructure<String>> deleteCategoryDetails(@PathVariable int id){
			return service.deleteCategory(id);
		}
		
		//7)Category details in pagination and sort format
		   @GetMapping("/{pageNumber}/{pageSize}/{field}")
		   public  ResponseEntity<ResponseStructure<Page<Category>>> getCategoryDetailsByPagenation_Sort(@PathVariable int pageNumber,@PathVariable int pageSize,@PathVariable String field){
			   return service.getCategoryByPagination_Sort(pageNumber,pageSize,field);
		   }
		   
	
}
