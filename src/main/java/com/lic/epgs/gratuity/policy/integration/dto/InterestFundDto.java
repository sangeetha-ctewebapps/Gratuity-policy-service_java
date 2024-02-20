package com.lic.epgs.gratuity.policy.integration.dto;

/**
 * 
 */


import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonProperty;

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
public class InterestFundDto {

	private Long id;

	private Long fundPolicyId;

	private Integer statusId;

	private String status;

	private String errorMessage;

	private Long policyId;

	private Long approvedPolicyId;

	private Long approvedMemberId;

	private String unitCode;

	private String proposalNumber;

	private String memberId;

	@JsonProperty(required = true)
	@NotBlank(message = "Policy No. is mandatory")
	private String policyNumber;

	private Integer quotationId;

	@JsonProperty(required = true)
	@NotBlank(message = "Transaction Type i.e., CREDIT/DEBIT is mandatory")
	private String txnType;

	@JsonProperty(required = true)
	@NotBlank(message = "Transaction Type i.e., CREDIT/DEBIT is mandatory")
	private String txnSubType;

	private String customerName;

	private String trnxDate;

	@JsonProperty(required = true)
	@NotBlank(message = "Transaction Effective Date is mandatory")
	private String effectiveTxnDate;

	@JsonProperty(required = true)
	@DecimalMin(value = "1.0", message = "Transaction Amount is required")
	private BigDecimal txnAmount;

	private BigDecimal closingBalance;

	private Integer rekonNoOfDays;

	private Integer fmcRekonNoOfDays;

	private BigDecimal actualInterestAmount;

	private String modifiedBy;

	private String modifiedOn;

	private String createdBy;

	@JsonProperty(required = true)
	@NotBlank(message = "Policy Type is empty i.e., DB or DC")
	private String policyType;

	private String createdOn;

	private @Default Boolean isActive = Boolean.TRUE;

	private String remarks;

	private @Default Boolean isOpeningBal = false;

	private BigDecimal airAmount;

	private BigDecimal mfrAmount;

	private BigDecimal raAmount;

	@JsonProperty(required = true)
	@NotBlank(message = "Variant is mandatory i.e., V2/V2/V3")
	private String variant;

	private @Default Boolean isZeroAccount = Boolean.FALSE;

	private BigDecimal fmcAmount;

	private BigDecimal withdrawalAmount;

	private BigDecimal gstAmount;

	private String quarter;

	private String financialYear;

	@JsonProperty(required = true)
	@NotBlank(message = "Stream is mandatory")
	private String stream;

	private BigDecimal openingBalance;

	private BigDecimal fmcAmountPerRate;

	private BigDecimal fmcGstAmount;

	private String depositId;

	@JsonProperty(required = true)
	@NotBlank(message = "Collection No. is mandatory")
	private String collectionNo;

	private InterestFundMemberContributionDto membercontribution;

	private Boolean isExit;

	private String entityType;
	private @Default BigDecimal averageBalanceAmount = BigDecimal.ZERO;
	private Date lastBatchOn;

	private List<Long> contributionIds;

	private Long memberTxnRefNo;

	private Long policyTxnRefNo;

	private @Default Boolean txnEntryStatus = Boolean.FALSE;

	private String saveType;
}
