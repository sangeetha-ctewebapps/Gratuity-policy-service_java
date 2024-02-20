package com.lic.epgs.gratuity.policy.renewalpolicy.valuationmatrix.dto;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class RenewalValuationMatrixTMPDto {

	private Long id;
	private Long policyId;
	private Date valuationDate;
	private Long valuationTypeId;
	private Long currentserviceLiability;
	private Long pastserviceLiability;
	private Long premium;
	private Long totalSumAssured;
	private Long gst;
	private Long totalPremium;
	private Long amountReceived;
	private Long amountPayable;
	private Long balanceToBePaid;
	private Boolean isActive;
	private String createdBy;
	private Date createdDate;
	private String modifiedBy;
	private Date modifiedDate;
	private Long tmpPolicyId;
	private Long pmst_ValuationMatrixId;

	
}
