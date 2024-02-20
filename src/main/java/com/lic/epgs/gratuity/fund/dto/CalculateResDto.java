package com.lic.epgs.gratuity.fund.dto;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author Gopi
 *
 */

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class CalculateResDto {
	private Double airAmount;
	private Double availableBalance;
	private String financialYear;
	private String frequencyPeriod;
	private Long fundSummaryId;
	private Double interestRate;
	private Double mfrAmount;
	private Double openingBalanceAmount;
	private Double openingBalanceInterestAmount;
	private Double policyAccountValue;
	private Long policyId;
	private String policyNumber;
	private Double raAmount;
	private Long reconDays;
	private String remarks;
	private Double totalContributionInterestAmount;
	private Double totalContributionReceivedAmount;
	private Double totalDebitAmount;
	private Double totalDebitInterestAmount;
	private Double totalFmcChargeAmount;
	private Double totalFmcGstAmount;
}
