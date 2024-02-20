package com.lic.epgs.gratuity.accountingservice.dto;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
@Getter
@Setter
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
public class GstDetailModelDto {


	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Kolkata")
	private Date createdDate;
	
	private String effectiveEndDate;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Kolkata")
	private Date effectiveStartDate;
	
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Kolkata")
	private Date modifiedDate;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", timezone = "Asia/Kolkata")
	private Date oldInvoiceDate;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Kolkata")
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
  
    private String createdBy;
   
    private Long modifiedBy;
  
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
   
    private String oldInvoiceNo;
    private String productCode;
    private String remarks;
    private String toGstIn;
    private String toPan;
    private String variantCode;
    private String year;
    private String month;
    
    

}
