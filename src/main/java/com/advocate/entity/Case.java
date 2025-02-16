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
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Table(name = "cases")
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
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

	private String status ;

	@PrePersist // ✅ Automatically sets default before persisting
    public void setDefaultStatus() {
        if (this.status == null) {
            this.status = "ACTIVE";
        }
    }
  
}
