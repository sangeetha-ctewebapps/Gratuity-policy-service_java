package com.lic.epgs.gratuity.accountingservice.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
public class RenewalContributionAdjustRequestModelDto {

	private int adjustmentNo;
	private double adjustmentAmount;
	private String refNo;
	private String dueMonth;
	private String mphCode;
	private Boolean isGstApplicable;
	private String unitCode;
	private String userCode;
	
	private JournalVoucherDetailModelDto journalVoucherDetailModel;
	private GlTransactionModelDto glTransactionModel;
	private GstDetailModelDto gstDetailModel;
	private List<RenewalContrinAdjustDebitCreditRequestModelDto> renewalDebitCreditRequestModel;

}
