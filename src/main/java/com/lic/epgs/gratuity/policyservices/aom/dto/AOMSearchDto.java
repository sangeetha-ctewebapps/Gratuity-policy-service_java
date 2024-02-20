package com.lic.epgs.gratuity.policyservices.aom.dto;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;

@Data
public class AOMSearchDto {

	@JsonIgnore
	private Long id;
	private Long tmpPolicyId;
	private String policyNumber;
	private Long policyStatusId;
	private String customerName;
	private String customerCode;
	private String intermediaryName;
	private String lineOfBusiness;
	private String productName;
	private Long masterPolicyId;
	private String productVariant;
	private Long quotationStatusId;
	private String unitOffice;
	private Long policyTaggedStatusId;
	private Long policyServiceId;
	private String pan;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy", timezone = "asia/kolkata")
	private Date policyStartDate;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy", timezone = "asia/kolkata")
	private Date policyEndDate;
	private Boolean isActive;
	private Long serviceStatusId;
}