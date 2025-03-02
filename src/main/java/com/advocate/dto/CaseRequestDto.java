package com.advocate.dto;


import com.advocate.enums.CaseStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CaseRequestDto {

    private Long userId; 

    private String caseNo;
    private String caseType; // Criminal, Civil, Family, etc.
    private String courtName;
    private String judgeName;
    private String section;
    private String versus;
    private String caseFileDate;
    private CaseStatus caseStatus; // Ongoing, Closed, etc.
    private String hearingDate; // Next hearing date
    private String caseDescription;
    private String priority; // High, Medium, Low
    
    private String status; // Default: ACTIVE
}
