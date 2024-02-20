package com.lic.epgs.gratuity.accountingservice.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
public class RenewalContrinAdjustDebitCreditRequestModelDto {
	
	private String collectionNo;
	private double depositDebitAmount;
	private double singlePremiumSubsequentCreditAmount;
	private double renewalPremiumOtherscreditAmount;
	private double OYRGTARenewalPremiumCreditAmount;
	private double gstOnPremiumCreditAmount;
	private double lateFeeCreditAmount;


}
