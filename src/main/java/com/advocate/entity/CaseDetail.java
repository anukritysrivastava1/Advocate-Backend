package com.advocate.entity;

import com.advocate.enums.CaseOrderStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
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

    @Column(name = "files")
    private String files;

    @Column(name = "created_at", nullable = false, updatable = false)
    private String createdAt;

    @Column(name = "updated_at", nullable = false)
    private String updatedAt;

    @Column(name = "created_by")
    private Long createdBy;

    @Column(name = "updated_by")
    private Long updatedBy;

	private String status;

	@PrePersist // ✅ Automatically sets default before persisting
    public void setDefaultStatus() {
        if (this.status == null) {
            this.status = "ACTIVE";
        }
    }
    
}
