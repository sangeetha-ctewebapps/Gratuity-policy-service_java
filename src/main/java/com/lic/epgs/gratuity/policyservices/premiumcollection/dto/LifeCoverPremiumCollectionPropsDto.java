package com.lic.epgs.gratuity.policyservices.premiumcollection.dto;

import java.util.Date;

import javax.persistence.Column;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class LifeCoverPremiumCollectionPropsDto{
	
private Long id;
	


private String lcPremCollNumber;



private Long pstgContriDetailId;


private Long pmstContriDetailId;


private Double amtDueToBeAdjusted;


private Double lcPremiumAmount;


private Double gstOnLCPremium;

private Double lateFee;




private Double gstOnLateFee;

@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy", timezone = "Asia/Kolkata")
private Date adjustmentForDate;

private Long premAdjStatus;


private String REJECTED_REASON;


private String rejectedRemarks;


private Long tmpPolicyId;
	private Boolean isActive;
	
	
	private String createdBy;
	
	
	private Date createdDate;
	
	
	private String modifiedBy;
	
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy", timezone = "Asia/Kolkata")
	private Date modifiedDate;
	private String rejectedReason;
	
	
	private String serviceType;
	private String policyNumber;
	private String mphName;
	private String mphCode;
	private String customerName;
	private String intermediaryName;
	private String lineOfBusiness;
	private String productName;
	private String productVariant;
	private String unitOffice;
	private Boolean waiveLateFee;
	private String customerCode;
	private Boolean isEscalatedToZo;
}