package com.lic.epgs.gratuity.policy.entity;

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

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "PMST_CONTRIBUTION_DETAIL")
public class MasterPolicyContributionDetails {
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "PMST_CONTRIB_DTL_ID_SEQ")
	@SequenceGenerator(name = "PMST_CONTRIB_DTL_ID_SEQ", sequenceName = "PMST_CONTRIB_DTL_ID_SEQ", allocationSize = 1)
	@Column(name = "CONTRIBUTION_DETAIL_ID")
	private Long id;

	@Column(name = "POLICY_ID")
	private Long policyId;

	@Column(name = "MASTER_POLICY_ID")
	private Long masterPolicyId;
	
	@Column(name = "CHALLAN_NO")
	private String challanNo;

	@Column(name = "LIFE_PREMIUM")
	private Double lifePremium;

	@Column(name = "GST")
	private Double gst;

	@Column(name = "PAST_SERVICE")
	private Double pastService;

	@Column(name = "CURRENT_SERVICE")
	private Double currentServices;

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
	
	@Column(name="ENTRY_TYPE")
	private String entryType;
	
	@Column(name="ADJUSTMENT_FOR_DATE")
	private Date adjustmentForDate;
	
	@Column(name = "FINANCIAL_YEAR")
	private String financialYear;
	
	@Column(name = "PAST_SERVICE_BALANCE")
	private Double pastServiceBalance;
	
	@Column(name = "CURRENT_SERVICE_BALANCE")
	private Double currentServiceBalance;
	
	@Column(name="STAMP_DUTY")
	private Double stampDuty;
	
	@Column(name = "LATE_FEE")
	private Double lateFee;
	
	@Column(name = "GST_ON_LATE_FEE")
	private Double gstOnLateFee;
	
	
	
	
}