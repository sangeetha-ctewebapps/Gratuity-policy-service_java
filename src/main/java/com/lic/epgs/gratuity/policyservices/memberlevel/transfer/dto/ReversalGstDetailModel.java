package com.lic.epgs.gratuity.policyservices.memberlevel.transfer.dto;

import java.util.Date;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReversalGstDetailModel {

	private Date transactionDate;
	private String gstRefNo;
	private String gstRefTransactionNo;
	private String gstTransactionType;
	private Double amountWithTax;
	private Double amountWithoutTax;
	private Double cessAmount;
	private Double totalGstAmount;
	private Double gstRate;
	private Double cgstAmount;
	private Double cgstRate;
	private Double igstAmount;
	private Double igstRate;
	private Double sgstAmount;
	private Double sgstRate;
	private Double utgstAmount;
	private Double utgstRate;
	private String gstApplicableType;
	private String gstType;
	private String fromStateCode;
	private String toStateCode;
	private String userCode;
	private String transactionType;
	private String transactionSubType;
	private String gstInvoiceNo;
	private String entryType;
	private String fromGstn;
	private String fromPan;
	private String hsnCode;
	private String mphAddress;
	private String mphName;
	private String natureOfTransaction;
	private Date oldInvoiceDate;
	private String oldInvoiceNo;
	private String productCode;
	private String remarks;
	private String toGstIn;
	private String toPan;
	private String variantCode;
	private String year;
	private String month;
	
	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
	}
}
