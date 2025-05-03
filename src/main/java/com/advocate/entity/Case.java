package com.advocate.entity;

import java.util.List;
import com.advocate.enums.CaseOrderStatus;
import com.advocate.enums.CaseStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Table(name = "cases")
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude ={"user"})
public class Case {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "case_no", nullable = false)
    private String caseNo;

    @Column(name = "case_type")
    private String caseType; // Criminal, Civil, Family, etc.

    @Column(name = "court_name")
    private String courtName;

    @Column(name = "judge_name")
    private String judgeName;

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

    @Column(name = "hearing_date")
    private String hearingDate; // Next hearing date

    @Column(name = "case_description", length = 2000)
    private String caseDescription;

    @Enumerated(EnumType.STRING)
    @Column(name = "case_order_status", nullable = false)
    private CaseOrderStatus caseOrderStatus;

    @Column(name = "priority")
    private String priority; // High, Medium, Low

    @Column(name = "remarks", length = 1000)
    private String remarks;

    @Column(name = "case_outcome")
    private String caseOutcome; // Verdict or case result

    @OneToMany(mappedBy = "caseEntity", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CaseDate> caseDates;

    @OneToMany(mappedBy = "caseEntity", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CaseDetail> caseDetails;

    @OneToMany(mappedBy = "caseEntity", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Client> clients;

    private String status;

    @PrePersist
    public void setDefaultStatus() {
        if (this.status == null) {
            this.status = "ACTIVE";
        }
    }
}
