package com.advocate.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.advocate.entity.CaseDate;


@Repository
public interface CaseDateRepository extends JpaRepository<CaseDate, Long> {
    // Custom queries can be added here
}
