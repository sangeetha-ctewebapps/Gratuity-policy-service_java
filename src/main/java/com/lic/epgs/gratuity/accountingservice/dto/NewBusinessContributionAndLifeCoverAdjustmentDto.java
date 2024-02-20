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
public class NewBusinessContributionAndLifeCoverAdjustmentDto {

	private Double adjustmentAmount;
	private int adjustmentNo;
	private String dueMonth;
	private GlTransactionModelDto glTransactionModel;
	private GstDetailModelDto gstDetailModel;
	private Boolean isGstApplicable;
	private JournalVoucherDetailModelDto JournalVoucherDetailModel;
	private String mphCode;

	private List<NewBusinessContriDebitCreditRequestModelDto> newBusinessContriDebitCreditRequestModel;	
	private List<RenwalWithGIDebitCreditRequestModelDto> renewalWithGiDebitCreditRequestModel;
	private String refNo;
	private String unitCode;
	private String userCode;
	
}
