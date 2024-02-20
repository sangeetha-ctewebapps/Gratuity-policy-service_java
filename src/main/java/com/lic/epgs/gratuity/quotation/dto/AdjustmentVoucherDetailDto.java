package com.lic.epgs.gratuity.quotation.dto;

import java.math.BigDecimal;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class AdjustmentVoucherDetailDto {
	
	private String headOfAccount;

	private String code;

	private Double debit;

	private Double credit;

	private BigDecimal debitBigdecimal;

	private BigDecimal creditBigDecimal;

	private String debitAmount;

	private String creditAmount;	

}
