package com.lic.epgs.gratuity.fund.dto;

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
public class PolicyFundStmtRequestDto {
	private String unitId;
	private String variant;
	private String financialYear;
	private String quarter;
	private String frequency;
}
