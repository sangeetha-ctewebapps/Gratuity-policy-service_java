package com.lic.epgs.gratuity.accountingservice.dto;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class AcctDepositResponse {

	private Long id;
	private Long acctResponseId;
	private Long adjustmentNumber;
	private String voucherNumber;
	private Double voucherAmount;
	private Date voucherEffectiveDate;
	private String gstInvoiceNumber;
}
