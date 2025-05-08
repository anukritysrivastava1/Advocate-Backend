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

		newUser.setRole(Role.USER);

		System.out.println(newUser);

		return userRepository.save(newUser);

	}



	public User login(String email, String password, Role role) {

		User user;
	
		if (role == Role.ADMIN) {
			user = userRepository.findByEmailAndRole(email, Role.ADMIN);
			if (user == null) {
				throw new EntityNotFoundException("Admin not found with given email!");
			}
		} else {
			user = userRepository.findByEmail(email);
			if (user == null) {
				throw new EntityNotFoundException("User not found with given email!");
			}
		}
	
		if (!passwordEncoder.matches(password, user.getPassword())) {
			throw new IllegalArgumentException("Wrong password entered");
		}
	
		System.out.println("Role: " + user.getRole());
		return user;
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
