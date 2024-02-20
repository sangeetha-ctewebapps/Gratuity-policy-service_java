package com.lic.epgs.gratuity.quotation.premium.dto;

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
public class PremiumDto {
	private Long id;
	private Long quotationId;
	private Long rateTableId;
	private String modifiedRateType;
	private Long modifiedRateTypeId;
	
	private String createdBy;
	private String modifiedBy;
}
