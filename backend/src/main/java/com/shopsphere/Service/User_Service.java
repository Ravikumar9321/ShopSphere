package com.shopsphere.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.shopsphere.DAO.User_dao;
import com.shopsphere.DTO.ResponseStructure;
import com.shopsphere.Entity.UserInfo;
import com.shopsphere.Exception.ResourceNotFoundException;



@Service
public class User_Service {

	@Autowired
	private User_dao userdao;
	public ResponseEntity<ResponseStructure<UserInfo>> createUser(UserInfo user) {
		ResponseStructure<UserInfo> response = new ResponseStructure<>();
		UserInfo user1 = userdao.createuser(user);

		response.setStatusCode(HttpStatus.OK.value());
		response.setMessage("Created Successfully ");
		response.setData(user1);
		return ResponseEntity.status(HttpStatus.OK).body(response);

	}

	public ResponseEntity<ResponseStructure<List<UserInfo>>> findAllUsers() {
		ResponseStructure<List<UserInfo>> response = new ResponseStructure<List<UserInfo>>();
		List<UserInfo> list = userdao.getAllUser();
		if (list.size() >= 1) {
			response.setStatusCode(HttpStatus.OK.value());
			response.setMessage("Retrieved ");
			response.setData(list);
		} else
			throw new ResourceNotFoundException("User details not found");

		return ResponseEntity.status(HttpStatus.OK).body(response);

	}

	
	

}
