package com.advocate.entity;
import com.advocate.enums.Role;
import com.fasterxml.jackson.annotation.JsonIgnore;

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
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Table(name = "clients")
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Client {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = true)
    private User user;

    @ManyToOne
    @JoinColumn(name = "case_id", nullable = true)
    private Case caseEntity;

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "mobile")
    private String mobile;

    @Column(name = "email")
    private String email;

    @Column(name = "password")
	private String password;

    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    private Role role;

    @JsonIgnore
	@OneToOne(cascade = CascadeType.ALL, optional = true) // Cascade ensures Address gets persisted with User
	@JoinColumn(name = "address_id", referencedColumnName = "id", nullable = true)
	private Address address;

    @Column(name = "identity_no")
    private String identityNo;

    @Column(name = "vehicle_no")
    private String vehicleNo;

    @Column(name = "created_at", updatable = false)
    private String createdAt;

    @Column(name = "updated_at")
    private String updatedAt;

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
