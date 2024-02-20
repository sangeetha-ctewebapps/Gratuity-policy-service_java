package com.lic.epgs.gratuity.quotation.schemerule.dto;

import java.util.Date;

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
public class SchemeRuleDto {

	private Long id;
	private Long quotationId;
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
	
}
