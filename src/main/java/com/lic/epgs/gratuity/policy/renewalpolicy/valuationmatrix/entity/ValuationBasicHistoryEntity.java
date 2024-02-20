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
@Table(name = "PMST_HIS_VALUATIONBASIC")
public class ValuationBasicHistoryEntity {

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
	
	@Column(name = "ANNUAL_RENEWAL_DATE")
	private Date annualRenewalDate;
	
	@Column(name = "TOTAL_SUM_ASSURED ")
	private Long totalSumAssured;
	
	@Column(name = "TOTAL_PREMIUM ")
	private Long totalPremium;
	
	
	@Column(name = "REFERENCE_NUMBER ")
	private String referenceNumber;

	@Column(name = "MAXIMUM_SERVICE ")
	private Long maximumService;
	
	@Column(name = "MINIMUM_SERVICE_FOR_DEATH ")
	private Long maximumServiceForDeath;
	
	@Column(name = "MINIMUM_SERVICE_FOR_WITHDRAWAL ")
	private Long maximumServiceForWithdrawal;
	
	@Column(name = "MINIMUM_SERVICE_FOR_RETIREMENT ")
	private Long maximumServiceForRetirement;
	
	@Column(name = "NO_OF_LIVES ")
	private Long noOFLives;
	
	@Column(name = "GRATUITY_AMOUNT_CELLING ")
	private Long GratuityAmountCelling;
	
	@Column(name = "RATE_TABLE ")
	private String rateTable;
	
	@Column(name = "MAX_LIFE_COVER_SUM_ASSURED ")
	private Long maxLifeCoverSumAssured;
	
	@Column(name = "MAX_SALARY ")
	private Long maxSalary;
	
	@Column(name = "PAST_SERVICE_DEATH ")
	private Long pastServiceDeath;
	
	@Column(name = "PAST_SERVICE_WITHDRAWAL ")
	private Long pastServiceWithdrawal;
	
	@Column(name = "PAST_SERVICE_RETIREMENT ")
	private Long pastServiceForRetirement;
	
	@Column(name = "CURRENT_SERVICE_DEATH ")
	private Long currentServiceDeath;

	@Column(name = "CURRENT_SERVICE_WITHDRAWAL ")
	private Long currentServiceWithdrawal;
	
	@Column(name = "CURRENT_SERVICE_RETIREMENT ")
	private Long currentServiceRetirement;
	
	@Column(name = "ACCRUED_GRATUITY ")
	private Long accruedGratuity;
	
	@Column(name = "TOTAL_GRATUITY ")
	private Long totalGratuity;
	
	@Column(name = "MAXIMUM_SALARY ")
	private Long maximumSalary;
	
	@Column(name = "MINIMUM_SALARY ")
	private Long minimumsalary;

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
	private Long ibnrValue;
	
	@Column(name = "STD_PREMIUM_RATE_AOCM")
	private Long stdPremiumRateAocm;
	
	@Column(name = "MODIFIED_PREMIUM_RATE_AOCM")
	private Long modifiedPremiumRateAocm;
	
	@Column(name = "STD_PREM_RATE_CRDBILTY_FCTR")
	private Long stdPremRateCrdbiltyFctr;
	
	@Column(name = "MODFD_PREM_RATE_CRDBILTY_FCTR")
	private Long modfdPremRateCrdbiltyFctr;
	
	@Column(name = "TMP_POLICY_ID")
	private Long tmpPolicyId;
	
	@Column(name = "PMST_VALUATIONBASIC_ID")
	private Long pmstValuationBasicId;
	
}
