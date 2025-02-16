package com.advocate.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.advocate.dto.CommonResponseDto;
import com.advocate.entity.User;
import com.advocate.service.UserService;

import io.swagger.v3.oas.annotations.parameters.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
@RequestMapping("/api/users")
public class UserController {
	
	@Autowired
	private UserService userService;
	
	//add
	@PostMapping("/")
	public ResponseEntity<String> add(@RequestBody User user){
		 userService.addUser(user);
		 return ResponseEntity.ok("User Added Successfully");
		
		
	}



	//get All User By Role
	@GetMapping("/role")
	public ResponseEntity<CommonResponseDto<List<User>>> getAllUserByRole(@RequestParam String role) {

		List<User> users = userService.getAllUsersByRole(role);
		if(users.isEmpty()) {
			return ResponseEntity.ok(new CommonResponseDto<>("No user found with role: "+role,  HttpStatus.OK, users ));
		}
		 return ResponseEntity.ok(new CommonResponseDto<>("Users found with role: "+role,  HttpStatus.OK, users ));
	}
	
	//get All User By Status
	@GetMapping("/status")
	public  ResponseEntity<CommonResponseDto<List<User>>> getAllUserByStatus(@RequestParam String status) {
		List<User> users = userService.getAllUsersByStatus(status);
		if(users.isEmpty()) {
			return ResponseEntity.ok(new CommonResponseDto<>("No user found with status: "+status,  HttpStatus.OK, users ));
		}
		 return ResponseEntity.ok(new CommonResponseDto<>("Users found with status: "+status,  HttpStatus.OK, users ));

	}


}
