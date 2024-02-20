package com.lic.epgs.gratuity.accountingservice.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
public class GlTransactionModelDto {

	private String bankAccountType;
	private Long iCodeForBusinessSegment;
	private Long iCodeForBusinessType;
	private Long iCodeForInvestmentPortfolio;
	private Long iCodeForLob;
	private Long iCodeForParticipatingType;
	private Long iCodeForProductLine;
	private String iCodeForVariant;
	private String operatingUnit;
	private String operatingUnitType;
	private String subRefType;
	private String narration;
	
}
