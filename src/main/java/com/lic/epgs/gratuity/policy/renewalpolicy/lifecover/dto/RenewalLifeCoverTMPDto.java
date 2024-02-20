package com.lic.epgs.gratuity.policy.renewalpolicy.lifecover.dto;

import java.util.Date;

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
public class RenewalLifeCoverTMPDto {

	
	private Long id;
	private Long policyId;
	private Long noofMonth;
	private Long categoryId;
	private Double sumAssured;
	private Integer retirementAge;
	private Double retentionLimit;
	private Double minimumSumAssured;	
	private Double maximumSumAssured;
	private Long lifeCoverTypeId;
	private Boolean isActive;
	private String createdBy;
	private Date createdDate;
	private String modifiedBy;
	private Date modifiedDate;
	private Long tmpPolicyId;
	private Long pmstLifeCoverId;

}
