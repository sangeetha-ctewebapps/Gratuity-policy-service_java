package com.lic.epgs.gratuity.accountingservice.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
public class NewBusinessContriDebitCreditRequestModelDto {

	private String collectionNo;
	private Double depositDebitAmount;
	private Double singlePremiumFirstYearCreditAmount = 0.0;
	private Double firstPremiumCreditAmount = 0.0;
	private Double OYRGTAFirstPremiumCreditAmount = 0.0;
	private Double gstOnPremiumCreditAmount = 0.0;
	
	
	
	
}
