package com.lic.epgs.gratuity.policy.surrender.dto;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonPropertyOrder({"variant", "surrenderId", "policyNumber", "exitLoad", "gstOnExitLoad", "surrenderCharges", "gstOnSurrenderCharges", "mvaApplicable", "exitAmount", 
	"percentageWithdrawn", "isBulkExit", "mvf", "mvaCharges", "mvaDeductionCharges", "gstOnMVACharges", "surrenderAmount"})
public class SurrenderPaymentResponse {

	private String variant;
	private Long surrenderId;
	private String policyNumber;
	private BigDecimal surrenderCharges;
	private BigDecimal exitLoad;
	private BigDecimal gstOnExitLoad;
	private BigDecimal surrenderAmount;
	private String mvaApplicable;
	private BigDecimal mvaCharges;
	private BigDecimal gstOnMVACharges;
	private BigDecimal gstOnSurrenderCharges;
	private BigDecimal exitAmount;
	private String isBulkExit;
	private int percentageWithdrawn;
	private double mvf;
}
