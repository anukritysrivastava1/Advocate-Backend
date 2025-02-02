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
import jakarta.persistence.Table;

@Entity
@Table(name = "case_details")
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

	public CaseDetail() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Case getCaseEntity() {
		return caseEntity;
	}

	public void setCaseEntity(Case caseEntity) {
		this.caseEntity = caseEntity;
	}

	public String getCaseDate() {
		return caseDate;
	}

	public void setCaseDate(String caseDate) {
		this.caseDate = caseDate;
	}

	public CaseOrderStatus getOrder() {
		return order;
	}

	public void setOrder(CaseOrderStatus order) {
		this.order = order;
	}

	public double getPayment() {
		return payment;
	}

	public void setPayment(double payment) {
		this.payment = payment;
	}

	public String getFiles() {
		return files;
	}

	public void setFiles(String files) {
		this.files = files;
	}

	public String getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(String createdAt) {
		this.createdAt = createdAt;
	}

	public String getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(String updatedAt) {
		this.updatedAt = updatedAt;
	}

	public Long getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(Long createdBy) {
		this.createdBy = createdBy;
	}

	public Long getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(Long updatedBy) {
		this.updatedBy = updatedBy;
	}

	public CaseDetail(Long id, Case caseEntity, String caseDate, CaseOrderStatus order, double payment, String files,
			String createdAt, String updatedAt, Long createdBy, Long updatedBy) {
		super();
		this.id = id;
		this.caseEntity = caseEntity;
		this.caseDate = caseDate;
		this.order = order;
		this.payment = payment;
		this.files = files;
		this.createdAt = createdAt;
		this.updatedAt = updatedAt;
		this.createdBy = createdBy;
		this.updatedBy = updatedBy;
	}

	@Override
	public String toString() {
		return "CaseDetail [id=" + id + ", caseEntity=" + caseEntity + ", caseDate=" + caseDate + ", order=" + order
				+ ", payment=" + payment + ", files=" + files + ", createdAt=" + createdAt + ", updatedAt=" + updatedAt
				+ ", createdBy=" + createdBy + ", updatedBy=" + updatedBy + "]";
	}

    
}
