package com.advocate.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.advocate.entity.Client;
import com.advocate.enums.Role;


@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {
    // Custom queries can be added here

    
    // query to find user by email
    Client findByEmail(String email);

    // query to find user by role
    List<Client> findByRole(Role role);

    List<Client> findByStatus(String status);

}
