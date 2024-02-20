package com.lic.epgs.gratuity.policyservices.premiumcollection.dto;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class PremiumCollectionSearchDto {
	
	
	private Long[] premAdjStatus;

	private Long tmpPolicyId;
	private Boolean isActive;
	private String serviceType;
	private String policyNumber;

	private String customerName;
	private String intermediaryName;
	private String lineOfBusiness;
	private String productName;
	private String productVariant;
	private String unitOffice;
	private String mphCode;
	private String mphName;
	private String unitCode;
	private String product;
	private String pan;
	private String userType;
	private String customerCode;

}
