package com.lic.epgs.gratuity.policy.claim.dto;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class TempPolicyClaimPropsDto {
	
	private Long id;
	private Long tmpPolicyId;
	private Long tmpMemberId;
	private String claimType;
	private Long modeOfExit;
	private String onboardNumber;
	private String initimationNumber;
	private Long claimStatusId;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy", timezone = "asia/kolkata")
	private Date dateOfExit;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy", timezone = "asia/kolkata")
	private Date dateOfDeath;
	private Long reasonForDeathId;
	private String reasonForDeathOther;
	private Long reasonForSubeventDeathId;
	private Long reasonForWithdrawalId;
	private String placeOfDeath;
	private Double salaryAsOnDateOfExit;
	private Double gratuityAmtOnDateExit;
	private Double modifiedGratuityAmount;
	private Double lcSumAssured;
	private Double isPremiumRefund;
	private Double refundPremiumAmount;
	private Double penalAmount;
	private Double courtAward;
	private Double isClaimFromReceived;
	private Long isDeathCertificateReceived;
	//get MasterPolicyId
	private Long pmstPolicyId;
	private Long pmstMemberId;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy", timezone = "asia/kolkata")
	private Date onboardingDate;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy", timezone = "asia/kolkata")
	private Date intimationDate;
	private String payoutNo;
	private Boolean isActive;
	private String createdBy;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy", timezone = "asia/kolkata")
	private Date createdDate;
	private String modifiedBy;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy", timezone = "asia/kolkata")
	private Date modifiedDate;
	private Long batchId;
	private Double refundGSTAmount;
	private List<ClaimPropSearchDetailDto> claimPropSearchDetailDto;
	
	private String licId;
	private String panNumber;
	private String aadharNumber;
	private String employeeCode;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy", timezone = "asia/kolkata")
	private Date payoutDate;
//	private String policyNumber;
	private String mphCode;
	
	
}
