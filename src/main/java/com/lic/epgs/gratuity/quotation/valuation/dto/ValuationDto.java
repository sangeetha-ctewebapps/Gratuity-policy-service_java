package com.lic.epgs.gratuity.quotation.valuation.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author Ismail Khan
 *
 */

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)

public class ValuationDto {
	private Long id;
	private Long quotationId;

	private Float salaryEscalation;

	private Float discountRate;
	
	private String createdBy;
	private String modifiedBy;
	
}
