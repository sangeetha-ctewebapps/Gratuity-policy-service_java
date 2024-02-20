package com.lic.epgs.gratuity.quotation.dto;

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
public class SupplementaryAdjustmentDto {

	private String  detailsNo;
	private String  detailsDate;
	private BigDecimal  detailsAmount;
	private BigDecimal  detailsAdjusted;
	private BigDecimal  detailsBalance;
}
