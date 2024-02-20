package com.lic.epgs.gratuity.policy.member.entity;

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
@Table(name = "PMST_MEMBER")
public class PolicyMemberEntity {
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "PMST_MEMBER_ID_SEQ")
	@SequenceGenerator(name = "PMST_MEMBER_ID_SEQ", sequenceName = "PMST_MEMBER_ID_SEQ", allocationSize = 1)
	@Column(name = "MEMBER_ID")
    private Long id;
    
    @OneToMany(mappedBy="member", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Set<PolicyMemberAddressEntity> addresses;
    
    @OneToMany(mappedBy="member", cascade = CascadeType.ALL)
    private Set<PolicyMemberAppointeeEntity> appointees;
    
    @OneToMany(mappedBy="member", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Set<PolicyMemberBankAccount> bankAccounts;
    
    @OneToMany(mappedBy="member", cascade = CascadeType.ALL)
    private Set<PolicyMemberNomineeEntity> nominees;
    
    @Column(name = "PROPOSAL_POLICY_NUMBER")	
	private String proposalPolicyNumber;
    
    @Column(name = "POLICY_ID")
    private Long policyId;
    
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
    
    @Column(name = "GENDER_ID")
    private Long genderId;
    
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
    
    @Column(name = "DATE_OF_APPOINTMENT")	
    private Date dateOfAppointment;
	
	@Column(name = "DOJ_TO_SCHEME")	
    private Date dojToScheme;
    
    @Column(name = "CATEGORY_ID")
    private Long categoryId;
    
    @Column(name = "SALARY_FREQUENCY")	
    private String salaryFrequency;
    
    @Column(name = "DESIGNATION")
    private String designation;
    
    @Column(name = "BASIC_SALARY")
    private Double basicSalary;
    
    @Column(name = "FATHER_NAME")
    private String fatherName;
    
	@Column(name = "SPOUSE_NAME")
	private String spouseName;

	@Column(name = "GRATCALCULATION_ID")
	private Long gratCalculationId;

	@Column(name = "STATUS_ID")
	private Long statusId;
    
    @Column(name = "LC_PREMIUM")
    private Float lifeCoverPremium;
    
    @Column(name = "LC_SUM_ASSURED")
    private Float lifeCoverSumAssured;
    
    @Column(name = "ACCRUED_GRATUITY")
    private Float accruedGratuity;
    
    @Column(name = "TOTAL_GRATUITY")
    private Float totalGratuity;
    
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
    
    @Column(name="RENEWAL_UPLOAD_TYPE")
    private String renewalUpdateType;
    
    @Column(name="LAST_PREMIUM_COLLECTED_DATE")
    private Date lastPremiumCollectedDate;
    
    @Column(name="NEW_LIFE")
    private String  newLife;
    
    @Column(name="GST_INVOICE_NUMBER")
    private String gstInvoiceNumber;
    
    @Column(name="GST_INVOICE_DATE")
    private Date gstInvoiceDate;
}
