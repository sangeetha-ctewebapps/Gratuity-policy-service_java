package com.lic.epgs.gratuity.fund.dto;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonPropertyOrder(alphabetic = true)
public class PolicyFundReportDto  implements Serializable {
	private static final long serialVersionUID = 1L;

	private String policyNumber;
	private String licId;
	private String firstName;
	private String openingBalance;
	private String totalContribution;
	private String openingBalanceInt;
	private String intrestContribution;
	private String fmc;
	private String gst;
	private String closingBalance;
	private String transFromDate;
	private String transToDate;
	private String unitId;
}
