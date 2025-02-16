package com.advocate.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.advocate.entity.User;


@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    // Custom queries can be added here
	
	User findByEmail(String email);
	
}
