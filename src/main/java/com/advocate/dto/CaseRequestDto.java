package com.advocate.dto;

import com.advocate.enums.CaseOrderStatus;
import com.advocate.enums.CaseStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class CaseRequestDto {


    private Long caseId;
    private Long clientId;
    private Long userId; // Added userId since Case entity has a User reference
    private String caseNo;
    private String caseType;
    private String courtName;
    private String judgeName;
    private String section;
    private String versus;
    private String caseFileDate;
    private CaseStatus caseStatus;
    private String presentDate; // Added missing presentDate
    private String lastDate; // Added missing lastDate
    private String hearingDate;
    private String caseDescription;
    private CaseOrderStatus caseOrderStatus;
    private String priority;
    private String remarks; // Added missing remarks
    private String caseOutcome; // Added missing caseOutcome
    private String status; // Added missing status field
}
