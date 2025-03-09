package com.advocate.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.advocate.dto.request.ProfilePicRequestDto;
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
	@PostMapping("/add")
	public ResponseEntity<CommonResponseDto<User>> add(@RequestBody SignupRequest signupRequest)
			throws EntityAlreadyExistsException {
		System.out.println(signupRequest);
		System.out.println("before service");
		var user = userService.addUser(signupRequest);

		return ResponseEntity.ok(new CommonResponseDto<>("Users added successfully ", HttpStatus.OK, user));

	}

	// Update User
	@PutMapping("/update")
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

	// add profile pic
	@PostMapping("/{userId}/addProfilePic")
	public ResponseEntity<CommonResponseDto<User>> addProfilePic(@RequestParam("file") MultipartFile file, @PathVariable Long userId) {
		User user = userService.addProfilePic(userId, file);
		return ResponseEntity.ok(new CommonResponseDto<>("Profile pic added successfully ", HttpStatus.OK, user));

	}

	 @GetMapping("/{userId}/profile-picture")
    public ResponseEntity<Resource> getProfilePicture(@PathVariable Long userId) {

		Resource pic = userService.getProfilePic(userId);

		if (pic == null) {
			return ResponseEntity.notFound().build();
		} else {
			return ResponseEntity.ok()
			.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + "prof-pic" + "\"")
			.body(pic);
			
		}
        
    }

}
