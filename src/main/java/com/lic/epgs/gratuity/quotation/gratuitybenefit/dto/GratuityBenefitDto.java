package com.lic.epgs.gratuity.quotation.gratuitybenefit.dto;

import java.util.Date;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author Vigneshwaran
 *
 */

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class GratuityBenefitDto {
	private Long id;
	private Long quotationId;
	private Long categoryId;
	private Long gratutiyBenefitTypeId;
	private Long gratutiySubBenefitId;
	private Boolean isActive;
	private String createdBy;
	private Date createdDate;
	private String modifiedBy;
	private Date modifiedDate;
	private Long gratuityNoSlabBenefitTypeId;
	
	private List<GratuityBenefitPropsDto> GratuityBenefitProps;	
}
