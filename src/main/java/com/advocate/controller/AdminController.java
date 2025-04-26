package com.advocate.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.advocate.dto.request.SignupRequest;
import com.advocate.dto.request.UpdateUserRequestDto;
import com.advocate.dto.response.CommonResponseDto;
import com.advocate.entity.User;
import com.advocate.exception.EntityAlreadyExistsException;
import com.advocate.service.UserService;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

	@Autowired
	private UserService userService;

	// add
	@PostMapping("")
	public ResponseEntity<CommonResponseDto<User>> add(@RequestBody SignupRequest signupRequest)
			throws EntityAlreadyExistsException {
		System.out.println(signupRequest);
		System.out.println("before service");
		var user = userService.addUser(signupRequest);

		return ResponseEntity.ok(new CommonResponseDto<>("Users added successfully ", HttpStatus.OK, user));

	}

	// Update User
	@PutMapping("")
	public ResponseEntity<CommonResponseDto<User>> updateUser(UpdateUserRequestDto updateUserRequestDto) {
		User updatedUser = userService.updateUser(updateUserRequestDto);

		return ResponseEntity.ok(new CommonResponseDto<>("Users updated successfully ", HttpStatus.OK, updatedUser));

	}

	// get All User By Role
	@GetMapping("/role")
	public ResponseEntity<CommonResponseDto<List<User>>> getAllUserByRole(@RequestParam String role) {

		List<User> users = userService.getAllUsersByRole(role);
		if (users.isEmpty()) {
			return ResponseEntity.ok(new CommonResponseDto<>("No user found with role: " + role, HttpStatus.OK, users));
		}
		return ResponseEntity.ok(new CommonResponseDto<>("Users found with role: " + role, HttpStatus.OK, users));
	}

	// get All User By Status
	@GetMapping("/status")
	public ResponseEntity<CommonResponseDto<List<User>>> getAllUserByStatus(@RequestParam String status) {
		List<User> users = userService.getAllUsersByStatus(status);
		if (users.isEmpty()) {
			return ResponseEntity
					.ok(new CommonResponseDto<>("No user found with status: " + status, HttpStatus.OK, users));
		}
		return ResponseEntity.ok(new CommonResponseDto<>("Users found with status: " + status, HttpStatus.OK, users));

	}

	@DeleteMapping("/{id}")
	public ResponseEntity<CommonResponseDto<User>> deleteClientById(@PathVariable Long id) {

		userService.deleteAdminById(id);
		return ResponseEntity.ok(new CommonResponseDto<>("Admin deleted successfully. ", HttpStatus.OK, null));

	}

}
