package com.advocate.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.advocate.entity.User;
import com.advocate.enums.Role;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    // Custom queries can be added here

    // query to find user by email
    User findByEmail(String email);

    // query to find user by role
    List<User> findByRole(Role role);

    List<User> findByStatus(String status);

    User findByEmailAndRole(String email, Role role);

}
