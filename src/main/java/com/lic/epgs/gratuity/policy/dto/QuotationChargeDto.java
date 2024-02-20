package com.lic.epgs.gratuity.policy.dto;

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
public class QuotationChargeDto {
	private Long id;
	private Long masterQuotationId;
	private Long chargeTypeId;
	private double amountCharged;
	private double balanceAmount;
}
