package com.lic.epgs.gratuity.policy.claim.entity;

import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "PMST_TMP_MEMBER")
public class TempMemberSearchEntity {
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "PMST_TMP_MEMBER_ID_SEQ")
	@SequenceGenerator(name = "PMST_TMP_MEMBER_ID_SEQ", sequenceName = "PMST_TMP_MEMBER_ID_SEQ", allocationSize = 1)
	@Column(name = "MEMBER_ID")
    private Long id;
	
	@Column(name = "LIC_ID")
	private String licId;
	
	@Column(name = "PAN_NUMBER")
	private String panNumber;
	
    @Column(name = "FIRST_NAME")
    private String firstName;

	@Column(name = "AADHAR_NUMBER")
	private String aadharNumber;
	
	@Column(name = "EMPLOYEE_CODE")
	private String employeeCode;

	@Column(name = "IS_ACTIVE")
	private Boolean isActive;
	
	@Column(name = "DOJ_TO_SCHEME")
	private Date dateOfJoining;
	
	@Column(name = "DATE_OF_APPOINTMENT")
	private Date dateOfAppointment;
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "TMP_POLICY_ID", referencedColumnName = "POLICY_ID")
	private PolicyTempSearchEntity policyTmp; 
//	@OneToOne(cascade = CascadeType.ALL)
//    @JoinColumn(name="CLAIM_PROPS_ID", nullable=false,referencedColumnName = "CLAIM_PROPS_ID")
//    private TempPolicyClaimPropsSearchEntity claimPropsSearch;
	
  
	
//	 (in master table)
	@OneToMany(mappedBy = "memberTmp", cascade = CascadeType.ALL)
	private List<TempPolicyClaimPropsSearchEntity> memberSearch;   

}
