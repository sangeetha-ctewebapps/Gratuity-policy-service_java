package com.lic.epgs.gratuity.policy.claim.dto;

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
public class ClaimPayoutDto {

	private String policyNumber;
	private String unitCode;
	private String mphName;
	private Long claimPropsId;
	private Long productVariatId;;
	private Date payoutDate;
	private Long productId;
	private Long contributionDetailId;
	private Long challanNo;
	private Date challanDate;


	private List<ClaimPayoutItemDto> claimPayoutItemDto;
	
}