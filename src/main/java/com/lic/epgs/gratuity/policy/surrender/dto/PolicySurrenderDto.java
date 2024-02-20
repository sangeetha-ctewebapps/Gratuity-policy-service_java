package com.lic.epgs.gratuity.policy.surrender.dto;

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
public class PolicySurrenderDto {

	private Long policyId;
	private String policyNumber;
	private Long productId;
	private String productName;
	private Long variantId;
	private String variantName;
	private Long policyStatusId;
	private String policyStatus; 
	private Long mphId;
	private String mphCode;
	private String mphName;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy", timezone = "Asia/Kolkata")
	private Date policyStartDate;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy", timezone = "Asia/Kolkata")
	private Date policyEndDate;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy", timezone = "Asia/Kolkata")
	private Date dateOfCommencementOfPolicy;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy", timezone = "Asia/Kolkata")
	private Date annualRenewalDate;
	private Long totalNumberOfLives;
	private String mphPAN;
	private String unitOffice;
	private Long surrenderNumber;
	private Long surrenderId;
	private String surrenderStatus;
	private Long noticePeriodInMonths;
	private Long waitingPeriodInMonths;
	private String isNoticePeriodEnded;
	private String surrenderType;
}
