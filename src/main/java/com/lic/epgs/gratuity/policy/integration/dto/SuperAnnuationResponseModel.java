package com.lic.epgs.gratuity.policy.integration.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author Logesh.D Date:16-09-2022
 *
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class SuperAnnuationResponseModel {
	
	// Portfolio Data
	
	private int iCodeForLob;

	private int iCodeForProductLine;

	private String iCodeForVariant;

	private int iCodeForBusinessType;

	private int iCodeForParticipatingType;

	private int iCodeForBusinessSegment;

	private int iCodeForInvestmentPortfolio;
	
	 // Servicing Unit Detail
	
	private String servicingUnitName;

	private String servicingUnitAddress1;

	private String servicingUnitAddress2;

	private String servicingUnitAddress3;

	private String servicingUnitAddress4;

	private String servicingUnitCity;

	private String servicingUnitPincode;

	private String servicingUnitEmail;

	private String servicingUnitPhoneNo;

	private String operatingUnitType;

	private String unitCode;
	
	  // MPH Data
	
	private String schemeName;

	private String mphCode;

	private String mphName;

	private String mphNo;

	private Long mphMobileNo;

	private String mphEmail;

	private String mphAddress1;

	private String mphAddress2;

	private String district;

	private Long state;

	private String stateName;
	private Long pinCode;

}
