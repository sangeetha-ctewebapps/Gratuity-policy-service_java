package com.lic.epgs.gratuity.policy.renewalpolicy.valuationmatrix.dto;

import java.util.Date;

import javax.persistence.Column;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.lic.epgs.gratuity.policy.renewalpolicy.valuation.dto.RenewalValuationTMPDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class RenewalValuationBasicTMPDto {

	private Long id;
	private Long policyId;
	private Long valuationTypeId;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy", timezone = "asia/kolkata")
	private Date valuationEffectivDate;
	private String referenceNumber;
	private Long maximumService;
	private Long minimumServiceForWithdrawal;
	private Long minimumServiceForDeath;
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
	private Boolean isActive;
	private String createdBy;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy", timezone = "asia/kolkata")
	private Date createdDate;
	private String modifiedBy;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy", timezone = "asia/kolkata")
	private Date modifiedDate;
	private Long noOfYrsAocm;
	private Double ibnrValue;
	private Double stdPremiumRateAocm;
	private Double modifiedPremiumRateAocm;
	private Double stdPremRateCrdbiltyFctr;
	private Double modfdPremRateCrdbiltyFctr;
	private Long tmpPolicyId;
	private Long pmstValuationBasicId;
	private Double totalSumAssured;
	private Double totalPremium;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy", timezone = "asia/kolkata")
	private Date annualRenewalDate;

	private String reasonForChange;
	private String uoComments;
	private String amountOfPending;
	

}
