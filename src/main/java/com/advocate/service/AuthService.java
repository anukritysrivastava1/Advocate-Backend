package com.advocate.service;

import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.advocate.dto.request.SignupRequest;
import com.advocate.entity.User;
import com.advocate.enums.Role;
import com.advocate.exception.EntityAlreadyExistsException;
import com.advocate.repository.UserRepository;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;

@Service
public class AuthService {

	@Autowired
	private UserRepository userRepository;

	private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

	// Sign-up new user
	public User signup(@Valid SignupRequest signupRequest) throws EntityAlreadyExistsException, BadRequestException {
		User newUser = userRepository.findByEmail(signupRequest.getEmail());

		if (newUser != null) {
			throw new EntityAlreadyExistsException("User already exists with given email !");

		} else if (!signupRequest.getPassword().equals(signupRequest.getConfirmPassword())) {
			throw new BadRequestException("password and confirm password didn't match !");
		}

		newUser = new User();
		System.out.println(signupRequest.getAddress());
		newUser.setAddress(signupRequest.getAddress());
		newUser.setEmail(signupRequest.getEmail());
		newUser.setMobile(signupRequest.getMobile());
		newUser.setFirstName(signupRequest.getFirstName());
		newUser.setLastName(signupRequest.getLastName());
		newUser.setPassword(passwordEncoder.encode(signupRequest.getPassword()));

		String roleType = signupRequest.getRole();

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

	// Login for Admin
	public User loginAdmin(String email, String password) {

		User user = userRepository.findByEmailAndRole(email, Role.ADMIN);

		if (user != null) {
			if (passwordEncoder.matches(password, user.getPassword())) {
				System.out.println("Role: " + user.getRole());
				return user;
			}
			throw new EntityNotFoundException("Wrong password entered");

		}
		throw new EntityNotFoundException("Admin not found with given email!");

	}

	// Login using email and password
	public User login(String email, String password) throws BadRequestException {

		User user = userRepository.findByEmail(email);

		if (user != null) {

			if (passwordEncoder.matches(password, user.getPassword())) {
				System.out.println("Role: " + user.getRole());
				return user;
			}
			throw new BadRequestException("Wrong password entered");

		}

		throw new EntityNotFoundException("User not found with given email !");
	}

	//Update Password
	@Transactional
	public String updatePassword(String email, String newPassword) {
	    User user = userRepository.findByEmail(email);

	    if (user == null) {
	        throw new EntityNotFoundException("User with email " + email + " does not exist");
	    }

		String encodedPassword = passwordEncoder.encode(newPassword);
		user.setPassword(encodedPassword);
		userRepository.save(user);

	    return "Password updated successfully!";
	}

}
