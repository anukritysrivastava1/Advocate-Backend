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
@RequestMapping("api/case")
public class CaseController {

    @Autowired
    private CaseService caseService;

    @PostMapping("/addCase")
    public ResponseEntity<CommonResponseDto<Case>> addCase(@RequestBody CaseRequestDto caseRequestDto)
            throws EntityAlreadyExistsException {
                var cases = caseService.addCase(caseRequestDto);
        return ResponseEntity.ok(new CommonResponseDto<>("Case added successfully ", HttpStatus.OK, cases));

    }

    @PutMapping("/updateCase")
    public ResponseEntity<CommonResponseDto<Case>> updateCase(@RequestBody CaseRequestDto caseRequestDto){

        Case updatedCase = caseService.updateCase(caseRequestDto);

        return ResponseEntity.ok(new CommonResponseDto<>("Case updated successfully ", HttpStatus.OK, updatedCase));

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

    @DeleteMapping("/deleteCase/{id}")
    public ResponseEntity<CommonResponseDto<Case>> deleteCaseById(@PathVariable Long id){
        caseService.deleteCaseById(id);
        return ResponseEntity.ok(new CommonResponseDto<>("Case deleted successfully. ", HttpStatus.OK, null));
    }


}
