package com.lic.epgs.gratuity.policy.integration.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author Logesh.D Date:16-09-2022
 *
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class PortfolioDataDto {
	
	private int iCodeForLob;

	private int iCodeForProductLine;

	private int iCodeForVariant;

	private int iCodeForBusinessType;

	private int iCodeForParticipatingType;

	private int iCodeForBusinessSegment;

	private int iCodeForInvestmentPortfolio;

}
