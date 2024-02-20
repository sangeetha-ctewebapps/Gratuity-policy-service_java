package com.lic.epgs.gratuity.policy.renewalpolicy.dto;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

@Data
public class PolicyTmpSearchDto {

	private Long tmpPolicyId;

	private String policyNumber;

	private String customerName;

	private String intermediaryName;

	private String lineOfBusiness;

	private String productName;

	private String productVariant;

	private Long productId;

	private Long productVariantId;

	private String unitId;

	private String unitCode;

	private String unitOffice;

	private Long policyStatusId;

	private Long policySubStatusId;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy", timezone = "asia/kolkata")
	private Date policyStartDate;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy", timezone = "asia/kolkata")
	private Date policyEndDate;

	private Long policyServiceid;
	private Boolean isActive;

	private Long masterPolicyId;

	private Long quotationStatusId;

	private Long quotationSubStatusId;

	private Long quotationTaggedStatusId;
	
	private Long serviceStatusId;
	
	private Integer fclUptoAge;
	
	private Boolean midLeaverAllowed;
	
	private Boolean midJoinerAllowed;
	
	

}
