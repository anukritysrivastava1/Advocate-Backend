package com.advocate.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.advocate.entity.Case;
import com.advocate.repository.CaseRepository;

public class CaseService {

    @Autowired
    private CaseRepository caseRepository;

    public List<Case> getAllCases() {

        return caseRepository.findAll();
    }

    public List<Case> getCasesByDate(String date) {

        return caseRepository.findByPresentDate(date);
    }

    public List<Case> getPreviousCases(String date) {

        return caseRepository.findByLastDateBefore(date);
    }

    public List<Case> getUpcomingCases(String date) {

        return caseRepository.findByPresentDateAfter(date);
    }

    // public Case addCase(){




    //     return Case;
    // }

}
