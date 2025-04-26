package com.advocate.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import org.apache.coyote.BadRequestException;
import org.apache.tika.Tika;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

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

	private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

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
		newUser.setPassword(passwordEncoder.encode(signupRequest.getPassword()));
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
		user.setPassword((String) checkAndUpdateValueIfPresent(user.getPassword(),
				passwordEncoder.encode(updateUserRequestDto.getPassword())));

		return userRepository.save(user);

	}

	private Object checkAndUpdateValueIfPresent(Object object1, Object object2) {
		return (object2 == null || object2.toString().equals("")) ? object1 : object2;
	}
	
	// Get all users
	public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    // Get user by ID
    public User getUserById(Long id) {
        Optional<User> optionalUser = userRepository.findById(id);
        return optionalUser.orElse(null);  // or throw exception if preferred
    }

	// Find if userEmail already exist
	public boolean userExistByEmail(String email) {
		System.out.println(userRepository.findByEmail(email));
		return userRepository.findByEmail(email) != null;

	}

	// Delete Admin
	public void deleteAdminById(Long id) {
		userRepository.findById(id)
				.ifPresentOrElse(userRepository::delete,
						() -> {
							throw new EntityNotFoundException("Admin not found with given id");
						});

	}

	// Add Profile Pic
	public User addProfilePic(Long userId, MultipartFile pic) {
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
			Path userDir = Paths.get(BASE_DIR + "userId-" + userId + "/");

			try (Stream<Path> files = Files.list(userDir)) {
				Optional<Path> profilePic = files
						.filter(Files::isRegularFile)
						.filter(path -> path.getFileName().toString().startsWith("prof-pic"))
						.findFirst();

				if (profilePic.isPresent()) {
					Resource resource = new UrlResource(profilePic.get().toUri());
					if (resource.exists() && resource.isReadable()) {
						return resource;
					}
				}
			}
			return null;
		} catch (IOException e) {
			throw new RuntimeException("Error fetching profile picture for userId: " + userId, e);
		}
	}

	public String getProfilePicContentType(Long userId) {
		try {
			Path userDir = Paths.get(BASE_DIR + "userId-" + userId + "/");
			try (Stream<Path> files = Files.list(userDir)) {
				Optional<Path> profilePic = files
						.filter(Files::isRegularFile)
						.filter(path -> path.getFileName().toString().startsWith("prof-pic"))
						.findFirst();

				if (profilePic.isPresent()) {
					return Files.probeContentType(profilePic.get());
				}
			}
			return "application/octet-stream";
		} catch (IOException e) {
			throw new RuntimeException("Error determining content type for userId: " + userId, e);
		}
	}

	public String getProfilePicFilename(Long userId) {
		try {
			Path userDir = Paths.get(BASE_DIR + "userId-" + userId + "/");
			try (Stream<Path> files = Files.list(userDir)) {
				Optional<Path> profilePic = files
						.filter(Files::isRegularFile)
						.filter(path -> path.getFileName().toString().startsWith("prof-pic"))
						.findFirst();

				return profilePic.map(path -> path.getFileName().toString()).orElse("profile-pic.jpg");
			}
		} catch (IOException e) {
			throw new RuntimeException("Error getting profile picture filename for userId: " + userId, e);
		}
	}

	// Update Profile Pic
	public User updateProfilePic(Long userId, MultipartFile pic) {
		User user = userRepository.findById(userId)
				.orElseThrow(() -> new RuntimeException("User not found"));

		// Ensure the user already has a profile picture
		if (user.getProfilePicPath() == null) {
			throw new IllegalStateException("No profile picture found. Use addProfilePic to upload one first.");
		}

		try {
			// Get the existing file path
			Path existingPicPath = Paths.get(user.getProfilePicPath());

			// Validate the new image format
			String fileExtension = getImageExtension(pic.getBytes());
			if (fileExtension == null) {
				throw new BadRequestException("Invalid image format");
			}

			// Overwrite the existing profile picture
			Files.write(existingPicPath, pic.getBytes());

			return userRepository.save(user);
		} catch (IOException e) {
			throw new RuntimeException("Error updating profile picture: " + e.getMessage());
		}
	}

	// Delete Profile Pic
	public User deleteProfilePic(Long userId) throws BadRequestException {
		User user = userRepository.findById(userId)
				.orElseThrow(() -> new RuntimeException("User not found"));

		if (user.getProfilePicPath() == null) {
			throw new BadRequestException("No profile picture found to delete.");
		}

		try {
			Path picPath = Paths.get(user.getProfilePicPath());
			Files.deleteIfExists(picPath);
			user.setProfilePicPath(null);

			return userRepository.save(user);
		} catch (IOException e) {
			throw new RuntimeException("Error deleting profile picture: " + e.getMessage());
		}
	}

}
