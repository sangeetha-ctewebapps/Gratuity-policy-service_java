package com.lic.epgs.gratuity.quotation.schemerule.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author Gopi
 *
 */

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class NewSchemeRuleDto {
	private Long quotationId;
	private Long fclTypeId;
	private Long premiumAadjustmentTypeId;
	private int minimumGratuityServiceYear;
	private int minimumGratuityServiceMonth;
	private int minimumGratuityServiceDay;
	private Double fcl;
	private String createdBy;
	private String modifiedBy;
}
