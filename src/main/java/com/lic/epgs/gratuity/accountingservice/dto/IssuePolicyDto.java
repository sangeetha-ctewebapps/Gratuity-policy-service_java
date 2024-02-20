package com.lic.epgs.gratuity.accountingservice.dto;

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
public class IssuePolicyDto {

	private String proposalNo;
	private String policyNo;
	private String productCode;
	private String variantCode;
	private String van;
	private String bankUniqueId;
	private String responseMessage;

}
