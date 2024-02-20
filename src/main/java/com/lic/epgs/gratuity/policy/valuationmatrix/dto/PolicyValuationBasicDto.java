package com.lic.epgs.gratuity.policy.valuationmatrix.dto;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class PolicyValuationBasicDto {

	private Long id;
	private Long policyId;
	private Long valuationTypeId;
	
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy", timezone = "asia/kolkata")
	private Date valuationEffectivDate;
	private String referenceNumber;
	private Long maximumService;
	private Long minimumServiceForDeath;
	private Long minimumServiceForWithdrawal;
	private Long minimumServiceForRetirement;
	private Integer numberOfLives;
	private Float gratuityAmountCelling;
	private String rateTable;
	private Float maxLifeCoverSumAssured;
	private Float maxSalary;
	private Float	  pastServiceDeath;
	private Float pastServiceWithdrawal;
	private Float pastServiceRetirement;
	private Float currentServiceDeath;
	private Float currentServiceWithdrawal;
	private Float currentServiceRetirement;
	private Float accruedGratuity;
	private Float totalGratuity;
	private Float maximumSalary;
	private Float minimumSalary;
	private Double totalPremium;
	private Double totalSumAssured;
	private Float modfdPremRateCrdbilityFctr;
	private Float stdPremRateCrdbiltyFctr;
	private Float modifiedPremiumRateAOCM;
	private Float stdPremiumRateAOCM;
	private Date annualRenewalDate;
	
	private Long noOfYearAOCM;
	private Long ibnrValue;
	
	private Boolean isActive;

	private String createdBy;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy", timezone = "asia/kolkata")
	private Date createdDate;

	private String modifiedBy;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy", timezone = "asia/kolkata")
	private Date modifiedDate;

	
	
}
