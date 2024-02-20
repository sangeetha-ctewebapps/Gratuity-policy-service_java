package com.lic.epgs.gratuity.policyservices.merger.dto;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class SearchSourcePolicyDto implements Serializable {
	
	private static final Long serialVersionUID = 1L;
	
	private String policyNumber; //PMST_POLICY
	private String mphName; //PMST_MPH
	private String annualRenewalDate; //PMST_POLICY
	private String contributionFrequencyId; //PMST_POLICY changed this, earlier it was contributionFrequencyId
	private String masterPolicyStatus; //PMST_CONTRIBUTION
	private Double employeeContribution; //PMST_CONTRIBUTION
	private Double employerContribution; //PMST_CONTRIBUTION
	private Double voluntaryContribution; //PMST_CONTRIBUTION
	private Double totalContribution; //PMST_CONTRIBUTION
	private Integer numberOfLives; //PMST_VALUATION_BASIC
	private String frequency; //PICK_LIST_ITEM
	
}
