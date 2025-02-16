package com.advocate.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class SignupRequest {

	public SignupRequest() {
		super();
		// TODO Auto-generated constructor stub
	}

	@NotBlank(message = "Country is required")
	private String country;

	@NotBlank(message = "Email is required")
	@Email(message = "Invalid email format")
	private String email;

	@NotBlank(message = "Phone number is required")
	private String phoneNumber;

	@NotBlank(message = "Username is required")
	@Size(min = 4, max = 20, message = "Username must be between 4 and 20 characters")
	private String username;

	@NotBlank(message = "Password is required")
	@Size(min = 6, message = "Password must be at least 6 characters")
	private String password;

	@NotBlank(message = "Confirm password is required")
	private String confirmPassword;

	@NotBlank(message = "Role is required")
	private String role; // Ensure this field is included in the request

	// Getters and Setters
	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getConfirmPassword() {
		return confirmPassword;
	}

	public void setConfirmPassword(String confirmPassword) {
		this.confirmPassword = confirmPassword;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public SignupRequest(@NotBlank(message = "Country is required") String country,
			@NotBlank(message = "Email is required") @Email(message = "Invalid email format") String email,
			@NotBlank(message = "Phone number is required") String phoneNumber,
			@NotBlank(message = "Username is required") @Size(min = 4, max = 20, message = "Username must be between 4 and 20 characters") String username,
			@NotBlank(message = "Password is required") @Size(min = 6, message = "Password must be at least 6 characters") String password,
			@NotBlank(message = "Confirm password is required") String confirmPassword,
			@NotBlank(message = "Role is required") String role) {
		super();
		this.country = country;
		this.email = email;
		this.phoneNumber = phoneNumber;
		this.username = username;
		this.password = password;
		this.confirmPassword = confirmPassword;
		this.role = role;
	}

	@Override
	public String toString() {
		return "SignupRequest [country=" + country + ", email=" + email + ", phoneNumber=" + phoneNumber + ", username="
				+ username + ", password=" + password + ", confirmPassword=" + confirmPassword + ", role=" + role + "]";
	}
	
	
}
