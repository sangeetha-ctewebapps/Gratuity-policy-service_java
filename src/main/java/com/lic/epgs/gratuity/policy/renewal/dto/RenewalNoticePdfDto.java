package com.lic.epgs.gratuity.policy.renewal.dto;

import java.util.Date;

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
public class RenewalNoticePdfDto {

	private String unitCodeAddress1;
	private String unitCodeAddress2;
	private String unitCodeAddress3;
	private String unitCodeAddress4;
	private String unitCodeAddress5;
	private String masterPolicyNumber;
	private String mphaddress1;
	private String mphaddress2;
	private String mphaddress3;
	private String mphaddress4;
	private String mphaddress5;
	private String annualRenewalDate;
	
}
