package com.advocate.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.advocate.dto.SignupRequest;
import com.advocate.dto.UpdateUserRequestDto;
import com.advocate.entity.User;
import com.advocate.exception.EntityAlreadyExistsException;
import com.advocate.repository.UserRepository;
import com.advocate.service.AuthService;
import com.advocate.service.UserService;

import io.swagger.v3.oas.annotations.parameters.RequestBody;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;

@RestController
@RequestMapping("api/user")
public class UserController {
	
	@Autowired
	private UserRepository userRepository;

	@Autowired
	private AuthService authService;
	
	//add
	@PostMapping("/")
	public ResponseEntity<String> add(@Valid @RequestBody SignupRequest signupRequest) throws EntityAlreadyExistsException{
		String res = authService.signup(signupRequest);
        if (res.equals("User registered successfully!")) {
			return ResponseEntity.ok(res);
		} else {
			return ResponseEntity.status(400).body(res); // If user already exists
		}	
	}

	//Update User
	@PutMapping("/")
		public String updateUser(UpdateUserRequestDto updateUserRequestDto) {
	    User updatedUser = UserService.updateUser(updateUserRequestDto);

        return 
	}
	


}
