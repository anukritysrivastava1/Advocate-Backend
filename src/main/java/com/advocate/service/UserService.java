package com.advocate.service;

import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.advocate.dto.SignupRequest;
import com.advocate.entity.User;
import com.advocate.enums.Role;
import com.advocate.repository.UserRepository;

import jakarta.validation.Valid;


@Service
public class UserService {
	
	@Autowired
	private UserRepository userRepository;
	
	
	//get
	public String getUsers() {
		
		
		return "A";
	}
	
	//add
	public void addUser(User user) {
		
		userRepository.save(user);
	}


	//Sign-up
	public String signup(@Valid SignupRequest signupRequest) {
		User existingUser = userRepository.findByEmail(signupRequest.getEmail());
		
		User newUser = new User();
		newUser.setAddress(signupRequest.getCountry());
		newUser.setEmail(signupRequest.getEmail());
		newUser.setMobile(signupRequest.getPhoneNumber());
		newUser.setFirstName(signupRequest.getUsername());
		newUser.setPassword(signupRequest.getPassword());
		
	    String roleType = signupRequest.getRole();
	     
	    if (roleType == null || roleType.trim().isEmpty()) {
	        return "Error: Role cannot be empty!";
	    }	    
	    
	    try {
	        Role role = Role.valueOf(roleType.trim().toUpperCase()); 
	        System.out.println(role);
	        newUser.setRole(role);
	    } catch (IllegalArgumentException e) {
	        return "Error: Invalid role '" + roleType + "'. Allowed values: ADMIN, SUBADMIN, USER.";
	    }
	    
		System.out.println(newUser);
		
		
		if(existingUser != null) {
			return "User already exist!";
			
		}
		
		userRepository.save(newUser);
		return "User registered successfully!";
	}
	
	//Login
	public User login(String email, String password) {
		
		User user = userRepository.findByEmail(email);
		
		if(user != null) {
			if(user.getPassword().equals(password));
			return user;
		}
		
		return null;
	}
	
	
	
    private String generateOtp() {
        Random random = new Random();
        int otp = 100000 + random.nextInt(900000); // Generate 6-digit OTP
        return String.valueOf(otp);
    }

	
	//update
	
	//delete
	
	

}
