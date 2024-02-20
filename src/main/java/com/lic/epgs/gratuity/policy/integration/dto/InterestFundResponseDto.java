package com.lic.epgs.gratuity.policy.integration.dto;

/**
 * 
 */

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Builder.Default;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * @author Muruganandam
 *
 */

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonPropertyOrder(alphabetic = true)
public class InterestFundResponseDto implements Serializable {
	/**
	* 
	*/
	private static final long serialVersionUID = 1L;

	private String policyNumber;
	private String licId;
	private Long policyId;
	private Long memberId;
	private @Default BigDecimal openingBalanceAmount = BigDecimal.ZERO;
	private @Default BigDecimal openingBalanceInterestAmount = BigDecimal.ZERO;
	private @Default BigDecimal totalContributionReceivedAmount = BigDecimal.ZERO;
	private @Default BigDecimal totalContributionInterestAmount = BigDecimal.ZERO;
	private @Default BigDecimal totalDebitAmount = BigDecimal.ZERO;
	private @Default BigDecimal totalDebitInterestAmount = BigDecimal.ZERO;
	private @Default BigDecimal totalFmcChargeAmount = BigDecimal.ZERO;
	private @Default BigDecimal totalFmcGstAmount = BigDecimal.ZERO;
	private @Default BigDecimal policyAccountValue = BigDecimal.ZERO;
	private @Default BigDecimal airAmount = BigDecimal.ZERO;
	private @Default BigDecimal mfrAmount = BigDecimal.ZERO;
	private @Default BigDecimal raAmount = BigDecimal.ZERO;

	private @Default BigDecimal totalClaimAmount = BigDecimal.ZERO;
	private @Default BigDecimal totalClaimInterestAmount = BigDecimal.ZERO;

	/** @JsonIgnore(value = true) */
	@JsonProperty(value = "availableBalance")
	private @Default BigDecimal totalTxnAmount = BigDecimal.ZERO;

	@JsonIgnore(value = true)
	private @Default BigDecimal totalInterestAmount = BigDecimal.ZERO;

	@JsonPropertyDescription(value = "Product Name eg., DB/DC")
	private String product;

	@JsonPropertyDescription(value = "Variant eg., V1/V2/V3")
	private String variant;

	@JsonProperty(value = "frequencyPeriod")
	private String currentQuarter;

	@JsonPropertyDescription(value = "Financial Year eg., 2022-2023")
	private String financialYear;

	private Integer reconDays;

//	@JsonIgnore(value = true)
//	private AccountsDto policyDetails;
//
//	@JsonIgnore(value = true)
//	private MembersDto memberDetails;

	private String batchDate;

	private String tranFromDate;
	private String tranToDate;
	private String remarks;
	private String batchNumber;

	private String batchStatus;
	private String batchType;
	private String mphName;
	private String memberName;
	private String unitId;
	private Long fundSummaryId;
	@JsonProperty(value = "summary")
	private transient @Default Map<String, Object> process = Collections.emptyMap();

	private @Default Double interestRate = 0.0;

	/****
	 * private List<AccountsDto> totalContributionReceivedHistory; private
	 * List<AccountsDto> totalContributionInterestHistory; private List<AccountsDto>
	 * totalDebitHistory; private List<AccountsDto> totalDebitInterestHistory;
	 * private List<AccountsDto> totalFmcChargeHistory;
	 */

}
