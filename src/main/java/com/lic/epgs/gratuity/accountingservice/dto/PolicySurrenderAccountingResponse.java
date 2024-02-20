package com.lic.epgs.gratuity.accountingservice.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
public class PolicySurrenderAccountingResponse {

    private Long adjustmentNo;
    private String policyNumber;
    private String voucherNo;
    private String voucherEffectiveDate;
    private Double voucherAmount;
    
    private String contraVoucherNo;
    private String contraVoucherEffectiveDate;
    private Double contraVoucherAmount;
	
    private String gstRefNo;
    private String gstInvoiceNo;
    private String responseStatus;
    private String responseMessage;
    private Long beneficiaryPaymentId;
    
	private ContraDebitCreditResponse contraDebitCreditResponseModels;
	private DebitCreditResponse debitCreditResponseModels;
	
}
