package com.advocate.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.advocate.entity.Case;


@Repository
public interface CaseRepository extends JpaRepository<Case, Long> {
   
    List<Case> findByPresentDate(String presentDate);
    List<Case> findByLastDateBefore(String lastDate);
    List<Case> findByPresentDateAfter(String presentDate);
}
