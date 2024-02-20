package com.lic.epgs.gratuity.accountingservice.dto;

import lombok.Data;

@Data
public class DebitCreditMemberSurrenderModel {
	
	private Double debitReFundOfFirstPremiumAmount;
	private Double debitReFundOfOtherFirstYearPremiumAmount;
	private Double debitReFundOfRenewalPremiumAmount;
	private Double debitGstLiabilityAmount;
	private Double creditCoPaymentContraAmount;
	private Double debitCoPaymentContraAmount;
	private Double creditOsPaymentAmount;
}
