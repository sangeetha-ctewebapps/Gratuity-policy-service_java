package com.lic.epgs.gratuity.policy.integration.dto;

/**
 * 
 */

import java.math.BigDecimal;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author Muruganandam
 *
 */

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class DebitRequestDto {
	private String proposalNumber;

	@NotEmpty
	private String policyNumber;
	private String memberId;
	private String txnType;
	private String variant;
	private String policyType;
	@NotNull
	private BigDecimal txnAmount;
	@NotEmpty
	private String txnDate;
	private String stream;
	private String module;
	private String remarks;
	private String frequency;

	private String message;
	private Integer statusId;
	private String status;

	private String createdBy;
	private String modifiedBy;
	private String depositId;

	@JsonProperty(required = true)
	@NotBlank(message = "Collection No. is mandatory")
	private String collectionNo;
	private InterestFundMemberContributionDto memberContribution;
}
