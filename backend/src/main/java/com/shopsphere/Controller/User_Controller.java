package com.shopsphere.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.shopsphere.DTO.ResponseStructure;
import com.shopsphere.Entity.UserInfo;
import com.shopsphere.Service.User_Service;

import io.swagger.v3.oas.annotations.Hidden;

@RestController
@RequestMapping("api/user")

public class User_Controller {
	
	@Autowired
	private User_Service service;
	@Hidden
	@GetMapping("/all")
	public ResponseEntity<ResponseStructure<List<UserInfo>>> findAllUsers(){
		return service.findAllUsers();
	}

}
