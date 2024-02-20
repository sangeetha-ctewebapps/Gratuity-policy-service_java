package com.lic.epgs.gratuity.policyservices.merger.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
public class GlTransactionModel {

	private String bankAccountType;
 	private String subRefType;
 	private Long iCodeForLob;
 	private Long iCodeForProductLine;
 	private String iCodeForVariant;
 	private Long iCodeForBusinessType;
 	private Long iCodeForParticipatingType;
 	private Long iCodeForBusinessSegment; //changed from int to Long
 	private Long iCodeForInvestmentPortfolio;
 	private String operatingUnitType;
 	private String operatingUnit;
 	private String narration;
 	private String isLegacy;
 	private String productCode;
 	private String variantCode;
 	private String unitCode;
 	private String userCode;
 	 
// 	private RegularGstDetailModel regularGstDetailModel;
}
