package com.lic.epgs.gratuity.policy.renewalpolicy.member.entity;

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

import com.lic.epgs.gratuity.policy.member.entity.PolicyMemberAddressEntity;
import com.lic.epgs.gratuity.policy.member.entity.PolicyMemberAppointeeEntity;
import com.lic.epgs.gratuity.policy.member.entity.PolicyMemberBankAccount;
import com.lic.epgs.gratuity.policy.member.entity.PolicyMemberNomineeEntity;

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
public class RenewalPolicyTMPMemberEntity {
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "PMST_MEMBER_ID_SEQ")
	@SequenceGenerator(name = "PMST_MEMBER_ID_SEQ", sequenceName = "PMST_MEMBER_ID_SEQ", allocationSize = 1)
	@Column(name = "MEMBER_ID")
    private Long id;
    
    @OneToMany(mappedBy="member", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Set<RenewalPolicyTMPMemberAddressEntity> addresses;
    
    @OneToMany(mappedBy="member", cascade = CascadeType.ALL)
    private Set<RenewalPolicyTMPMemberAppointeeEntity> appointees;
    
    @OneToMany(mappedBy="member", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Set<RenewalPolicyTMPMemberBankAccountEntity> bankAccounts;
    
    @OneToMany(mappedBy="member", cascade = CascadeType.ALL)
    private Set<RenewalPolicyTMPMemberNomineeEntity> nominees;
    

    
    @Column(name = "POLICY_ID")
    private Long policyId;
    
	@Column(name = "TMP_POLICY_ID")
	private Long tmpPolicyId;
	
    @Column(name = "ENDORSEMENT_ID")	
	private Long endorsementId;
    
    @Column(name = "PMST_MEMBER_ID")	
  	private Long pmstMemebrId;
    
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
    private Double lifeCoverPremium;
    
    @Column(name = "LC_SUM_ASSURED")
    private Double lifeCoverSumAssured;
    
    @Column(name = "ACCRUED_GRATUITY")
    private Double accruedGratuity;
    
    @Column(name = "TOTAL_GRATUITY")
    private Double totalGratuity;
    
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
    
    @Column(name = "RECORD_TYPE")
    private String recordType;
    
    @Column(name = "PROPOSAL_POLICY_NUMBER")	
	private String proposalPolicyNumber;
    
    @Column(name = "HAS_EXCEEDED_FCL")	
   	private Long hasExceededFCL;
    
    @Column(name = "HAS_GROUP_EXCEEDED_FCL")	
   	private Long hasGroupExceededFCL;
    
    @Column(name = "POLICY_SERVICE_ID")	
   	private Long policyServiceId;

    
    @Column(name = "PMST_HIS_MEMBER_ID")	
  	private Long pmstHistMemebrId;

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
    
    
    

