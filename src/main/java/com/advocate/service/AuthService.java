package com.advocate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.advocate.dto.SignupRequest;
import com.advocate.entity.User;
import com.advocate.enums.Role;
import com.advocate.exception.EntityAlreadyExistsException;
import com.advocate.repository.UserRepository;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;

@Service
public class AuthService {
    

    @Autowired
	private UserRepository userRepository;


    //Sign-up new user
	public String signup(@Valid SignupRequest signupRequest) throws EntityAlreadyExistsException {
		User newUser = userRepository.findByEmail(signupRequest.getEmail());

		if(newUser != null) {
			throw new EntityAlreadyExistsException("User already exists with given email !");
			
		}
		
		newUser = new User();
        System.out.println(signupRequest.getAddress());
		newUser.setAddress(signupRequest.getAddress());
		newUser.setEmail(signupRequest.getEmail());
		newUser.setMobile(signupRequest.getPhoneNumber());
		newUser.setFirstName(signupRequest.getFirstName());
		newUser.setLastName(signupRequest.getLastName());
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

		userRepository.save(newUser);
		return "User registered successfully!";
	}
	
	//Login using email and password
	public User login(String email, String password) {
		
		User user = userRepository.findByEmail(email);
		
		if(user != null) {
			if(user.getPassword().equals(password));
			return user;
		}
		
		throw new EntityNotFoundException("User not found with given email !");
	}
}
