package com.lic.epgs.gratuity.policy.dto;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class PmstTempMemTrfPropsDto {
	private Long id;
	private Long trfRequestNumber;
	private Date trfRequestDate;
	private Date trfProcessingDate;
	private Long transferType;
	private Long totalFundValue;
	private Long sourceTmpPolicyID;
	private Long destinationTmpPolicyID;
	private Long StatusID;
	private Long isActive;
	private String createdBy;
	private Date createdDate;
	private String modifiedBy;
	private Date modifiedDate;
	


}
