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
public class PmstTmpPolTrfPropsDto {
	private Long id;
	private Integer polTrfRequestNumber;
	private Date polTrfrequestDate;
	private Date poltrfProcessingDate;
	private Long pmstTmpPolicyID;
	private String destinationUnitCode;
	private Integer totalFundValue;
	private Integer statusID;
	private Integer isActive;
	private String createdBy;
	private Date createdDate;
	private String modifiedBy;
	private Date modifiedDate;
	

}
