package com.advocate.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import javax.mail.Multipart;

import org.apache.coyote.BadRequestException;
import org.apache.tika.Tika;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.advocate.dto.request.ProfilePicRequestDto;
import com.advocate.dto.request.SignupRequest;
import com.advocate.dto.request.UpdateUserRequestDto;
import com.advocate.entity.Address;
import com.advocate.entity.User;
import com.advocate.enums.Role;
import com.advocate.exception.EntityAlreadyExistsException;
import com.advocate.repository.UserRepository;

import jakarta.persistence.EntityNotFoundException;

@Service
public class UserService {

	@Autowired
	private UserRepository userRepository;

	private static final String BASE_DIR = "resources/";

	// Add New user
	public User addUser(SignupRequest signupRequest) throws EntityAlreadyExistsException {
		User newUser = userRepository.findByEmail(signupRequest.getEmail());

		if (newUser != null) {
			throw new EntityAlreadyExistsException("User already exists with given email !");

		}

		newUser = new User();
		newUser.setAddress(signupRequest.getAddress());
		newUser.setEmail(signupRequest.getEmail());
		newUser.setMobile(signupRequest.getMobile());
		newUser.setFirstName(signupRequest.getFirstName());
		newUser.setLastName(signupRequest.getLastName());
		newUser.setPassword(signupRequest.getPassword());
		System.out.println(signupRequest);
		String roleType = signupRequest.getRole();
		System.out.println("Role is " + roleType);
		if (roleType == null || roleType.trim().isEmpty()) {
			throw new IllegalArgumentException("Error: Role cannot be empty!");
		}

		try {
			Role role = Role.valueOf(roleType.trim().toUpperCase());
			System.out.println(role);
			newUser.setRole(role);
		} catch (IllegalArgumentException e) {
			throw new IllegalArgumentException(
					"Error: Invalid role '" + roleType + "'. Allowed values: ADMIN, SUBADMIN, USER.");
		}

		System.out.println(newUser);

		return userRepository.save(newUser);
	}

	// private String generateOtp() {
	// Random random = new Random();
	// int otp = 100000 + random.nextInt(900000); // Generate 6-digit OTP
	// return String.valueOf(otp);
	// }

	// Get all users by role
	public List<User> getAllUsersByRole(String roleType) {

		if (roleType == null || roleType.trim().isEmpty()) {
			throw new IllegalArgumentException("Error: Role cannot be empty!");
		}

		try {
			Role role = Role.valueOf(roleType.trim().toUpperCase());
			System.out.println(role);
			return userRepository.findByRole(role);

		} catch (IllegalArgumentException e) {
			throw new IllegalArgumentException(
					"Error: Invalid role '" + roleType + "'. Allowed values: ADMIN, SUBADMIN, USER.");
		}

	}

	// Get all users by status
	public List<User> getAllUsersByStatus(String status) {
		status = status.trim().toUpperCase();
		if (status == null || status.isEmpty()) {
			throw new IllegalArgumentException("Error: Status cannot be empty!");
		} else if (!status.equals("ACTIVE") && !status.equals("INACTIVE")) {
			throw new IllegalArgumentException(
					"Error: Invalid status '" + status + "'. Allowed values: ACTIVE, INACTIVE.");
		}
		return userRepository.findByStatus(status);
	}

	// update
	public User updateUser(UpdateUserRequestDto updateUserRequestDto) {
		User user = userRepository.findById(updateUserRequestDto.getId())
				.orElseThrow(() -> new EntityNotFoundException("User not found with given id"));

		user.setAddress((Address) checkAndUpdateValueIfPresent(user.getAddress(), updateUserRequestDto.getAddress()));
		user.setEmail((String) checkAndUpdateValueIfPresent(user.getEmail(), updateUserRequestDto.getEmail()));
		user.setMobile((String) checkAndUpdateValueIfPresent(user.getMobile(), updateUserRequestDto.getMobile()));
		user.setFirstName(
				(String) checkAndUpdateValueIfPresent(user.getFirstName(), updateUserRequestDto.getFirstName()));
		user.setLastName((String) checkAndUpdateValueIfPresent(user.getLastName(), updateUserRequestDto.getLastName()));
		user.setPassword((String) checkAndUpdateValueIfPresent(user.getPassword(), updateUserRequestDto.getPassword()));

		return userRepository.save(user);

	}

	private Object checkAndUpdateValueIfPresent(Object object1, Object object2) {
		return (object2 == null || object2.toString().equals("")) ? object1 : object2;
	}

	// Find if userEmail already exist
	public boolean userExistByEmail(String email) {
		System.out.println(userRepository.findByEmail(email));
		return userRepository.findByEmail(email) != null;

	}

	// private String generateOtp() {
	// Random random = new Random();
	// int otp = 100000 + random.nextInt(900000); // Generate 6-digit OTP
	// return String.valueOf(otp);
	// }

	// Delete Admin
	public void deleteAdminById(Long id) {
		userRepository.findById(id)
				.ifPresentOrElse(userRepository::delete,
						() -> {
							throw new EntityNotFoundException("Admin not found with given id");
						});

	}

	public User addProfilePic(Long userId, MultipartFile  pic) {
		User user = userRepository.findById(userId)
				.orElseThrow(() -> new RuntimeException("User not found"));

		try {
			String userDir = BASE_DIR + "userId-" + userId + "/";
			Files.createDirectories(Paths.get(userDir));

			String fileExtension = getImageExtension(pic.getBytes());
			if (fileExtension == null) {
				throw new BadRequestException("Invalid image format");
			}

			String filePath = userDir + "prof-pic." + fileExtension;
			Files.write(Paths.get(filePath), pic.getBytes());

			user.setProfilePicPath(filePath);
			return userRepository.save(user);

		} catch (IOException e) {
			throw new RuntimeException("Error saving profile picture: " + e.getMessage());
		}
	}

	private String getImageExtension(byte[] fileData) throws IOException {
		Tika tika = new Tika();
		String mimeType = tika.detect(fileData);

		return switch (mimeType) {
			case "image/jpeg" -> "jpg";
			case "image/png" -> "png";
			case "image/gif" -> "gif";
			default -> null;
		};
	}

	public Resource getProfilePic(Long userId) {
		try {
			Path filePath = Paths.get(BASE_DIR + "userId-" + userId + "/prof-pic.jpg");
			Resource resource = new UrlResource(filePath.toUri());

			if (resource.exists() || resource.isReadable()) {
				return resource;
			} else {
				return null;
			}
		} catch (Exception e) {
			throw new RuntimeException("Unimplemented method 'getProfilePic'");
		}
	}

}
