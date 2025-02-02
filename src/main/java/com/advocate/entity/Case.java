package com.advocate.entity;

import java.util.List;

import com.advocate.enums.CaseOrderStatus;
import com.advocate.enums.CaseStatus;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "cases")
public class Case {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "case_no", nullable = false)
    private String caseNo;

    @Column(name = "section")
    private String section;

    @Column(name = "versus")
    private String versus;

    @Column(name = "case_file_date")
    private String caseFileDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "case_status", nullable = false)
    private CaseStatus caseStatus;

    @Column(name = "present_date")
    private String presentDate;

    @Column(name = "last_date")
    private String lastDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "case_order_status", nullable = false)
    private CaseOrderStatus caseOrderStatus;

    @OneToMany(mappedBy = "caseEntity", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CaseDate> caseDates;

    @OneToMany(mappedBy = "caseEntity", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CaseDetail> caseDetails;

    @OneToMany(mappedBy = "caseEntity", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Client> clients;

	public Case() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public String getCaseNo() {
		return caseNo;
	}

	public void setCaseNo(String caseNo) {
		this.caseNo = caseNo;
	}

	public String getSection() {
		return section;
	}

	public void setSection(String section) {
		this.section = section;
	}

	public String getVersus() {
		return versus;
	}

	public void setVersus(String versus) {
		this.versus = versus;
	}

	public String getCaseFileDate() {
		return caseFileDate;
	}

	public void setCaseFileDate(String caseFileDate) {
		this.caseFileDate = caseFileDate;
	}

	public CaseStatus getCaseStatus() {
		return caseStatus;
	}

	public void setCaseStatus(CaseStatus caseStatus) {
		this.caseStatus = caseStatus;
	}

	public String getPresentDate() {
		return presentDate;
	}

	public void setPresentDate(String presentDate) {
		this.presentDate = presentDate;
	}

	public String getLastDate() {
		return lastDate;
	}

	public void setLastDate(String lastDate) {
		this.lastDate = lastDate;
	}

	public CaseOrderStatus getCaseOrderStatus() {
		return caseOrderStatus;
	}

	public void setCaseOrderStatus(CaseOrderStatus caseOrderStatus) {
		this.caseOrderStatus = caseOrderStatus;
	}

	public List<CaseDate> getCaseDates() {
		return caseDates;
	}

	public void setCaseDates(List<CaseDate> caseDates) {
		this.caseDates = caseDates;
	}

	public List<CaseDetail> getCaseDetails() {
		return caseDetails;
	}

	public void setCaseDetails(List<CaseDetail> caseDetails) {
		this.caseDetails = caseDetails;
	}

	public List<Client> getClients() {
		return clients;
	}

	public void setClients(List<Client> clients) {
		this.clients = clients;
	}

	@Override
	public String toString() {
		return "Case [id=" + id + ", user=" + user + ", caseNo=" + caseNo + ", section=" + section + ", versus="
				+ versus + ", caseFileDate=" + caseFileDate + ", caseStatus=" + caseStatus + ", presentDate="
				+ presentDate + ", lastDate=" + lastDate + ", caseOrderStatus=" + caseOrderStatus + ", caseDates="
				+ caseDates + ", caseDetails=" + caseDetails + ", clients=" + clients + "]";
	}

	public Case(Long id, User user, String caseNo, String section, String versus, String caseFileDate,
			CaseStatus caseStatus, String presentDate, String lastDate, CaseOrderStatus caseOrderStatus,
			List<CaseDate> caseDates, List<CaseDetail> caseDetails, List<Client> clients) {
		super();
		this.id = id;
		this.user = user;
		this.caseNo = caseNo;
		this.section = section;
		this.versus = versus;
		this.caseFileDate = caseFileDate;
		this.caseStatus = caseStatus;
		this.presentDate = presentDate;
		this.lastDate = lastDate;
		this.caseOrderStatus = caseOrderStatus;
		this.caseDates = caseDates;
		this.caseDetails = caseDetails;
		this.clients = clients;
	}

  
}
