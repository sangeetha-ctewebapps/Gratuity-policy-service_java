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
@Table(name = "PSTG_CONTRIBUTION")
public class PolicyContributionEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "PSTG_CONTRIBUTION_ID_SEQ")
	@SequenceGenerator(name = "PSTG_CONTRIBUTION_ID_SEQ", sequenceName = "PSTG_CONTRIBUTION_ID_SEQ", allocationSize = 1)
	@Column(name = "CONTRIBUTION_ID")
	private Long id;
	@Column(name = "POLICY_ID")
	private Long policyId;
	@Column(name = "MASTER_POLICY_ID")
	private Long masterPolicyId;
	@Column(name = "TMP_POLICY_ID")
	private Long tmpPolicyId;
	
	@Column(name = "ADJ_CON_ID")
	private Long adjConid;
	@Column(name = "CLOSING_BALANCE")
	private Double closingBlance;
	@Column(name = "CONT_REFERENCE_NO")
	private String contReferenceNo;
	@Column(name = "CONTRIBUTION_DATE")
	private Date contributionDate;
	@Column(name = "CONTRIBUTION_TYPE")
	private String contributionType;
	@Column(name = "EMPLOYEE_CONTRIBUTION")
	private Double employeeContribution;
	@Column(name = "EMPLOYER_CONTRIBUTION")
	private Double employerContribution;
	@Column(name = "FINANCIAL_YEAR")
	private String financialYear;
	@Column(name = "IS_DEPOSIT")
	private Boolean isDeposit;
	@Column(name = "OPENING_BALANCE")
	private Double openingBalance;
	@Column(name = "REG_CON_ID")
	private Long regConId;
	@Column(name = "TOTAL_CONTRIBUTION")
	private Double totalContribution;
	@Column(name = "TXN_ENTRY_STATUS")
	private Long txnEntryStatus;
	@Column(name = "VERSION_NO")
	private Long versionNo;
	@Column(name = "VOLUNTARY_CONTRIBUTION")
	private Double voluntaryContribution;
	@Column(name = "ZERO_ACCOUNT_ENTRIES")
	private Long zeroAccountEntries;
	@Column(name = "IS_ACTIVE")
	private boolean isActive;
	@Column(name = "CREATED_BY")
	private String createdBy;
	@Column(name = "CREATED_DATE")
	private Date createDate;
	@Column(name = "MODIFIED_BY")
	private String modifyedBy;
	@Column(name = "MODIFIED_DATE")
	private Date modifiedDate;
	@Column(name = "QUARTER")
	private String quarter;
	@Column(name="CONTRIBUTION_DETAIL_ID")
	private Long contributionDetailId;
	
	@Column(name = "EFFECTIVE_DATE")
	private Date effectiveDate;
	
	
	

}
