package com.shopsphere.DTO;

import jakarta.validation.constraints.*;

public record AuthRequest(@NotBlank(message = "Email is required") @Email(message = "Email must be valid") String email,
		String password) {

}