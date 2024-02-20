package com.lic.epgs.gratuity.policyservices.memberlevel.transfer.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "PMST_CONTRI_ADJ_PROPS")
public class ContriAdjustmentPropsEntity {

	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "CONTRI_ADJ_PROPS_ID")
	private Long id;
	
	@Column(name = "CONRI_ADJ_NUMBER")
	private String contriAdjNumber;
	
	@Column(name = "PSTG_CONTRI_DETAIL_ID")
	private Long pstgContriDetailId;
	
	
	@Column(name = "PMST_CONTRI_DETAIL_ID")
	private Long pmstContriDetailId;
	
	@Column(name = "AMT_TO_BE_ADJUSTED")
	private Double amtToBeAdjusted;
	
	@Column(name = "FIRST_PREMIUM")
	private Double firstPremiumPS;
	
	@Column(name = "SINGLE_PREMIUM_FIRSTYR")
	private Double singlePremiumFirstYearCS;
	
	@Column(name = "ADJUSTMENT_FOR_DATE")
	private Date adjustmentForDate;
	
	@Column(name = "CONRI_ADJ_STATUS")
	private Long contriAdjStatus;
	
	@Column(name = "REJECTED_REASON")
	private String rejectedReason;
	
	@Column(name = "REJECTED_REMARKS")
	private String rejectedRemarks;
	
	@Column(name = "TEMP_POLICY_ID")
	private Long tmpPolicyId;
	
	@Column(name = "IS_ACTIVE")
	private Boolean isActive;
	
	@Column(name = "CREATED_BY", length = 128)
	private String createdBy;
	
	@Column(name = "CREATED_DATE")
	private Date createdDate;
	
	@Column(name = "MODIFIED_BY", length = 128)
	private String modifiedBy;
	
	@Column(name = "MODIFIED_DATE")
	private Date modifiedDate;


}
