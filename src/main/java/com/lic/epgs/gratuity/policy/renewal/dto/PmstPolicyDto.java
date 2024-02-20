package com.lic.epgs.gratuity.policy.renewal.dto;

import java.io.Serializable;
import java.util.Date;

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
public class PmstPolicyDto implements Serializable {

	private Long id;

	private Date annualRenewlDate;
	/*
	 * private String policyNumber; private String customerName; private String
	 * intermediaryName; private Long lineOfBusinessId; private Long productId;
	 * private String productVariant; private String unitOffice; private Date
	 * policyStartDate; private Date policyEndDate; private String taggedStatusId;
	 * private Long policyStatusId;
	 */
}
