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
public class BeneficiaryPaymentDetailModel {

	private String payoutSourceModule;
 	private String paymentSourceRefNumber;
 	private String beneficiaryName;
 	private String beneficiaryBankName;
 	private String beneficiaryBankIfsc;
 	private String beneficiaryBranchName;
 	private String beneficiaryAccountType;
 	private String beneficiaryAccountNumber;
 	private Double paymentAmount;
 	private Date effectiveDateOfPayment;
 	private Character paymentMode;
 	private String beneficiaryLei;
 	private String senderLei;
 	private String unitCode;
 	private String policyNumber;
 	private String memberNumber;
 	private String paymentCategory;
 	private String paymentSubCategory;
 	private String nroAccount;
 	private String iban;
 	private String remarks;
 	private String voucherNo;
 	private String beneficiaryPaymentId;
 	private String productCode;
 	private String variantCode;
 	private String operatinUnitType;
 	 
// 	private GlTransactionModel glTransactionModel;
}
