package com.advocate.dto;

import com.advocate.entity.Address;
import com.fasterxml.jackson.annotation.JsonInclude;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;


@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class SignupRequest {

	@NotBlank(message = "First Name is required")
	@Size(min = 4, max = 20, message = "First Name must be between 4 and 20 characters")
	private String firstName;

	@NotBlank(message = "Last Name is required")
	@Size(min = 4, max = 20, message = "Last Name must be between 4 and 20 characters")
	private String lastName;
	

	@NotBlank(message = "Email is required")
	@Email(message = "Invalid email format")
	private String email;

	@NotBlank(message = "Password is required")
	@Size(min = 6, message = "Password must be at least 6 characters")
	private String password;

	@NotBlank(message = "Confirm password is required")
	private String confirmPassword;

	@NotBlank(message = "Phone number is required")
	private String mobile;


	

	@NotBlank(message = "Role is required")
	private String role; // Ensure this field is included in the request

	@JsonInclude(JsonInclude.Include.NON_NULL) // Ignore null fields in JSON
	private Address address; // Ensure this field is included in the request

	
	
	
}
