package com.advocate.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.advocate.entity.CaseDetail;


@Repository
public interface CaseDetailRepository extends JpaRepository<CaseDetail, Long> {
    // Custom queries can be added here
}
