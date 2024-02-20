package com.lic.epgs.gratuity.policyservices.contributionadjustment.dto;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
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
public class ContributionAdjustmentSearchDto {
	private Long id;

	private String contriAdjNumber;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy", timezone = "Asia/Kolkata")
	private Date adjustmentForDate;

	private Long[] contriAdjStatus;

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

	private String customerName;
	private String intermediaryName;
	private String lineOfBusiness;
	private String productName;
	private String productVariant;
	private String unitOffice;
	private String mphCode;
	private String customerCode;
	private String mphName;
	private String unitCode;
	private String product;
	private String pan;
}
