package com.lic.epgs.gratuity.policyservices.merger.dto;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
public class MergePolicyRequestModel {

	private String sourcePolicyNo;
 	private String targetPolicyNo;
 	private String sourcePolicyUnitCode;
 	private String targetPoicyUnitCode;
 	private String sourceVersion;
 	private String targetVersion;
 	private String mphCode;
 	private String refNo;
 	private String isPayoutApplicable;
 	private String isGSTRefundOnPremiumApplicable;
 	
 	private BeneficiaryPaymentDetailModel beneficiaryPaymentDetailModel;
 	private DebitCreditMergePolicyModel debitCreditMergePolicyModel;
 	private GlTransactionModel glTransactionModel;
 	private JournalVoucherDetailModel journalVoucherDetailModel;
 	private RegularGstDetailModel regularGstDetailModel;
 	private ReversalGstDetailModel reveraGstDetailModel;
 	private TrnRegisModel trnRegisModel;
// 	private DebitCreditMergePolicyModel debitCreditMergePolicyModel;
}
