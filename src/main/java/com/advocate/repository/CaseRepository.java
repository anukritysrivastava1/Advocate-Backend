package com.advocate.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.advocate.entity.Case;
import com.advocate.enums.CaseStatus;

@Repository
public interface CaseRepository extends JpaRepository<Case, Long> {

    List<Case> findByPresentDate(String presentDate);

    List<Case> findByLastDateBefore(String lastDate);

    List<Case> findByPresentDateAfter(String presentDate);

    @Query("SELECT c.presentDate FROM Case c WHERE c.id = :caseId")
    List<String> findCaseDates(@Param("caseId") Long caseId);

    // Search by Case Number
    List<Case> findByCaseNoContainingIgnoreCase(String caseNo);

    // Search by Case Status
    List<Case> findByCaseStatus(CaseStatus caseStatus);

    // // Search by Client Name
    // @Query("SELECT c FROM Case c JOIN c.clients cl WHERE LOWER(cl.name) LIKE LOWER(CONCAT('%', :clientName, '%'))")
    // List<Case> findByClientName(@Param("clientName") String clientName);

    // @Query("SELECT c FROM Case c JOIN c.clients cl WHERE cl.clientCode = :clientCode")
    // List<Case> findByClientCode(@Param("clientCode") String clientCode);

}
