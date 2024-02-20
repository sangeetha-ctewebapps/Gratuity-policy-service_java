package com.lic.epgs.gratuity.policy.claim.dto;

import java.util.Date;
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
public class PdfClaimForwardLetter {

	private String unitName;
	private String address1;
	private String address2;
	private String address3;
	private String city_name;
	private String state_name;
	private String pincode;
	private String gstIn;

}
