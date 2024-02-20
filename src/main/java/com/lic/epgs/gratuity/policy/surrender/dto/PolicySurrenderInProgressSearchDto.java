package com.lic.epgs.gratuity.policy.surrender.dto;


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
public class PolicySurrenderInProgressSearchDto {

	private String policyNumber;
	private String mphName;
	private String mphPAN;
	private String unitCode;
	private String screenName;
	private String roleType;
	private String sortBy;
	private String sortOrder;
	private Long surrenderNumber;
	private String surrenderType;
}
