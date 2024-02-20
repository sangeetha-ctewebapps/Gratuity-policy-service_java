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
public class PolicyStmtDto {
	private String policyId;
	private String policyNumber;
	private String remarks;
	private Boolean isOSDeposit;
	private Boolean isOsClaim;
	private Boolean isFundGenerated;
}
