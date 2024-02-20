package com.lic.epgs.gratuity.policy.dto;

import java.util.Date;

import javax.persistence.Column;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class PmstTmpMergerPropsDto {
	private Long id;
	private Long policyNumber;
	private String mergerRequestNumber;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy", timezone = "asia/kolkata")
	private Date mergerRequestDate;
	private Integer mergerType;
	private Long sourceTmpPolicyID;
	private Long destinationTmpPolicyID;
	private Double sourceAccruedInterest;
	private Double sourcePriorTotalFund;
	private Double destinationPriorFundValue;
	private Double destinationPriorTotalValue;
	private Long statusID;
	private Long deductFrom;
	private Boolean isActive;
	private String createdBy;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy", timezone = "asia/kolkata")
	private Date createdDate;
	private String modifiedBy;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy", timezone = "asia/kolkata")
	private Date modifiedDate;
	private Long sourcemasterPolicyId;
	private Long destiMasterPolicyId;
	private String serviceType;
	private Double sourcePriorFundValue;
	private Double premiumRefundAmount;	
	private Double gstRefundAmount;
	private String isPremiumRefundRequired;
	
	
}
