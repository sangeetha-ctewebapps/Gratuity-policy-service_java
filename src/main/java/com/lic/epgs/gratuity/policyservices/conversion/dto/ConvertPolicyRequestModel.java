package com.lic.epgs.gratuity.policyservices.conversion.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
public class ConvertPolicyRequestModel {

	
	
	
	
	
	
	private String sourcePolicyNo;
	private String targetPolicyNo;
	private String sourceVersion;
	private String targetVersion;
	private String mphCode;
	private Double debitConversionOutGoAmount;
	private Double debitDepositAmount;
	private Double creditConversionInAmount;
	private Double creditSinglePremiumFirstYearAmount;
	private Double creditFirstPremiumOtherAmount;
	private Double creditSinglePremiumSubsequentAmount;
	private Double creditRenewalPremiumOthersAmount;
	private Double creditLifePremiumOYRGTAAmount;
	private Double creditOtherFirstYearOYRGTAAmount;
	private Double creditGstOnPremiumAmount;
	private Double creditLateFeeAmount;
	private String isLegacy;
	private int adjustmentNo;
	private int iCodeForLob;
	private int iCodeForProductLine;
	private String iCodeForVariant;
	private int iCodeForBusinessType;
	private int iCodeForParticipatingType;
	private int iCodeForBusinessSegment;
	private int iCodeForInvestmentPortfolio;
	private String productCode;
	private String variantCode;
	private String operatingUnitType;
	private String unitCode;
	private String userCode;

	private TrnRegisModel trnRegisModel;

	private GstDetailModel gstDetailModel;

}
