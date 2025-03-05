package com.advocate.entity;

import com.advocate.enums.CaseOrderStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Table(name = "case_details")
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class CaseDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "case_id", referencedColumnName = "id", nullable = false)
    private Case caseEntity;

    @Column(name = "case_date", nullable = false)
    private String caseDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "`order`", nullable = false)
    private CaseOrderStatus order;

    @Column(name = "payment", nullable = false)
    private double payment;

    @Column(name = "document_type")
    private String documentType; // e.g., FIR, Judgment, Petition

    @Column(name = "files")
    private String files; // File path or URL

    @Column(name = "uploaded_by")
    private Long uploadedBy; // ID of the person who uploaded the file

    @Column(name = "review_status")
    private String reviewStatus; // Pending, Reviewed, Rejected

    @Column(name = "document_description", length = 2000)
    private String documentDescription; // Notes on the document

    @Column(name = "case_progress", length = 2000)
    private String caseProgress; // Updates about case progress

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
