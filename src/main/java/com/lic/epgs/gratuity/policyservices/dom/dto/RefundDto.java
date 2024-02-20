package com.lic.epgs.gratuity.policyservices.dom.dto;

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
public class RefundDto {
	private Double premiumCollected;
	private Double refundablePremium;
	private Double refundableGst;
}
