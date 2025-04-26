package com.advocate.service;

import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.advocate.dto.request.CaseRequestDto;
import com.advocate.entity.Case;
import com.advocate.entity.Client;
import com.advocate.entity.User;
import com.advocate.enums.CaseStatus;
import com.advocate.exception.EntityAlreadyExistsException;
import com.advocate.repository.CaseRepository;
import com.advocate.repository.ClientRepository;
import com.advocate.repository.UserRepository;

import jakarta.persistence.EntityNotFoundException;

@Service
public class CaseService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CaseRepository caseRepository;

    @Autowired
    private ClientRepository clientRepository;

    // Get All Cases
    public List<Case> getAllCases() {
        return caseRepository.findAll();
    }

    // Add Case
    public Case addCase(CaseRequestDto caseRequestDto) throws EntityAlreadyExistsException {

        if (caseRequestDto.getCaseId() != null && caseRepository.existsById(caseRequestDto.getCaseId())) {
            throw new EntityAlreadyExistsException("Case with ID already exists.");
        }

        Case newCase = new Case();

        Client client = clientRepository.findById(caseRequestDto.getClientId())
                .orElseThrow(() -> new EntityNotFoundException("Client not found with given ID."));

        User user = userRepository.findById(caseRequestDto.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));
        newCase.setUser(user);

        newCase.setClients(List.of(client));
        newCase.setCaseNo(caseRequestDto.getCaseNo());
        newCase.setCaseType(caseRequestDto.getCaseType());
        newCase.setCourtName(caseRequestDto.getCourtName());
        newCase.setJudgeName(caseRequestDto.getJudgeName());
        newCase.setSection(caseRequestDto.getSection());
        newCase.setVersus(caseRequestDto.getVersus());
        newCase.setCaseFileDate(caseRequestDto.getCaseFileDate());
        newCase.setPresentDate(caseRequestDto.getPresentDate());
        newCase.setLastDate(caseRequestDto.getLastDate());
        newCase.setHearingDate(caseRequestDto.getHearingDate());
        newCase.setCaseDescription(caseRequestDto.getCaseDescription());
        newCase.setPriority(caseRequestDto.getPriority());
        newCase.setRemarks(caseRequestDto.getRemarks());
        newCase.setCaseOutcome(caseRequestDto.getCaseOutcome());
        newCase.setStatus(caseRequestDto.getStatus() != null ? caseRequestDto.getStatus() : "ACTIVE");

        newCase.setCaseStatus(caseRequestDto.getCaseStatus() != null ? caseRequestDto.getCaseStatus() : CaseStatus.NEW);

        if (caseRequestDto.getCaseOrderStatus() == null) {
            throw new IllegalArgumentException("CaseOrderStatus is mandatory.");
        }
        newCase.setCaseOrderStatus(caseRequestDto.getCaseOrderStatus());

        return caseRepository.save(newCase);
    }

    // Update Case
    public Case updateCase(CaseRequestDto caseRequestDto) {

        Case existCases = caseRepository.findById(caseRequestDto.getCaseId())
                .orElseThrow(() -> new EntityNotFoundException("Case with this Case Id not found"));

        if (caseRequestDto.getUserId() != null) {
            User user = userRepository.findById(caseRequestDto.getUserId())
                    .orElseThrow(() -> new RuntimeException("User not found"));
            existCases.setUser(user);
        }

        existCases.setCaseNo(checkAndUpdateValueIfPresent(existCases.getCaseNo(), caseRequestDto.getCaseNo()));
        existCases.setCaseType(checkAndUpdateValueIfPresent(existCases.getCaseType(), caseRequestDto.getCaseType()));
        existCases.setCourtName(checkAndUpdateValueIfPresent(existCases.getCourtName(), caseRequestDto.getCourtName()));
        existCases.setJudgeName(checkAndUpdateValueIfPresent(existCases.getJudgeName(), caseRequestDto.getJudgeName()));
        existCases.setSection(checkAndUpdateValueIfPresent(existCases.getSection(), caseRequestDto.getSection()));
        existCases.setVersus(checkAndUpdateValueIfPresent(existCases.getVersus(), caseRequestDto.getVersus()));
        existCases.setCaseFileDate(
                checkAndUpdateValueIfPresent(existCases.getCaseFileDate(), caseRequestDto.getCaseFileDate()));
        existCases.setPresentDate(
                checkAndUpdateValueIfPresent(existCases.getPresentDate(), caseRequestDto.getPresentDate()));
        existCases.setLastDate(checkAndUpdateValueIfPresent(existCases.getLastDate(), caseRequestDto.getLastDate()));
        existCases.setHearingDate(
                checkAndUpdateValueIfPresent(existCases.getHearingDate(), caseRequestDto.getHearingDate()));
        existCases.setCaseDescription(
                checkAndUpdateValueIfPresent(existCases.getCaseDescription(), caseRequestDto.getCaseDescription()));
        existCases.setPriority(checkAndUpdateValueIfPresent(existCases.getPriority(), caseRequestDto.getPriority()));
        existCases.setRemarks(checkAndUpdateValueIfPresent(existCases.getRemarks(), caseRequestDto.getRemarks()));
        existCases.setCaseOutcome(
                checkAndUpdateValueIfPresent(existCases.getCaseOutcome(), caseRequestDto.getCaseOutcome()));

        if (caseRequestDto.getCaseStatus() != null) {
            existCases.setCaseStatus(caseRequestDto.getCaseStatus());
        }

        if (caseRequestDto.getCaseOrderStatus() == null) {
            throw new IllegalArgumentException("CaseOrderStatus is mandatory.");
        }
        existCases.setCaseOrderStatus(caseRequestDto.getCaseOrderStatus());

        return caseRepository.save(existCases);
    }

    private String checkAndUpdateValueIfPresent(String existingValue, String newValue) {
        return (newValue == null || newValue.trim().isEmpty()) ? existingValue : newValue;
    }

    // Delete Case By Id
    public void deleteCaseById(Long id) {
        Case caseEntity = caseRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Case not found with the given ID."));

        if ("INACTIVE".equalsIgnoreCase(caseEntity.getStatus())) {
            throw new IllegalStateException("Case is already deleted.");
        }

        caseEntity.setStatus("INACTIVE");
        caseRepository.save(caseEntity);
    }

    // Search Cases
    public List<Case> searchCases(String searchType, String searchValue, Long caseId) {
        switch (searchType.toLowerCase()) {
            case "date":
                try {
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
                    formatter.parse(searchValue);
                } catch (DateTimeParseException e) {
                    throw new IllegalArgumentException("Invalid date format. Please use dd-MM-yyyy");
                }
            case "case_no":
                return caseRepository.findByCaseNoContainingIgnoreCase(searchValue);
            case "status":
                CaseStatus status = CaseStatus.valueOf(searchValue.toUpperCase());
                return caseRepository.findByCaseStatus(status);
            case "client_name":
                return caseRepository.findByClientName(searchValue);
            case "all_dates":
                return caseRepository.findByPresentDate(searchValue); // Adjust this based on your logic for all dates
            case "case_id":
                if (caseId != null) {
                    return caseRepository.findById(caseId).stream().collect(Collectors.toList());
                } else {
                    throw new IllegalArgumentException("Case ID is required for this filter.");
                }
            default:
                throw new IllegalArgumentException("Invalid filter type");

        }
    }

}
