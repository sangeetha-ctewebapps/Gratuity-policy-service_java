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
public class PolicyContrySummarydto {

	private Long id;
	private Long policyId;
	private String financialYear;
	private Long openingBalance;
	private Long closingBalance;
	private Double stampDuty;
	private Long totalContribution;
	private Long totalEmployeeContribution;
	private Long totalEmployerContribution;
	private Long totalVoluntaryContribution;
	private boolean isActive;
	private String createBy;
	private Date createDate;
	private String modifiedBy;
	private Date modifiedDate;
	private Long contributionDetailId;
	

}
