package com.advocate.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.advocate.dto.SignupRequest;
import com.advocate.entity.User;
import com.advocate.exception.EntityAlreadyExistsException;
import com.advocate.service.AuthService;


import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    
    @Autowired
    private AuthService authService;

    
    
    //SignUp
    @PostMapping("/signup")
    public ResponseEntity<String> registerUser(@Valid @RequestBody SignupRequest signupRequest) throws EntityAlreadyExistsException {
        String res = authService.signup(signupRequest);
        if (res.equals("User registered successfully!")) {
			return ResponseEntity.ok(res);
		} else {
			return ResponseEntity.status(400).body(res); // If user already exists
		}
    }
    
    
    //Login
    @PostMapping("/login")
    public ResponseEntity<User> loginUser(@RequestParam String email, @RequestParam String password){
    	User user = authService.login(email, password);
    	
    	if(user != null) {
    		return ResponseEntity.status(HttpStatus.OK).body(user);
    	}
    	return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(user);
    }
    
    
    //Logout
    
    
    //ForgetPassword
    
    
    //LoginWithOTPTeamMem
}
