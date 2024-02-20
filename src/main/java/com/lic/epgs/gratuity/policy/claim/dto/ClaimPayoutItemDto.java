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
public class ClaimPayoutItemDto {

	private String debitCodeDescribtion;
	private String creditCodeDescribtion;
	private String debitcode;
	private String creditcode;
	private Double debitAmount;
	private Double creditAmount;

}