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
public class ClaimResDto {
	private Long id;
	private Long policyId;
	private Double transactionAmount;
	private Double closingBalance;
	private Double openingBalance;
	private String status;
}
