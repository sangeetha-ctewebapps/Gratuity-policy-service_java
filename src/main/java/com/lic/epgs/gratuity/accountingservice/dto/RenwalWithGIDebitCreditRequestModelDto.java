package com.lic.epgs.gratuity.accountingservice.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
public class RenwalWithGIDebitCreditRequestModelDto {	
	private String collectionNo;
	private Double depositDebitAmount;
	private Double gstOnPremiumCreditAmount = 0.0;
	private Double lateFeeCreditAmount = 0.0;
	private Double lifePremiumOYRGTARenewalPremiumCreditAmount = 0.0;
	private Double otherFirstYearOYRGTALifePremiumCreditAmount = 0.0;
	private Double firstPremiumOtherCurrentServiceCreditAmount = 0.0;   //if renewal FALSE
;
	private Double renewalPremiumOthersCurrentServiceCreditAmount = 0.0;  //if renewal true
	private Double singlePremiumFirstYearPastServiceCreditAmount = 0.0;   //if renewal FALSE
	private Double singlePremiumSubsequentPastServiceCreditAmount = 0.0;  //if renewal true
	
}
