package com.lic.epgs.gratuity.policy.renewalpolicy.aocmcalculation.dto;

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
public class AOCMDto {
	
	private int year;
	private Long noOFLives;
	private Long totalPremium;
	private Long totalSumAssured;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy", timezone = "asia/kolkata")
	private Date annualRenewalDate;
	
	private Long amountOfClaim;
	private Long amountOfDCPOurEnd;
	private Long amountOfDCPwithCustomer;
	private Long TotalAmountOfDeathClaims;
	private Long noOfClaim;
	private Float premiumRate;
	private String flag;
	
	
	
}
