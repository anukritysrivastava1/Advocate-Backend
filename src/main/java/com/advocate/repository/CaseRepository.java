package com.advocate.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.advocate.entity.Case;

@Repository
public interface CaseRepository extends JpaRepository<Case, Long> {

    List<Case> findByPresentDate(String presentDate);

    List<Case> findByLastDateBefore(String lastDate);

    List<Case> findByPresentDateAfter(String presentDate);

    @Query("SELECT c.presentDate FROM Case c WHERE c.id = :caseId")
    List<String> findCaseDates(@Param("caseId") Long caseId);

}
