package com.lic.epgs.gratuity.policyservices.contributionadjustment.dto;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.lic.epgs.gratuity.policy.claim.dto.ClaimPayoutItemDto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class ContributionAdjustmentPropsDto {
	
	private Long id;
	

	private String contriAdjNumber;
	

	private Long pstgContriDetailId;
	
	

	private Long pmstContriDetailId;
	
	private Double amtToBeAdjusted;

	private Double firstPremiumPS;
	

	private Double singlePremiumFirstYearCS;
	
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy", timezone = "Asia/Kolkata")
	private Date adjustmentForDate;
	

	private Long contriAdjStatus;
	

	private String rejectedReason;

	private String rejectedRemarks;

	private Long tmpPolicyId;
	



	
	private Boolean isActive;
	

	private String createdBy;
	
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy", timezone = "Asia/Kolkata")
	private Date createdDate;

	private String modifiedBy;
	
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy", timezone = "Asia/Kolkata")
	private Date modifiedDate;
	
	private String serviceType;
	private String policyNumber;
	private String mphName;
	private String mphCode;
	private String customerName;
	private String customerCode;
	private String intermediaryName;
	private String lineOfBusiness;
	private String productName;
	private String productVariant;
	private String unitOffice;



}
