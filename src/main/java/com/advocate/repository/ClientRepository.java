package com.advocate.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.advocate.entity.Client;


@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {
    // Custom queries can be added here
}
