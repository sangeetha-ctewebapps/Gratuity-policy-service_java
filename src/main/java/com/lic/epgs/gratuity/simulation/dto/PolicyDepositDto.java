package com.lic.epgs.gratuity.simulation.dto;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

@Data
public class PolicyDepositDto {

	private Long id;
	private Long policyId;
	private Long masterPolicyId;
	private Long tmpPolicyId;
	private Double adjustmentAmount;
	private Long adjConId;
	private Double availableAmount;
	private String challanNo;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy", timezone = "asia/kolkata")
	private Date chequeRealistionDate;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy", timezone = "asia/kolkata")
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
