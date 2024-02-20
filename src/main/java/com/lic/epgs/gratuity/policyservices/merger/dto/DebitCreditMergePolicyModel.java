package com.lic.epgs.gratuity.policyservices.merger.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
public class DebitCreditMergePolicyModel {

	private double debitMergerOutGo;
	private double debitRefundOfFirstPremium;
	private double debitRefundOfOtherFirstYeatPremium;
	private double debitRefundOfRenewalPremium;
	private double debitRefundOfGstLiability;
	private double creditCoPaymentContraAc;
	private double creditMergerContraForOut;
	private double debitMergerContraForIn;
	private double creditMegerIn;
	private double creditLifePremiumOyrgtaRp;
	private double creditOtherFirstYearOyrgtaLp;
	private double creditGstOnPremium;
	private double debitCoPaymentContraAc;
	private double creditOsPaymentsAtGeneralCpc;

//	private JournalVoucherDetailModel journalVoucherDetailModel;

}
