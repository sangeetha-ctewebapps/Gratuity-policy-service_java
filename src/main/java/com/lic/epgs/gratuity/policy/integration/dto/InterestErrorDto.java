package com.lic.epgs.gratuity.policy.integration.dto;

/**
 * 
 */


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author Muruganandam
 *
 */

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class InterestErrorDto {
	private String policyNo;
	private String memberId;
	private String error;
	private String txnType;
	private String username;
	private String policyType;
}
