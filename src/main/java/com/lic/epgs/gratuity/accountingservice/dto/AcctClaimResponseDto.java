package com.lic.epgs.gratuity.accountingservice.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class AcctClaimResponseDto {

	private Long id;
	private Long acctResponseId;
	private String debitCode;
	private String creditCode;
	private Double payoutAmount;
	private String journalNumber;
	
	private List<AcctCrDrResDto> acctCrDrResDtos;
	
}
