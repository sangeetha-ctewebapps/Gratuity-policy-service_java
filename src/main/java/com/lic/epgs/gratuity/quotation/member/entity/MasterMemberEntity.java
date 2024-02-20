package com.lic.epgs.gratuity.quotation.member.entity;

import java.util.Date;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author Ismail Khan A
 *
 */

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "QMST_MEMBER")
public class MasterMemberEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "QMST_MEMBER_ID_SEQ")
	@SequenceGenerator(name = "QMST_MEMBER_ID_SEQ", sequenceName = "QMST_MEMBER_ID_SEQ", allocationSize = 1)
	@Column(name = "MEMBER_ID")
	private Long id;

	@Column(name = "MEMBER_BATCH_ID")
	private Long memberBatchId;

	@OneToMany(mappedBy = "member", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	private Set<MasterMemberAddressEntity> addresses;

	@OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
	private Set<MasterMemberAppointeeEntity> appointees;

	@OneToMany(mappedBy = "member", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	private Set<MasterMemberBankAccount> bankAccounts;

	@OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
	private Set<MasterMemberNomineeEntity> nominees;

	@Column(name = "QUOTATION_ID")
	private Long quotationId;

	@Column(name = "PROPOSAL_POLICY_NUMBER")	
	private String proposalPolicyNumber;
	 
	@Column(name = "LIC_ID")
	private String licId;

	@Column(name = "EMPLOYEE_CODE")	
    private String employeeCode;

	@Column(name = "FIRST_NAME")
	private String firstName;

	@Column(name = "MIDDLE_NAME")
	private String middleName;

	@Column(name = "LAST_NAME")
	private String lastName;

	@Column(name = "DATE_OF_BIRTH")
	private Date dateOfBirth;

	@Column(name = "DATE_OF_APPOINTMENT")	
    private Date dateOfAppointment;
	
	@Column(name = "DOJ_TO_SCHEME")	
    private Date dojToScheme;

	@Column(name = "CATEGORY_ID")
	private Long categoryId;

	@Column(name = "BASIC_SALARY")
	private Double basicSalary;

	@Column(name = "GENDER_ID")
	private Long genderId;
	
	@Column(name = "SALARY_FREQUENCY")	
    private String salaryFrequency;

	@Column(name = "PAN_NUMBER")
	private String panNumber;

	@Column(name = "AADHAR_NUMBER")
	private String aadharNumber;

	@Column(name = "LANDLINE_NO")
	private Long landlineNo;

	@Column(name = "MOBILE_NO")
	private Long mobileNo;

	@Column(name = "EMAIL_ID")
	private String emailId;

	@Column(name = "DESIGNATION")
	private String designation;

	@Column(name = "FATHER_NAME")
	private String fatherName;

	@Column(name = "SPOUSE_NAME")
	private String spouseName;
	
	 @Column(name = "GRATCALCULATION_ID")
	 private Long gratCalculationId;
	 
	@Column(name = "STATUS_ID")
	private Long statusId;

	@Column(name = "LC_PREMIUM")
	private float lifeCoverPremium;

	@Column(name = "LC_SUM_ASSURED")
	private float lifeCoverSumAssured;

	@Column(name = "ACCRUED_GRATUITY")
	private float accruedGratuity;

	@Column(name = "TOTAL_GRATUITY")
	private float totalGratuity;

	@Column(name = "VALUATION_DATE")
	private Date valuationDate;

	@Column(name = "COMMUNICATION_PREFERENCE")
	private String communicationPreference;

	@Column(name = "LANGUAGE_PREFERENCE")
	private String languagePreference;

	@Column(name = "IS_ACTIVE")
	private Boolean isActive;

	@Column(name = "CREATED_BY")
	private String createdBy;

	@Column(name = "CREATED_DATE")
	private Date createdDate;

	@Column(name = "MODIFIED_BY")
	private String modifiedBy;

	@Column(name = "MODIFIED_DATE")
	private Date modifiedDate;
}
