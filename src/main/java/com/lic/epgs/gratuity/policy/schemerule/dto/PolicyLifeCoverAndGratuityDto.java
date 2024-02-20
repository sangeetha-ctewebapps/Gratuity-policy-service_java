package com.lic.epgs.gratuity.policy.schemerule.dto;

import java.util.Date;
import java.util.List;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.lic.epgs.gratuity.policy.gratuitybenefit.dto.PolicyGratuityBenefitPropsDto;
import com.lic.epgs.gratuity.quotation.gratuitybenefit.dto.GratuityBenefitPropsDto;
import com.lic.epgs.gratuity.quotation.gratuitybenefit.entity.GratuityBenefitPropsEntity;
import com.lic.epgs.gratuity.quotation.gratuitybenefit.entity.MasterGratuityBenefitEntity;
import com.lic.epgs.gratuity.quotation.gratuitybenefit.entity.MasterGratuityBenefitPropsEntity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class PolicyLifeCoverAndGratuityDto {

	private Long id;
	private Long policyId;
	private Long gratuityBenefitId;
	private Long lifeCoverTypeId;
	private Long categoryId;
	private String categoryName;
	private Double sumAssured;
	private int retirementAge;
	private Double retentionLimit;
	private Double minimumSumAssured;
	private Double maximumSumAssured;
	private Long noOfMonths;
	private Long gratutiySubBenefitId;
	private Long gratutiyBenefitTypeId;
	
	private Boolean isActive;
	private String createdBy;
	private Date createdDate;
	private String modifiedBy;
	private Date modifiedDate;
	private List<PolicyGratuityBenefitPropsDto> policyGratuityBenefitProps;	
}
