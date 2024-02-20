package com.lic.epgs.gratuity.policy.renewalpolicy.gratuitybenefit.dto;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.lic.epgs.gratuity.policy.gratuitybenefit.dto.PolicyGratuityBenefitPropsDto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class RenewalGratuityBenefitTMPDto {
	private Long id;
	private Long policyId;
	private Long categoryId;
	private Long gratutiyBenefitTypeId;
	private Long gratutiySubBenefitId;
	private Boolean isActive;
	private String createdBy;
	private Date createdDate;
	private String modifiedBy;
	private Date modifiedDate;
	private Long tmpPolicyId;
	private Long pmstGratutiyBenefitId;
	private Long gratuityNoSlabBenefitTypeId;

	private List<RenewalGratuityBenefitPropsTMPDto> renewalGraBenePropTmpDto;

}
