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
public class RegularGstDetailModel {

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
 	private Character transactionType;
 	private Character transactionSubType;
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
 	  	 
// 	private ReversalGstDetailModel reveraGstDetailModel;
}
