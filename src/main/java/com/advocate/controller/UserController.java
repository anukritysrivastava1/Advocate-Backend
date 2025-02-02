package com.advocate.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.advocate.entity.User;
import com.advocate.service.UserService;

import io.swagger.v3.oas.annotations.parameters.RequestBody;

@RestController
@RequestMapping("api/user")
public class UserController {
	
	@Autowired
	private UserService userService;
	
	//add
	@PostMapping("/add")
	public ResponseEntity<String> add(@RequestBody User user){
		 userService.addUser(user);
		 return ResponseEntity.ok("User Added Successfully");
		
		
	}
	


}
