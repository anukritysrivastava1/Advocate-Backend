package com.advocate.service;

import java.io.IOException;
import java.nio.file.*;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class CaseDocumentService {

    private static final String CASES_DIR = "resources/cases/";

    public void uploadCaseDocument(Long caseId, MultipartFile file, String documentName, String description) {
        try {
            String caseDir = CASES_DIR + "caseId-" + caseId + "/";
            Files.createDirectories(Paths.get(caseDir));

            String filePath = caseDir + documentName;
            Files.write(Paths.get(filePath), file.getBytes());
        } catch (IOException e) {
            throw new RuntimeException("Error saving case document: " + e.getMessage());
        }
    }

    public Resource getCaseDocument(Long caseId, String documentName) {
        try {
            Path filePath = Paths.get(CASES_DIR + "caseId-" + caseId + "/" + documentName);
            Resource resource = new UrlResource(filePath.toUri());

            if (resource.exists() && resource.isReadable()) {
                return resource;
            } else {
                throw new RuntimeException("Document not found: " + documentName);
            }
        } catch (IOException e) {
            throw new RuntimeException("Error fetching case document: " + e.getMessage());
        }
    }

    public void deleteCaseDocument(Long caseId, String documentName) {
        try {
            Path filePath = Paths.get(CASES_DIR + "caseId-" + caseId + "/" + documentName);
            Files.deleteIfExists(filePath);
        } catch (IOException e) {
            throw new RuntimeException("Error deleting case document: " + e.getMessage());
        }
    }

    public void updateCaseDocument(Long caseId, MultipartFile file, String documentName, String description) {
        try {
            String caseDir = CASES_DIR + "caseId-" + caseId + "/";
            Path filePath = Paths.get(caseDir + documentName);

            if (!Files.exists(filePath)) {
                throw new RuntimeException("Document not found: " + documentName);
            }

            Files.write(filePath, file.getBytes(), StandardOpenOption.TRUNCATE_EXISTING);
        } catch (IOException e) {
            throw new RuntimeException("Error updating case document: " + e.getMessage());
        }
    }
}
