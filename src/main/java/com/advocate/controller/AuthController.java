package com.advocate.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.advocate.dto.CommonResponseDto;
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
	public ResponseEntity<CommonResponseDto<User>> add(@Valid @RequestBody SignupRequest signupRequest) throws EntityAlreadyExistsException{
		var user = authService.signup(signupRequest);
      
		return ResponseEntity.ok(new CommonResponseDto<>("Users added successfully ",  HttpStatus.OK, user ));
	
	}
    
    
    //Login
    @PostMapping("/login")
    public ResponseEntity<CommonResponseDto<User>> loginUser(@RequestParam String email, @RequestParam String password){
    	User user = authService.login(email, password);
    	
        return ResponseEntity.ok(new CommonResponseDto<>("Users logged-in successfully ",  HttpStatus.OK, user ));
	
    }
    
    
    //Logout
    
    
    //ForgetPassword
    
    
    //LoginWithOTPTeamMem
}
