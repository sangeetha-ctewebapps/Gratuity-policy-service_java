package com.lic.epgs.gratuity.policyservices.conversion.dto;

import java.math.BigInteger;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
public class TrnRegisModel {

	private String referenceIdType;
	private String van;
	private BigInteger challanNo;
	private String policyNo;
	private String proposalNo;
	private String referenceDate;
	private String amount;
	private String amountType;
	private String productCode;
	private String variantCode;
	private String validityUptoDate;
	private Long status;
	private String createdBy;
	private String poolAccountCode;
	private String bankUniqueId;
	private String reason;

}
