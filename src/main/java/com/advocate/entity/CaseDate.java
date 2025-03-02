package com.advocate.entity;

import com.advocate.enums.CaseOrderStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Table(name = "case_dates")
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class CaseDate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "case_id", referencedColumnName = "id", nullable = false)
    private Case caseEntity;

    @Column(name = "case_date", nullable = false)
    private String caseDate;

    @Column(name = "hearing_type")
    private String hearingType; // e.g., First Hearing, Final Hearing, etc.

    @Column(name = "hearing_outcome")
    private String hearingOutcome; // e.g., Adjourned, Judgment Delivered

    @Column(name = "next_hearing_date")
    private String nextHearingDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "`order`", nullable = false)
    private CaseOrderStatus order;

    @Column(name = "payment", nullable = false)
    private double payment;

    @Column(name = "remarks", length = 1000)
    private String remarks; // Additional notes

    @Column(name = "created_at", nullable = false, updatable = false)
    private String createdAt;

    @Column(name = "updated_at", nullable = false)
    private String updatedAt;

    @Column(name = "created_by")
    private Long createdBy;

    @Column(name = "updated_by")
    private Long updatedBy;

    private String status;

    @PrePersist
    public void setDefaultStatus() {
        if (this.status == null) {
            this.status = "ACTIVE";
        }
    }
}
