package com.lic.epgs.gratuity.policy.dto;

import java.util.Date;
import java.util.List;

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
public class PremiumReceiptDto {

	private String policyId;
	private String policyNumber;
	private String unitCode;
	private String proposalNumber;
	private Date createdDate;
	private Date annualRenewalDate;
	private Date premiumAdjustment;
	private Date nextArd;
	private String premiumMode;
	private String challanNo;
	private String contributionId;
	private String unitAddress;
	private String mphname;
	private String addressLine1;
	private String addressLine2;
	private String addressLine3;
	private String uin;
	private String nameOfPlan;
	private Long productId;
	private Long lifePremium;
	private String collectionNumber;
	private Date collectionDate;
	private String availableAmount;
	private Long currentService;
	private Long pastService;
	private List<PremiumDepositDto> depositDto;
	private Long gst;
}
