package com.lic.epgs.gratuity.policy.renewalpolicy.valuationmatrix.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
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
@Table(name = "PMST_TMP_VALUATIONBASIC")
public class RenewalValuationBasicTMPEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "PMST_TMP_VAL_BASIC_ID_SEQ")
	@SequenceGenerator(name = "PMST_TMP_VAL_BASIC_ID_SEQ", sequenceName = "PMST_TMP_VAL_BASIC_ID_SEQ", allocationSize = 1)
	@Column(name = "VALUATIONBASIC_ID ")
	private Long id;
	
	@Column(name = "POLICY_ID ")
	private Long policyId;
	
	@Column(name = "VALUATION_TYPE_ID ")
	private Long valuationTypeId;
	
	@Column(name = "VALUATION_EFFECTIVE_DATE ")
	private Date valuationEffectivDate;
	
	@Column(name = "REFERENCE_NUMBER ")
	private String referenceNumber;

	@Column(name = "MAXIMUM_SERVICE ")
	private Long maximumService;
	
	@Column(name = "MINIMUM_SERVICE_FOR_DEATH ")
	private Long minimumServiceForDeath;
	
	@Column(name = "MINIMUM_SERVICE_FOR_WITHDRAWAL ")
	private Long minimumServiceForWithdrawal;
	
	@Column(name = "MINIMUM_SERVICE_FOR_RETIREMENT ")
	private Long minimumServiceForRetirement;
	
	@Column(name = "NO_OF_LIVES ")
	private Integer numberOfLives;
	
	@Column(name = "GRATUITY_AMOUNT_CELLING ")
	private Double gratuityAmountCelling;
	
	@Column(name = "RATE_TABLE ")
	private String rateTable;
	
	@Column(name = "MAX_LIFE_COVER_SUM_ASSURED ")
	private Double maxLifeCoverSumAssured;
	
	@Column(name = "MAX_SALARY ")
	private Double maxSalary;
	
	@Column(name = "PAST_SERVICE_DEATH ")
	private Double pastServiceDeath;
	
	@Column(name = "PAST_SERVICE_WITHDRAWAL ")
	private Double pastServiceWithdrawal;
	
	@Column(name = "PAST_SERVICE_RETIREMENT ")
	private Double pastServiceRetirement;
	
	@Column(name = "CURRENT_SERVICE_DEATH ")
	private Double currentServiceDeath;

	@Column(name = "CURRENT_SERVICE_WITHDRAWAL ")
	private Double currentServiceWithdrawal;
	
	@Column(name = "CURRENT_SERVICE_RETIREMENT ")
	private Double currentServiceRetirement;
	
	@Column(name = "ACCRUED_GRATUITY ")
	private Double accruedGratuity;
	
	@Column(name = "TOTAL_GRATUITY ")
	private Double totalGratuity;
	
	@Column(name = "MAXIMUM_SALARY ")
	private Double maximumSalary;
	
	@Column(name = "MINIMUM_SALARY ")
	private Double minimumSalary;

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
	
	@Column(name = "NO_OF_YRS_AOCM")
	private Long noOfYrsAocm;
	
	@Column(name = "IBNR_VALUE")
	private Double ibnrValue;
	
	@Column(name = "STD_PREMIUM_RATE_AOCM")
	private Double stdPremiumRateAocm;
	
	@Column(name = "MODIFIED_PREMIUM_RATE_AOCM")
	private Double modifiedPremiumRateAocm;
	
	@Column(name = "STD_PREM_RATE_CRDBILTY_FCTR")
	private Double stdPremRateCrdbiltyFctr;
	
	@Column(name = "MODFD_PREM_RATE_CRDBILTY_FCTR")
	private Double modfdPremRateCrdbiltyFctr;
	
	@Column(name = "TMP_POLICY_ID")
	private Long tmpPolicyId;
	
	@Column(name = "PMST_VALUATIONBASIC_ID")
	private Long pmstValuationBasicId;
	
	@Column(name = "TOTAL_SUM_ASSURED")
	private Double totalSumAssured;
	
	@Column(name = "TOTAL_PREMIUM")
	private Double totalPremium;
	
	@Column(name = "ANNUAL_RENEWAL_DATE")
	private Date annualRenewalDate;
	
	@Column(name = "PMST_HIS_VALUATIONBASIC_ID")
	private Long pmstHisValuationBasicId;
	
	@Column(name="REASON_FOR_CHANGE")
	private String reasonForChange;
	
	@Column(name="UO_COMMENTS")
	private String uoComments;
	
	@Column(name="AMOUNT_OF_PENDING")
	private String amountOfPending;
		
}
