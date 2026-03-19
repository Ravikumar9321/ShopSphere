package com.shopsphere.Exception;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.shopsphere.DTO.ResponseStructure;

@ControllerAdvice
public class GlobelExceptionHandler extends ResponseEntityExceptionHandler {

	@ExceptionHandler(CategoryNotFoundException.class)
	public ResponseEntity<ResponseStructure<String>> handleCategoryNotFound(CategoryNotFoundException e) {
		return errorResponse(HttpStatus.NOT_FOUND, "FAILED", e.getMessage());
	}

	@ExceptionHandler(ResourceDuplicateFoundException.class)
	public ResponseEntity<ResponseStructure<String>> handleDuplicateFound(ResourceDuplicateFoundException e) {
		return errorResponse(HttpStatus.CONFLICT, "FAILED", e.getMessage());

	}

	@ExceptionHandler(CartNotFoundException.class)
	public ResponseEntity<ResponseStructure<String>> handleCartNotFound(CartNotFoundException e) {
		return errorResponse(HttpStatus.NOT_FOUND, "FAILED", e.getMessage());

	}

	@ExceptionHandler(CartItemNotFoundException.class)
	public ResponseEntity<ResponseStructure<String>> handleCartItemNotFound(CartItemNotFoundException e) {
		return errorResponse(HttpStatus.NOT_FOUND, "FAILED", e.getMessage());

	}

	@ExceptionHandler(InvalidRequestException.class)
	public ResponseEntity<ResponseStructure<String>> handleInvalidRequest(InvalidRequestException e) {
		return errorResponse(HttpStatus.BAD_REQUEST, "FAILED", e.getMessage());

	}

	@ExceptionHandler(ProductNotFoundException.class)
	public ResponseEntity<ResponseStructure<String>> handleProductNotFound(ProductNotFoundException e) {
		return errorResponse(HttpStatus.NOT_FOUND, "FAILED", e.getMessage());

	}

	@ExceptionHandler(DataIntegrityViolationException.class)
	public ResponseEntity<ResponseStructure<String>> handleDuplicate(DataIntegrityViolationException e) {
		return errorResponse(HttpStatus.CONFLICT, null, e.getMessage());
	}

	@ExceptionHandler(ResourceNotFoundException.class)
	public ResponseEntity<ResponseStructure<String>> handleResourceNotFound(ResourceNotFoundException e) {
		return errorResponse(HttpStatus.NOT_FOUND, "FAILED", e.getMessage());

	}

	private ResponseEntity<ResponseStructure<String>> errorResponse(HttpStatus status, String data, String message) {
		ResponseStructure<String> response = new ResponseStructure<>();
		response.setStatusCode(status.value());
		response.setData(data);
		response.setMessage(message);
		return ResponseEntity.status(status).body(response);
	}

}
