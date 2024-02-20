package com.lic.epgs.gratuity.accountingservice.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class AcctCrDrResDto {

	private Long id;
	private Long acctResponseId;
	private String debitCode;
	private Double debitAmount;
	private String debitCodeDescription;
	private String creditCode;
	private Double creditAmount;
	private String creditCodeDescription;
}
