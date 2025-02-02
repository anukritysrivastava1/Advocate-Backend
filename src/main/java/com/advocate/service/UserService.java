package com.advocate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.advocate.entity.User;
import com.advocate.repository.UserRepository;


@Service
public class UserService {
	
	

	
	@Autowired
	private UserRepository userRepo;
	
	
	
	//get
	
	//add
	public void addUser(User user) {
		
		userRepo.save(user);
	}
	
	//update
	
	//delete
	
	

}
