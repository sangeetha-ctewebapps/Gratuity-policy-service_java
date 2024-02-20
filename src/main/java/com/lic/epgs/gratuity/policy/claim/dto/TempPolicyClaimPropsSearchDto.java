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
public class TempPolicyClaimPropsSearchDto {
	
	private Long tmpPolicyId;
	private Long tmpMemberId;
	private String onboardNumber;
	private String payoutNo;
	private String initimationNumber;
	private Long[] claimStatusId;
	private String claimType;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy", timezone = "asia/kolkata")
	private Date dateOfExit;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy", timezone = "asia/kolkata")
	private Date dateOfDeath;
	private Long modeOfExit;
	private String licId;
	private String employeeCode;
	private String panNumber;
	 private String aadharNumber;
	 private String mphName;
	 private String mphPan;
	 private String policyNumber;
	 private String employeeName;
}
