package com.lic.epgs.gratuity.policy.renewalpolicy.gratuitybenefit.dto;

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
public class RenewalGratuityBenefitPropsTMPDto {
	private Long id;
	private Integer numberOfYearsOfService;
	private Integer numberOfDaysWage;
	private Integer numberOfWorkingDaysPerMonth;
	private Double gratuityCellingAmount;
	private Double monthlyCelling;
	private Double salaryCelling;
	private Integer retirementAge;
	private Boolean isActive;
	private String createdBy;
	private Date createdDate;
	private String modifiedBy;
	private Date modifiedDate;
	private Long tmpPolicyId;
	private Long pmstGratuityBenefitPropsId;
	private Long recordType;
}
