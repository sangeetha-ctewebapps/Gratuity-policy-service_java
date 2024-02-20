package com.lic.epgs.gratuity.accountingservice.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class AcctResponseDto {
	
	private Long id;
	private String module; //NB/CLAIM/RENEWAL
	private Long referenceId; //CONTRIBUTION_ID/CLAIM_PROPS_ID
	private String referenceValue;
	
}
