package com.lic.epgs.gratuity.policy.premiumadjustment.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "PMST_CONTRI_SUMMARY")
public class MasterPolicyContrySummaryEntity {
	
	

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "PMST_POL_CONT_SUMM_ID_SEQ")
	@SequenceGenerator(name = "PMST_POL_CONT_SUMM_ID_SEQ", sequenceName = "PMST_POL_CONT_SUMM_ID_SEQ", allocationSize = 1)
	@Column(name = "POL_CONT_SUMMARY_ID")
	private Long id;
	@Column(name = "POLICY_ID")
	private Long policyId;
	@Column(name = "FINANCIAL_YEAR")
	private String financialYear;
	@Column(name = "OPENING_BALANCE")
	private Long openingBalance;
	@Column(name = "CLOSING_BALANCE")
	private Long closingBalance;
	@Column(name = "STAMP_DUTY")
	private Double stampDuty;
	@Column(name = "TOTAL_CONTRIBUTION")
	private Long totalContribution;
	@Column(name = "TOTAL_EMPLOYEE_CONTRIBUTION")
	private Long totalEmployeeContribution;
	@Column(name = "TOTAL_EMPLOYER_CONTRIBUTION")
	private Long totalEmployerContribution;
	@Column(name = "TOTAL_VOLUNTARY_CONTRIBUTION")
	private Long totalVoluntaryContribution;
	@Column(name = "IS_ACTIVE")
	private boolean isActive;
	@Column(name = "CREATED_BY")
	private String createdBy;
	@Column(name = "CREATED_DATE")
	private Date createdDate;
	@Column(name = "MODIFIED_BY")
	private String modifiedBy;
	@Column(name = "MODIFIED_DATE")
	private Date modifiedDate;
	@Column(name="CONTRIBUTION_DETAIL_ID")
	private Long contributionDetailId;

}
