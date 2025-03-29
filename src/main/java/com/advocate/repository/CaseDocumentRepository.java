package com.advocate.repository;

import com.advocate.entity.CaseDocument;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CaseDocumentRepository extends JpaRepository<CaseDocument, Long> {
    List<CaseDocument> findByCaseId(Long caseId);
}
