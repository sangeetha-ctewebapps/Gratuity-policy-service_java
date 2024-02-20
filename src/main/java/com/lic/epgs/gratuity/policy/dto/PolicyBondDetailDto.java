package com.lic.epgs.gratuity.policy.dto;




import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class PolicyBondDetailDto {

	private String policyNumber;
	private String proposalNumber;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy", timezone = "asia/kolkata")
	private String annualRenewalDate;
	
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy", timezone = "asia/kolkata")
	private String valuationEffectiveDate;
	
	private Long totalMember;
	
	private String unitCode;
	private String proposalDate;
	private Long intermediaryCode;
	private String intermediaryName;
	private Long intermediaryContactNo;
	private String productCode;

	private String paymentReceived;
	private Long totalSumAssured;
	private String retirementAge;
	private String mphName;
	private String adjustmentAmount;
	private String totalGratuity;
	private String totalPremium;
	private String totalSumAssuredAmount;
	private String mphEmail;
	private String mphAddress1;
	private String mphAddress2;
	private String mphAddress3;
	private String mphAddressType;
	private Long mphMobileNo;
	private String unitName;
	private String address1;
	private String address2;
	private String address3;
	private String state;
	private String district;
	private String policyIssuanceDate;
	
}
