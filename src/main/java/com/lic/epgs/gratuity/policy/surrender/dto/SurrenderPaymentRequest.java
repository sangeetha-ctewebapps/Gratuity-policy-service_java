package com.lic.epgs.gratuity.policy.surrender.dto;

import java.math.BigDecimal;

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
public class SurrenderPaymentRequest {

	private String variant;
	private BigDecimal policyFundValue;
	private Long surrenderId;
	private String policyNumber;
}
