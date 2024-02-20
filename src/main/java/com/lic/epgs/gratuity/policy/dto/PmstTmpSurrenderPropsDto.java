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
public class PmstTmpSurrenderPropsDto {
	private Long id;
	private Integer surrenderRequestNumber;
	private Date surrenderRequestDate;
	private Date surrenderProcessingDate;
	private Long pmstTempPolicyID;
	private Integer surrenderType;
	private Integer surrenderAmount;
	private Integer noOfInstallments;
	private Integer statusID;
	private Integer isActive;
	private String createdBy;
	private Date createdDate;
	private String modifiedBy;
	private Date modifiedDate;
	
}
