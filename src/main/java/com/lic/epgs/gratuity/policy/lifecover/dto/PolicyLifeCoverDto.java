package com.lic.epgs.gratuity.policy.lifecover.dto;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author Ismail Khan A
 *
 */

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class PolicyLifeCoverDto {
	private Long id;
	private Long policyId;
	private Long lifeCoverTypeId;
	private Long categoryId;
	private Double sumAssured;
	private Integer retirementAge;
	private Double retentionLimit;
	private Long noOfMonths;
	private Double minimumSumAssured;	
	private Double maximumSumAssured;
	private Boolean isActive;
	private String createdBy;
	private Date createdDate;
	private String modifiedBy;
	private Date modifiedDate;
}
