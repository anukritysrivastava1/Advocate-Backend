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
import com.advocate.enums.CaseStatus;
import com.advocate.exception.EntityAlreadyExistsException;
import com.advocate.service.CaseService;

@RestController
@RequestMapping("api/cases")
public class CaseController {

    @Autowired
    private CaseService caseService;

    @PostMapping("/")
    public ResponseEntity<CommonResponseDto<Case>> addCase(@RequestBody CaseRequestDto caseRequestDto)
            throws EntityAlreadyExistsException {
                var cases = caseService.addCase(caseRequestDto);
        return ResponseEntity.ok(new CommonResponseDto<>("Case added successfully ", HttpStatus.OK, cases));

    }

    @PutMapping("/")
    public ResponseEntity<CommonResponseDto<Case>> updateCase(@RequestBody CaseRequestDto caseRequestDto){

        Case updatedCase = caseService.updateCase(caseRequestDto);

        return ResponseEntity.ok(new CommonResponseDto<>("Case updated successfully ", HttpStatus.OK, updatedCase));

    }

    @GetMapping("/allDates")
    public ResponseEntity<CommonResponseDto<List<String>>> allDatesByCaseId(@RequestParam Long caseId){

        List<String> allDates = caseService.getAllDates(caseId);
        return ResponseEntity.ok(new CommonResponseDto<>("All Dates for Case Id: "+ caseId, HttpStatus.OK, allDates));
    }

    @GetMapping("/allCases")
    public ResponseEntity<CommonResponseDto<List<Case>>> getAllCases(){
       
        List<Case> casesAll = caseService.getAllCases();
        return ResponseEntity.ok(new CommonResponseDto<>("All Cases are : ", HttpStatus.OK, casesAll));
 

    }

    @GetMapping("/casesByDate")
    public ResponseEntity<CommonResponseDto<List<Case>>> getCasesByDate(@RequestParam String Date){
       
        List<Case> casesAll = caseService.getCasesByDate(Date);
        return ResponseEntity.ok(new CommonResponseDto<>("Cases on : " + Date, HttpStatus.OK, casesAll));
 

    }

    @GetMapping("/casesByPreviousDate")
    public ResponseEntity<CommonResponseDto<List<Case>>> getCasesByPreviousDate(@RequestParam String Date){
       
        List<Case> casesAll = caseService.getPreviousCases(Date);
        return ResponseEntity.ok(new CommonResponseDto<>("Cases on : " + Date, HttpStatus.OK, casesAll));
 

    }

    @GetMapping("/casesByUpcomingDate")
    public ResponseEntity<CommonResponseDto<List<Case>>> getCasesByUpcomingDate(@RequestParam String Date){
       
        List<Case> casesAll = caseService.getUpcomingCases(Date);
        return ResponseEntity.ok(new CommonResponseDto<>("Cases on : " + Date, HttpStatus.OK, casesAll));
 

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<CommonResponseDto<Void>> deleteCaseById(@PathVariable Long id) {
        caseService.deleteCaseById(id);
        return ResponseEntity.ok(new CommonResponseDto<>("Case deleted successfully.", HttpStatus.OK, null));
    }
    


     @GetMapping("/search/date")
    public ResponseEntity<CommonResponseDto<List<Case>>> searchByDate(@RequestParam String date) {
        List<Case> cases = caseService.searchByDate(date);
        return ResponseEntity.ok(new CommonResponseDto<>("Cases found by date", HttpStatus.OK, cases));
    }

    @GetMapping("/search/caseNo")
    public ResponseEntity<CommonResponseDto<List<Case>>> searchByCaseNo(@RequestParam String caseNo) {
        List<Case> cases = caseService.searchByCaseNo(caseNo);
        return ResponseEntity.ok(new CommonResponseDto<>("Cases found by case number", HttpStatus.OK, cases));
    }

    @GetMapping("/search/clientName")
    public ResponseEntity<CommonResponseDto<List<Case>>> searchByClientName(@RequestParam String clientName) {
        List<Case> cases = caseService.searchByClientName(clientName);
        return ResponseEntity.ok(new CommonResponseDto<>("Cases found by client name", HttpStatus.OK, cases));
    }

    // @GetMapping("/search/clientCode")
    // public ResponseEntity<CommonResponseDto<List<Case>>> searchByClientCode(@RequestParam String clientCode) {
    //     List<Case> cases = caseService.searchByClientCode(clientCode);
    //     return ResponseEntity.ok(new CommonResponseDto<>("Cases found by client code", HttpStatus.OK, cases));
    // }

    @GetMapping("/search/status")
    public ResponseEntity<CommonResponseDto<List<Case>>> searchByCaseStatus(@RequestParam CaseStatus caseStatus) {
        List<Case> cases = caseService.searchByCaseStatus(caseStatus);
        return ResponseEntity.ok(new CommonResponseDto<>("Cases found by status", HttpStatus.OK, cases));
    }

}
