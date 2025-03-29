package com.advocate.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.advocate.dto.response.CommonResponseDto;
import com.advocate.service.CaseDocumentService;

@RestController
@RequestMapping("/api/cases")
public class CaseDocumentController {

    @Autowired
    private CaseDocumentService caseDocumentService;

    @PostMapping("/{caseId}/uploadDocument")
    public ResponseEntity<CommonResponseDto<Void>> uploadCaseDocument(@PathVariable Long caseId,
            @RequestParam("file") MultipartFile file, @RequestParam("documentName") String documentName,
            @RequestParam("description") String description) {
        caseDocumentService.uploadCaseDocument(caseId, file, documentName, description);
        return ResponseEntity.ok(new CommonResponseDto<>("Document Created Successfully.", HttpStatus.CREATED,null));
    }

    @GetMapping("/{caseId}/downloadDocument/{documentName}")
    public ResponseEntity<Resource> downloadCaseDocument(@PathVariable Long caseId,
            @PathVariable String documentName) {
        Resource resource = caseDocumentService.getCaseDocument(caseId, documentName);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + documentName + "\"")
                .body(resource);
    }

    @DeleteMapping("/{caseId}/deleteDocument/{documentName}")
    public ResponseEntity<CommonResponseDto<String>> deleteCaseDocument(@PathVariable Long caseId,
            @PathVariable String documentName) {
        caseDocumentService.deleteCaseDocument(caseId, documentName);
        return ResponseEntity.ok(new CommonResponseDto<>("Document deleted successfully.", HttpStatus.OK, null));
    }

    @PutMapping("/{caseId}/updateDocument")
    public ResponseEntity<CommonResponseDto<Void>> updateCaseDocument(@PathVariable Long caseId,
            @RequestParam("file") MultipartFile file, @RequestParam("name") String documentName,
            @RequestParam("description") String description) {
        caseDocumentService.updateCaseDocument(caseId, file, documentName, description);
        return ResponseEntity.ok(new CommonResponseDto<>("Document updated successfully.", HttpStatus.OK,null));
    }

}
