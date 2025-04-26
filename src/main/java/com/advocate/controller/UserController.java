package com.advocate.controller;

import java.util.List;

import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
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
import org.springframework.web.multipart.MultipartFile;

import com.advocate.dto.request.SignupRequest;
import com.advocate.dto.request.UpdateUserRequestDto;
import com.advocate.dto.response.CommonResponseDto;
import com.advocate.entity.User;
import com.advocate.exception.EntityAlreadyExistsException;
import com.advocate.service.UserService;

@RestController
@RequestMapping("/api/users")
public class UserController {

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
		if (updatedUser == null) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(new CommonResponseDto<>("Failed to update user.", HttpStatus.INTERNAL_SERVER_ERROR, null));

		}
		return ResponseEntity.ok(new CommonResponseDto<>("Users updated successfully ", HttpStatus.OK, updatedUser));

	}

	// Get All Users
	@GetMapping("")
	public ResponseEntity<CommonResponseDto<List<User>>> getAllUsers() {
		List<User> users = userService.getAllUsers();
		return ResponseEntity.ok(new CommonResponseDto<>("All users retrieved successfully", HttpStatus.OK, users));
	}

	// Get User By ID
	@GetMapping("/{userId}")
	public ResponseEntity<CommonResponseDto<User>> getUserById(@PathVariable Long userId) {
		User user = userService.getUserById(userId);
		if (user == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND)
					.body(new CommonResponseDto<>("User not found with ID: " + userId, HttpStatus.NOT_FOUND, null));
		}
		return ResponseEntity.ok(new CommonResponseDto<>("User retrieved successfully", HttpStatus.OK, user));
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

	// add profile pic
	@PostMapping(value = "/{userId}/profile-pic", consumes = "multipart/form-data")
	public ResponseEntity<CommonResponseDto<User>> addProfilePic(@RequestParam("file") MultipartFile file,
			@PathVariable Long userId) {
		User user = userService.addProfilePic(userId, file);
		if (user == null) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(new CommonResponseDto<>("Failed to upload profile pic.", HttpStatus.INTERNAL_SERVER_ERROR,
							null));

		}
		return ResponseEntity.ok(new CommonResponseDto<>("Profile pic uploaded successfully ", HttpStatus.OK, null));

	}

	@GetMapping("/{userId}/profile-pic")
	public ResponseEntity<Resource> getProfilePicture(@PathVariable Long userId) {
		Resource profilePic = userService.getProfilePic(userId);

		if (profilePic == null) {
			return ResponseEntity.notFound().build();
		}

		String contentType = userService.getProfilePicContentType(userId);
		String filename = userService.getProfilePicFilename(userId);

		return ResponseEntity.ok()
				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"")
				.contentType(org.springframework.http.MediaType.parseMediaType(contentType))
				.body(profilePic);
	}

	@PutMapping(value = "/{userId}/profile-pic", consumes = "multipart/form-data")
	public ResponseEntity<CommonResponseDto<User>> updateProfilePic(@RequestParam("file") MultipartFile file,
			@PathVariable Long userId) {
		User user = userService.updateProfilePic(userId, file);
		if (user == null) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(new CommonResponseDto<>("Failed to update profile pic.", HttpStatus.INTERNAL_SERVER_ERROR,
							null));

		}
		return ResponseEntity.ok(new CommonResponseDto<>("Profile pic updated successfully.", HttpStatus.OK, null));
	}

	@DeleteMapping("/{userId}/profile-pic")
	public ResponseEntity<CommonResponseDto<User>> deleteProfilePic(@PathVariable Long userId)
			throws BadRequestException {
		User user = userService.deleteProfilePic(userId);
		if (user == null) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(new CommonResponseDto<>("Failed to delete profile pic.", HttpStatus.INTERNAL_SERVER_ERROR,
							null));

		}
		return ResponseEntity.ok(new CommonResponseDto<>("Profile pic deleted successfully.", HttpStatus.OK, null));
	}

}
