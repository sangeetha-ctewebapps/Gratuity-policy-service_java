package com.lic.epgs.gratuity.policy.claim.dto;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class PayoutApproveRequestDto {
	
	
	
	
	private String accountRuleContext;
	private String refNo;
	private Date   effectiveDateOfPayment;
	private String payoutSourceModule;
	private String beneficiaryPaymentId;
	private String productCode;
	private String variantCode;
	private Double totalAmount;
	private String operatingUnit;
	private String operatingUnitType;
	private String paymentMode;
	private String policyNo;
	private String lob;
	private String product;
	private String mphCode;
	private String productVariant;
	private Long   iCodeForLob;
	private Long   iCodeForProductLine;
	private String iCodeForVariant;
	private Long   iCodeForBusinessType;
	private Long   iCodeForParticipatingType;
	private Long   iCodeForBusinessSegment;
	private Long   iCodeForInvestimentPortFolio;
	private String beneficiaryName;
	private String beneficiaryBankName;
	private String beneficiaryBranchIfsc;
	private String beneficiaryBranchName;
	private String beneficiaryAccountType;
	private String beneficiaryAccountNumber;
	private String beneficiaryLie;
	private String senderLei;
	private String unitCode;
	private String memberNumber;
	private String paymentCategory;
	private String paymentSubCategory;
	private String nroAccount;
	private String iban;
	private String remarks;
	private Double paymentAmount;
	private Double gstliabiltyAmount;
	private String isGSTapplicable;
	private String transactionType;
	private String transactionSubType;
	private String gstRefNo;
	private String gstRefTransactionNo;
	private String gstTrasactionType;
	private Long   amountWithTax;
	private Long   amountWithOutTax;
	private Long   cessAmount;
	private Double totalGstAmount;
	private Long   gstRate;
	private Double cgstAmount;
	private Long   cgstRate;
	private Double igstAmount;
	private Long   igstRate;
	private Double sgstAmount;
	private Long   sgstRate;
	private Double utgstAmount;
	private Long   utgstRate;
	private String gstApplicableType;
	private String gstType;
	private Long   collectionId;
	private String toGstin;
	private String fromgstn;
	private String hsnCode;
	private String fromPan;
	private String toPan;
	private String natureOfTransaction;
	private String mphName;
	private String mphAddress;
	private String entryType;
	private String gstRemarks;
	private Date   oldInvoiceDate;
	private String fromStateCode;
	private String toStateCode;
	private String createdBy;
	private Long   drSrAccN;
	private Long   crSrAccN;
	private String isMultipleBeneficiary;
	
	private String OUT_JournalNumber;
	private String OUT_DebitAccount;
	private String OUT_CreditAccount;
	private String OUT_TotalAmount;
	private String OUT_CreditICode;
	private String OUT_DebitICode;
	private String OUT_Message;
	private String OUT_Status;
	private String OUT_StatusCode;
	private String P_SQLCODE;
	private String P_SQLERROR_MESSAGE;
		
}
	
	
	