package com.lic.epgs.gratuity.policy.dto;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.lic.epgs.gratuity.quotation.dto.AdjustmentVoucherDetailDto;
import com.lic.epgs.gratuity.quotation.dto.SupplementaryAdjustmentDto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class PolicyAdjustmentPDFDto {

	private String pngsDept;
	private String divisionalName;

	private String voucherNumber;
	private String voucherDate;

	private String favouringName;

	private String amountInWords;

	private String policyNumber;
	private String schemeName;

	private String total;

	private  BigDecimal balanceDeposit=BigDecimal.ZERO;

	private String ard;
	private String mode;
	private String duedate;

	private String drawn;
	private String chequeNumber;
	private String date;
	private String payDate;
	private String paidDate;

	private String preparedBy;
	private String checkedBy;

	private List<AdjustmentVoucherDetailDto> voucherDetail = new ArrayList<AdjustmentVoucherDetailDto>();
	private List<SupplementaryAdjustmentDto> supplymentary = new ArrayList<SupplementaryAdjustmentDto>();

}
