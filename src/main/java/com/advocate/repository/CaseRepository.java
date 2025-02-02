package com.advocate.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.advocate.entity.Case;


@Repository
public interface CaseRepository extends JpaRepository<Case, Long> {
    // Custom queries can be added here
}
