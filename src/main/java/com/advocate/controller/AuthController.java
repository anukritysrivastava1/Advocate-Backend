package com.advocate.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.advocate.dto.request.AdminLoginRequest;
import com.advocate.dto.request.SignupRequest;
import com.advocate.dto.response.CommonResponseDto;
import com.advocate.entity.User;
import com.advocate.exception.EntityAlreadyExistsException;
import com.advocate.service.AuthService;
import com.advocate.service.EmailService;
import com.advocate.service.UserService;

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
			throws EntityAlreadyExistsException {
		var user = authService.signup(signupRequest);

		return ResponseEntity.ok(new CommonResponseDto<>("Users added successfully ", HttpStatus.OK, user));

	}

	// Login
	@PostMapping("/login")
	public ResponseEntity<CommonResponseDto<User>> loginUser(@RequestParam String email,
			@RequestParam String password) {
		User user = authService.login(email, password);

		return ResponseEntity.ok(new CommonResponseDto<>("Users logged-in successfully ", HttpStatus.OK, user));

	}

	// Admin Login
	@PostMapping("/adminLogin")
	public ResponseEntity<CommonResponseDto<User>> loginAdmin(@RequestBody AdminLoginRequest loginRequest) {
		
		User user = authService.loginAdmin(loginRequest.getEmail(), loginRequest.getPassword());

		return ResponseEntity.ok(new CommonResponseDto<>("Admin logged-in successfully ", HttpStatus.OK, user));
	}

	// Logout

	// ForgetPassword

	// LoginWithOTPTeamMem
	@PostMapping("/sendOTP")
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
