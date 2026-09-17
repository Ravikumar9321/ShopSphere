package com.shopsphere.Service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.mapping.PropertyReferenceException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.shopsphere.DAO.OrderItem_dao;
import com.shopsphere.DTO.ResponseStructure;
import com.shopsphere.Entity.OrderItem;
import com.shopsphere.Exception.InvalidRequestException;
import com.shopsphere.Exception.ResourceNotFoundException;

@Service
public class OrderItem_Service {

	
	@Autowired
	private OrderItem_dao orderItemdao;

	private <T> ResponseEntity<ResponseStructure<T>> buildResponse(HttpStatus status, T data, String message) {
		ResponseStructure<T> response = new ResponseStructure<T>();
		response.setStatusCode(status.value());
		response.setData(data);
		response.setMessage(message);
		return ResponseEntity.status(status).body(response);
	}

	// 3)
	public ResponseEntity<ResponseStructure<List<OrderItem>>> fetchallOrderItem() {
		List<OrderItem> list = orderItemdao.fetchAllOrderItem();
		if (list.size() == 0)
			throw new ResourceNotFoundException("OrderItem  Empty");
		return buildResponse(HttpStatus.OK, list, "OrderItem Details Retrieved");
	}

	// 4)
	public ResponseEntity<ResponseStructure<OrderItem>> fetchOrderItemById(int id) {
		Optional<OrderItem> optional = orderItemdao.fetchOrderItemById(id);
		if (!optional.isPresent())
			throw new ResourceNotFoundException("OrderItem details does not exist");
		return buildResponse(HttpStatus.OK, optional.get(), "OrderItem details retrieved based on id");
	}

	// 5)
	public ResponseEntity<ResponseStructure<OrderItem>> updateOrderItem(OrderItem OrderItem) {
		if (OrderItem.getId() == null)
			throw new InvalidRequestException("please enter OrderItem id to update");
		Optional<OrderItem> optional = orderItemdao.fetchOrderItemById(OrderItem.getId());

		if (!optional.isPresent())
			throw new ResourceNotFoundException("OrderItem not found");

		return buildResponse(HttpStatus.OK, orderItemdao.creatOrderItem(OrderItem), "successfuly updated");
	}

	// 6
	public ResponseEntity<ResponseStructure<String>> deleteOrderItem(int id) {
		Optional<OrderItem> optional = orderItemdao.fetchOrderItemById(id);
		if (!optional.isPresent())
			throw new ResourceNotFoundException("OrderItem does not exist");
		orderItemdao.deleteOrderItem(id);

		return buildResponse(HttpStatus.OK, "SUCCESS", "Deleted successfully");
	}

	// 7
	public ResponseEntity<ResponseStructure<Page<OrderItem>>> getOrderItemByPagination_Sort(int pageNumber,
			int pageSize, String field) {
		try {
			Page<OrderItem> page = orderItemdao.getOrderItemByPagination_Sort(pageNumber, pageSize, field);
			if (page.isEmpty()) {
				return buildResponse(HttpStatus.OK, page, "No categories found for this page");
			}
			return buildResponse(HttpStatus.OK, page, "Success");

		} catch (IllegalArgumentException | PropertyReferenceException e) {
			throw new InvalidRequestException("Invalid sort field. Allowed: id,name,price,quantity");
		}
	}

	public ResponseEntity<ResponseStructure<List<OrderItem>>> getOrderItemsByOrder(String orderId) {

		List<OrderItem> list = orderItemdao.getOrderItemsByOrder(orderId);
		if (list.size() <= 0)
			throw new ResourceNotFoundException("No ordered item found with " + orderId);
		return buildResponse(HttpStatus.OK, list, "Successfully Retrieved");
	}

}
