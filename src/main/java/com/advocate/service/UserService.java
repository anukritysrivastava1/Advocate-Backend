package com.advocate.service;

import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.advocate.entity.User;
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

	
	//update
	
	//delete
	
	

}
