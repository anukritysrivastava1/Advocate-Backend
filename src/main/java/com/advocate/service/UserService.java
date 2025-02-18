package com.advocate.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.advocate.dto.SignupRequest;
import com.advocate.dto.UpdateUserRequestDto;
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
	
	

	
	
    //Add New user
	public User addUser( SignupRequest signupRequest) throws EntityAlreadyExistsException {
		User newUser = userRepository.findByEmail(signupRequest.getEmail());

		if(newUser != null) {
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
	     System.out.println("Role is "+roleType);
		if (roleType == null || roleType.trim().isEmpty()) {
	        throw new IllegalArgumentException("Error: Role cannot be empty!");
	    }	    
	    
	    try {
	        Role role = Role.valueOf(roleType.trim().toUpperCase()); 
	        System.out.println(role);
	        newUser.setRole(role);
	    } catch (IllegalArgumentException e) {
			throw new IllegalArgumentException("Error: Invalid role '" + roleType + "'. Allowed values: ADMIN, SUBADMIN, USER.");
	    }
	    
		System.out.println(newUser);

		return userRepository.save(newUser);
	}


	
	
	
	
    // private String generateOtp() {
    //     Random random = new Random();
    //     int otp = 100000 + random.nextInt(900000); // Generate 6-digit OTP
    //     return String.valueOf(otp);
    // }

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
	public User updateUser(UpdateUserRequestDto updateUserRequestDto){
		User user = userRepository.findById(updateUserRequestDto.getId()).orElseThrow(() ->  new EntityNotFoundException("User not found with given id"));

		user.setAddress((Address) checkAndUpdateValueIfPresent(user.getAddress(),updateUserRequestDto.getAddress()));
		user.setEmail((String)checkAndUpdateValueIfPresent(user.getEmail(),updateUserRequestDto.getEmail()));
		user.setMobile((String)checkAndUpdateValueIfPresent(user.getMobile(),updateUserRequestDto.getMobile()));
		user.setFirstName((String)checkAndUpdateValueIfPresent(user.getFirstName(),updateUserRequestDto.getFirstName()));
		user.setLastName((String)checkAndUpdateValueIfPresent(user.getLastName(),updateUserRequestDto.getLastName()));
		user.setPassword((String)checkAndUpdateValueIfPresent(user.getPassword(),updateUserRequestDto.getPassword()));

		return userRepository.save(user);

	}

	private Object checkAndUpdateValueIfPresent(Object object1, Object object2){
		return (object2 == null || object2.toString().equals(""))?object1: object2;
	}
	
	
	
	
    // private String generateOtp() {
    //     Random random = new Random();
    //     int otp = 100000 + random.nextInt(900000); // Generate 6-digit OTP
    //     return String.valueOf(otp);
    // }

	
	
	//delete
	
	

}
