package com.lic.epgs.gratuity.accountingservice.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
public class ClaimReverseLiabilityReqDto {

	private String mphCode;
	private String policyNo;
	private String refNo;
	private String unitCode;
	private String userCode;
	private Double debitOutstandingAccount;
	private Double debitSrAccount;
	private Double creditFundAmount;
	
	private Double creditReFundOfOtherFirstYearPremiumOYRGTA;
	private Double creditConsumerForumAwardExpenses=0.0;
	private Double creditConsumerForumAward=0.0;
	private Double creditLifeCoverSumAssuredPayable=0.0;
	private Double creditReFundOfFirstPremiumOYRGTA;
	private Double creditReFundOfOtherFirstPremiumOYRGTA;
	private Double creditReFundOfRenewalPremiumOYRGTA;
	private Double creditSrAccount;
	private Double creditPenalInterest;
	
	private Double debitOutStandingAmount;
	private Double creditSRAmount;
	private Double creditReFundFirstPremiumOYRGTA;
	private Double creditReFundOtherFirstPremiumOYRGTA;
	private Double creditReFundRenewalPremiumOYRGTA;
	private Double debitSRAmount;
	
	
	
	private GlTransactionModelDto glTransactionVO;
	private JournalVoucherDetailModelDto journalVoucherDetailModel;
}
