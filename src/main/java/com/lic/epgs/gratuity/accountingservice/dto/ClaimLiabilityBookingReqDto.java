package com.lic.epgs.gratuity.accountingservice.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
public class ClaimLiabilityBookingReqDto {

	
	private String mphCode;
	private String policyNo;
	private String refNo;
	private String unitCode;
	private String userCode;	
	
	private Double creditOutstandingAccount;
	
	private Double creditSrAccount;
	
	private Double debitConsumerForumAwardExpenses=0.0;
	private Double debitConsumerForumAward=0.0;
	private Double debitFundAmount;
	private Double debitPenalInterest;
	private Double debitLifeCoverSumAssuredPayable=0.0;
	private Double debitReFundOfFirstPremiumOYRGTA;
	private Double debitReFundOfOtherFirstPremiumOYRGTA;
	private Double debitReFundOfOtherFirstYearPremiumOYRGTA;
	private Double debitReFundOfRenewalPremiumOYRGTA;
	private Double debitSrAccount;
	private GlTransactionModelDto glTransactionVO;
	private JournalVoucherDetailModelDto journalVoucherDetailModel;
	
	
	//Maturity
	private Double creditOutStandingAmount;
	private Double creditSRAmount;
	private Double debitReFundFirstPremiumOYRGTA;
	private Double debitReFundOtherFirstPremiumOYRGTA;
	private Double debitReFundRenewalPremiumOYRGTA;
	private Double debitSRAmount;
	
	
	
}
