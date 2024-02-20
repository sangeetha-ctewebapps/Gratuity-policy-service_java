package com.lic.epgs.gratuity.policy.premiumadjustment.dto;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class PolicyDepositdto {

	
	private Long id;
	private Long policyId;
	private Long masterPolicyId;
	private Double adjustmentAmount;
	private Long  adjConId;
	private Double availableAmount;
	private String challanNo;
	private Date chequeRealistionDate;
	private Date collectionDate;
	private Long collectionNo;
	private String collectionStatus;
	private String contributionType;
	private Double depositAmount;
	private Boolean isDeposit;
	private Long regConId;
	private String remark;
	private String status;
	private String transactionMode;
	private Long zeroId;
	private boolean isActive;
	private String createdBy;
	private Date createDate;
	private String modifiedBy;
	private Date modifiedDate;
	private Long contributionDetailId;
	
}
