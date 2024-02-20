package com.lic.epgs.gratuity.accountingservice.dto;

import com.lic.epgs.gratuity.policyservices.conversion.dto.GstDetailModel;
import com.lic.epgs.gratuity.policyservices.memberlevel.transfer.dto.BeneficiaryPaymentDetailModel;
import com.lic.epgs.gratuity.policyservices.merger.dto.JournalVoucherDetailModel;

import lombok.Data;

@Data
public class MidLeaverContributuionPremuimAndGstDto {

	private String adjustmentNo;
	private String refNo;
	private String mphCode;
	private String policyNo;
	private String unitCode;
	private String userCode;
	private String productCode;
	private String variantCode;
	private String isLegacy;

	private BeneficiaryPaymentDetailModel beneficiaryPaymentDetailModel;

	private DebitCreditMemberSurrenderModel debitCreditMemberSurrenderModel;

	private GlTransactionModelDto glTransactionModel;

	private GstDetailModel gstDetailModel;

	private JournalVoucherDetailModel journalVoucherDetailModel;

}
