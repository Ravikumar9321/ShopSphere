package com.shopsphere.Controller;

import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import com.shopsphere.DTO.*;
import com.shopsphere.Entity.UserInfo;
import com.shopsphere.Repository.User_Repository;
import com.shopsphere.Service.User_Service;
import com.shopsphere.Utility.JwtUtil;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
@Tag(name = "Authentication", description = "Authentication related APIs")
public class Auth_Controller {
	
	   private final User_Service service;
	   private final User_Repository repository;
	   private final JwtUtil jwtUtil;
	   private final PasswordEncoder passwordEncoder;
	   
	   @PostMapping("/register")
	   public ResponseEntity<AuthResponse> registerUser(@Valid @RequestBody AuthRequest request){
		   if (repository.findByEmail(request.email()).isPresent()) {
		        return new ResponseEntity<>(new AuthResponse("User already exists", null), HttpStatus.CONFLICT);
		    }	   
		   service.createUser(UserInfo.builder()
				   .email(request.email())
				   .password(passwordEncoder.encode(request.password()))
				   .build());
		    return new ResponseEntity<>(new AuthResponse(" Registered successfully", null), HttpStatus.CREATED);		   
	   }
	   @PostMapping("/login")
	   public ResponseEntity<AuthResponse> loginUser(@RequestBody AuthRequest request){
		   Optional<UserInfo> user = repository.findByEmail(request.email());
		    if (user.isEmpty()) {
		        return new ResponseEntity<>(new AuthResponse("User not registered", null), HttpStatus.NOT_FOUND);
		    }
	           
		    UserInfo userInfo = user.get();
		    if (!passwordEncoder.matches(request.password(), userInfo.getPassword())) {
		        return new ResponseEntity<>(new AuthResponse("Invalid password", null), HttpStatus.UNAUTHORIZED);
		    }	
		    String token = jwtUtil.generateToken(request.email());
		    return ResponseEntity.ok(new AuthResponse("Login successful", token));

	}

}
