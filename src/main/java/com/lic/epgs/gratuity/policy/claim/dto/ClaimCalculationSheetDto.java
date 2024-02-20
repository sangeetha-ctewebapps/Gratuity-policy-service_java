package com.lic.epgs.gratuity.policy.claim.dto;

import java.util.Date;
import java.util.List;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.lic.epgs.gratuity.policy.renewalpolicy.member.dto.RenewalPolicyTMPMemberNomineeDto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class ClaimCalculationSheetDto {

	private String mphName;

	private String intimationNumber;

	
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy", timezone = "asia/kolkata")
	private Date intimationDate;

	private String policyNo;

	
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy", timezone = "asia/kolkata")
	private Date annualRenewalDate;

	
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy", timezone = "asia/kolkata")
	private Date dateOfBirth;

	
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy", timezone = "asia/kolkata")
	private Date dateOfexit;

	
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy", timezone = "asia/kolkata")
	private Date dateOfDeath;
	
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy", timezone = "asia/kolkata")
	private Date dateOfAppointment;

	private String modeOfExit;

	private String caseOfDeath;

	private String reasonForDeath;

	private String placeOfDeath;

	private String employeeCode;

	private String licId;

	private String memberName;

	private Double salaryAsOnDateOFExit;

	private Double salaryAsOnArd;
	
	private Double lcsaPayable;
	
	private Double refundPremiumAmt;
	private Long percentage;
	private Double modifiedGratuityAmount;
	private List<RenewalPolicyTMPMemberNomineeDto> getNomineeDetail;
//	private Long percentageSingle;  
	
	private String productCode;
	
	private String categoryName;

	private Double refundGstAmount;
	
	

}
