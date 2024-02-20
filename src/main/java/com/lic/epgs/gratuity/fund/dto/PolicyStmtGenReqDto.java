package com.lic.epgs.gratuity.fund.dto;

import java.util.List;

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
public class PolicyStmtGenReqDto {
	private List<String> policyId;
	private String trnxDate;
	private String variant;
	private String policyType;
	private String memberStatus;
	private String recalculate;
	private String unitId;
}
