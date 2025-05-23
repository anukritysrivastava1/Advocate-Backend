package com.advocate.controller;

import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.advocate.dto.request.LoginRequest;
import com.advocate.dto.request.SignupRequest;
import com.advocate.dto.response.CommonResponseDto;
import com.advocate.entity.User;
import com.advocate.enums.Role;
import com.advocate.exception.EntityAlreadyExistsException;
import com.advocate.service.AuthService;
import com.advocate.service.EmailService;
import com.advocate.service.UserService;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

	@Autowired
	private AuthService authService;

	@Autowired
	private EmailService emailService;

	@Autowired
	private UserService userService;

	// SignUp
	@PostMapping("/signup")
	public ResponseEntity<CommonResponseDto<User>> add(@Valid @RequestBody SignupRequest signupRequest)
			throws EntityAlreadyExistsException, BadRequestException {
		var user = authService.signup(signupRequest);

		return ResponseEntity.ok(new CommonResponseDto<>("Users added successfully ", HttpStatus.OK, user));

	}


	@PostMapping("/login")
	public ResponseEntity<CommonResponseDto<User>> login(@RequestBody @Valid LoginRequest loginRequest) {
		Role resolvedRole = Role.USER; // Default
	
		if (loginRequest.getRole() != null && !loginRequest.getRole().isBlank()) {
			try {
				resolvedRole = Role.valueOf(loginRequest.getRole().toUpperCase());
			} catch (IllegalArgumentException ex) {
				throw new IllegalArgumentException("Invalid role. Accepted values are: USER or ADMIN.");
			}
		}
	
		User user = authService.login(loginRequest.getEmail(), loginRequest.getPassword(), resolvedRole);
		return ResponseEntity.ok(new CommonResponseDto<>("Logged-in successfully", HttpStatus.OK, user));
	}
	

	// UpdatePassword
	@PatchMapping("/update-password")
	public ResponseEntity<CommonResponseDto<Object>> updatePassword(@RequestParam("email") String email,
			@RequestParam("newPassword") String newPassword) {
		try {
			authService.updatePassword(email, newPassword);
			return ResponseEntity.ok(new CommonResponseDto<>("Password Changed Successfully.", HttpStatus.OK, null));
		} catch (EntityNotFoundException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new CommonResponseDto<>("Users Not Found.", HttpStatus.NOT_FOUND, null));
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new CommonResponseDto<>("An error occurred while updating the password.", HttpStatus.INTERNAL_SERVER_ERROR, null));
			
		}
	}


	// LoginWithOTPTeamMem
	@PostMapping("/send-otp")
	public ResponseEntity<String> sendOTPMail(@RequestParam("email") String recipientEmail,
			@RequestParam("type") String type) {
		boolean isUserExist = userService.userExistByEmail(recipientEmail);

		if ((type.equals("forget-password") && isUserExist) || (type.equals("login") && isUserExist)
				|| (type.equals("sign-up") && !isUserExist)) {

			var otp = emailService.sendOtpNotification(recipientEmail);

			if (otp != null) {
				return ResponseEntity.ok(otp);
			}
		} else if ((type.equals("forget-password") && !isUserExist)) {

			return ResponseEntity.badRequest().body("User does not exist with the given email");
		} else if ((type.equals("login") && !isUserExist)) {

			return ResponseEntity.badRequest().body("User does not exist with the given email");
		} else if ((type.equals("sign-up") && isUserExist)) {

			return ResponseEntity.badRequest().body("User already exist with given email");
		}
		return ResponseEntity.badRequest().body("OTP does not sent due to error");

	}

}
