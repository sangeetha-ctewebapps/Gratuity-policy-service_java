package com.lic.epgs.gratuity.policy.claim.dto;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
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
public class TempPolicyClaimPropsDtoTest {

	private Long onbordNumber;
	
	private String policyNumber;
	
	private String mphCode;
	
	private String licId;
	
	private String employeeCode;
	
	private String claimType;
	
	private String modeOfExitname;
	
	private String claimStatusName;
	

}
