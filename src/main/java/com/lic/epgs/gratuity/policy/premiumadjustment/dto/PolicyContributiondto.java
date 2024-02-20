package com.lic.epgs.gratuity.policy.premiumadjustment.dto;

import java.util.Date;

import javax.persistence.Column;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class PolicyContributiondto {
	private Long contributionId;
	private Long policyId;
	private Long masterPolicyId;
	private Long adjConid;
	private Long closingBlance;
	private String contReferenceNo;
	private Date contributionDate;
	private Date contributionType;
	private Long employeeContribution;
	private Long employerContribution;
	private String financialYear;
	private Boolean isDeposit;
	private Long openingBalance;
	private Long regConId;
	private Long totalContribution;
	private Long txnEntryStatus;
	private Long versionNo;
	private Long voluntaryContribution;
	private Long zeroAccountEntries;
	private boolean isActive;
	private String createBy;
	private Date createDate;
	private String modifyedBy;
	private Date modifiedDate;
	private String quarter;
	private Long contributionDetailId;
	

}
