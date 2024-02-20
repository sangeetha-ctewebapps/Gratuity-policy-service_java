package com.lic.epgs.gratuity.policy.renewalpolicy.schemerule.dto;

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
public class RenewalSchemeruleTMPDto {

	private Long id;
	private Long policyId;
	private Long policyServiceId;
	private Long fclTypeId;
	private Long premiumAadjustmentTypeId;
	private int minimumGratuityServiceYear;
	private int minimumGratuityServiceMonth;
	private int minimumGratuityServiceDay;
	private Double fcl;
	private Boolean isActive;
	private String createdBy;
	private Date createdDate;
	private Date modifiedDate;
	private String modifiedBy;
	private Long tmpPolicyId;
	private Long pmstSchemeRuleId;

}
