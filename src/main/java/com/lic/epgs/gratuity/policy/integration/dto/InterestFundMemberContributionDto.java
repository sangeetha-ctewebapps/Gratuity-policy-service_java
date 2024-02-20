package com.lic.epgs.gratuity.policy.integration.dto;

/**
 * 
 */


import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Builder.Default;
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
@Builder
public class InterestFundMemberContributionDto {

	private static final long serialVersionUID = 1L;

	private Long id;

	private String memberId;

	private Long policyId;

	private @Default Boolean isActive = Boolean.TRUE;

	private String remarks;

	private String calcLogic;

	private String txnType;

	private BigDecimal employerContributionAmount;
	private BigDecimal employeeContributionAmount;
	private BigDecimal voluntaryContributionAmount;
	private BigDecimal totalContributionAmount;

	private BigDecimal employerActualIntrestCal;

	private BigDecimal employerAirAmount;

	private BigDecimal employerFmcAmount;

	private BigDecimal employerFmcAmountPerRate;

	private BigDecimal employerFmcGstAmount;

	private BigDecimal employerMfrAmount;

	private BigDecimal employerRaAmount;

	private BigDecimal employerWihdrawalAmount;

	private BigDecimal employeeActualIntrestCal;

	private BigDecimal employeeAirAmount;

	private BigDecimal employeeFmcAmount;

	private BigDecimal employeeFmcAmountPerRate;

	private BigDecimal employeeFmcGstAmount;

	private BigDecimal employeeMfrAmount;

	private BigDecimal employeeRaAmount;

	private BigDecimal employeeWihdrawalAmount;

	private BigDecimal volnContributionactualIntrestCal;

	private BigDecimal volnContributionairAmount;

	private BigDecimal volnContributionfmcAmount;

	private BigDecimal volnContributionfmcAmountPerRate;

	private BigDecimal volnContributionfmcGstAmount;

	private BigDecimal volnContributionmfrAmount;

	private BigDecimal volnContributionraAmount;

	private BigDecimal volnContributionwihdrawalAmount;

}
