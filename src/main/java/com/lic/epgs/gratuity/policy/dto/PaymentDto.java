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
public class PaymentDto {
	private Long id;
	private Long masterQuotationId;
	private Long depositId;
	private Long quotationChargeId;
	private double balanceAmount;
	private Boolean isMinimumPaymentReceived;
	private Boolean isFullPaymentReceived;
	
	private String createdBy;
}
