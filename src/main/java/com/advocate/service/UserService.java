package com.advocate.service;

import java.util.List;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.advocate.entity.User;
import com.advocate.enums.Role;
import com.advocate.repository.UserRepository;


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


	
	
	
	
    private String generateOtp() {
        Random random = new Random();
        int otp = 100000 + random.nextInt(900000); // Generate 6-digit OTP
        return String.valueOf(otp);
    }

	//Get all users by role
	public List<User> getAllUsersByRole(String roleType) {

		if (roleType == null || roleType.trim().isEmpty()) {
	        throw new IllegalArgumentException("Error: Role cannot be empty!");
	    }	    
	    
	    try {
	        Role role = Role.valueOf(roleType.trim().toUpperCase()); 
	        System.out.println(role);
			return  userRepository.findByRole(role);
	        
	    } catch (IllegalArgumentException e) {
	        throw new  IllegalArgumentException("Error: Invalid role '" + roleType + "'. Allowed values: ADMIN, SUBADMIN, USER.");
	    }
	    
	}

	//Get all users by status
	public List<User> getAllUsersByStatus(String status) {
		status = status.trim().toUpperCase();
		if(status == null || status.isEmpty()) {
			throw new IllegalArgumentException("Error: Status cannot be empty!");
		} else if (!status.equals("ACTIVE") && !status.equals("INACTIVE")) {
			throw new IllegalArgumentException("Error: Invalid status '" + status + "'. Allowed values: ACTIVE, INACTIVE.");
		}
		return  userRepository.findByStatus(status);
	}

	
	//update
	
	//delete
	
	

}
