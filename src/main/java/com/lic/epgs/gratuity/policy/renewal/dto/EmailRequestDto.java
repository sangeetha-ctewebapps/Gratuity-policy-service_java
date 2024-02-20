package com.lic.epgs.gratuity.policy.renewal.dto;

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
public class EmailRequestDto {
	
	private Long quotationid;
	private long policyId;
	private String emailId;
	
	

}
