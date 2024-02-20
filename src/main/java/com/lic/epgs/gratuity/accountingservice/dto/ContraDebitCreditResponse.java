package com.lic.epgs.gratuity.accountingservice.dto;

import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class ContraDebitCreditResponse {

	private Set<AcctCrDrResDto> contraCrDrEntries;
}
