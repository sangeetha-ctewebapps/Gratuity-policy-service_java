package com.lic.epgs.gratuity.policy.surrender.dto;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.lic.epgs.gratuity.accountingservice.dto.GlTransactionModelDto;
import com.lic.epgs.gratuity.accountingservice.dto.GstDetailModelDto;
import com.lic.epgs.gratuity.accountingservice.dto.JournalVoucherDetailModelDto;
import com.lic.epgs.gratuity.policyservices.conversion.dto.TrnRegisModel;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
public class SurrenderAccountingIntegrationRequestModel {

	private String productCode;
	private String policyNo;
	private String isLegacy;
	private GlTransactionModelDto glTransactionModel;
	private GstDetailModelDto gstModel;
	private JournalVoucherDetailModelDto JournalVoucherDetailModel;
	private String mphCode;

	private DebitCreditPolicySurrenderModel debitCreditPolicySurrenderModel;	
	private BeneficiaryPaymentDetailModel beneficiaryPaymentDetailModel;
	private TrnRegisModel trnRegisModel;
	private String refNo;
	private String unitCode;
	private String userCode;
	private String variantCode;
	private Boolean isGstApplicable;
}
