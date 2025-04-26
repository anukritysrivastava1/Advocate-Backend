package com.advocate.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.advocate.dto.response.CommonResponseDto;
import com.advocate.dto.request.CaseRequestDto;
import com.advocate.entity.Case;
import com.advocate.exception.EntityAlreadyExistsException;
import com.advocate.service.CaseService;

@RestController
@RequestMapping("api/cases")
public class CaseController {

    @Autowired
    private CaseService caseService;

    @PostMapping("")
    public ResponseEntity<CommonResponseDto<Case>> addCase(@RequestBody CaseRequestDto caseRequestDto)
            throws EntityAlreadyExistsException {
                var cases = caseService.addCase(caseRequestDto);
        return ResponseEntity.ok(new CommonResponseDto<>("Case added successfully ", HttpStatus.OK, cases));

    }

    @PutMapping("")
    public ResponseEntity<CommonResponseDto<Case>> updateCase(@RequestBody CaseRequestDto caseRequestDto){

        Case updatedCase = caseService.updateCase(caseRequestDto);

        return ResponseEntity.ok(new CommonResponseDto<>("Case updated successfully ", HttpStatus.OK, updatedCase));

    }

    @GetMapping("")
    public ResponseEntity<CommonResponseDto<List<Case>>> getAllCases(){
       
        List<Case> casesAll = caseService.getAllCases();
        return ResponseEntity.ok(new CommonResponseDto<>("All Cases are : ", HttpStatus.OK, casesAll));
 

    }

    @GetMapping("/search")
    public ResponseEntity<CommonResponseDto<List<Case>>> searchCases(@RequestParam String searchType, 
                                                                      @RequestParam String searchValue, 
                                                                      @RequestParam(required = false) Long caseId) {
        try {
            List<Case> searchedResults = caseService.searchCases(searchType, searchValue, caseId);
            return ResponseEntity.ok(new CommonResponseDto<>("Search completed successfully", HttpStatus.OK, searchedResults));
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(new CommonResponseDto<>("Invalid search type", HttpStatus.BAD_REQUEST, null), HttpStatus.BAD_REQUEST);
        }
    }
    

    @DeleteMapping("/{id}")
    public ResponseEntity<CommonResponseDto<Void>> deleteCaseById(@PathVariable Long id) {
        caseService.deleteCaseById(id);
        return ResponseEntity.ok(new CommonResponseDto<>("Case deleted successfully.", HttpStatus.OK, null));
    }
    



}
