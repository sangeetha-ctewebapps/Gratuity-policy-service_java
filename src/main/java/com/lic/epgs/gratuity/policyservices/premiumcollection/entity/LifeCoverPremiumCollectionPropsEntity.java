package com.lic.epgs.gratuity.policyservices.premiumcollection.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
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
@Table(name = "PMST_LC_PREM_COLL_PROPS")
public class LifeCoverPremiumCollectionPropsEntity{
	
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "LC_PREM_COLL_PROPS_ID")
	private Long id;
	
	@Column(name="LC_PREM_COLL_NUMBER")
	private String lcPremCollNumber;
	
	
	@Column(name="PSTG_CONTRI_DETAIL_ID")
	private Long pstgContriDetailId;
	
	@Column(name="PMST_CONTRI_DETAIL_ID")
	private Long pmstContriDetailId;
	
	@Column(name="AMT_DUE_TO_BE_ADJUSTED")
	private Double amtDueToBeAdjusted;
	
	@Column(name="LC_PREMIUM_AMOUNT")
	private Double lcPremiumAmount;
	
	@Column(name="GST_ON_LC_PREMIUM")
	private Double gstOnLCPremium;
	
	@Column(name="LATE_FEE")
	private Double lateFee;
	
	
	
	@Column(name ="GST_ON_LATE_FEE")
	private Double gstOnLateFee;
	
	@Column(name ="ADJUSTMENT_FOR_DATE")
	private Date adjustmentForDate;
	
	@Column(name ="PREM_ADJ_STATUS")
	private Long premAdjStatus;
	
	@Column(name ="REJECTED_REMARKS")	
	private String rejectedRemarks;
	

	@Column(name ="REJECTED_REASON")
	private String rejectedReason;
	
	@Column(name ="TEMP_POLICY_ID")
	private Long tmpPolicyId;
	
	@Column(name = "IS_ACTIVE")
	private Boolean isActive;
	
	@Column(name = "CREATED_BY")
	private String createdBy;
	
	@Column(name = "CREATED_DATE")
	private Date createdDate;
	
	@Column(name = "MODIFIED_BY")
	private String modifiedBy;
	
	@Column(name = "MODIFIED_DATE")
	private Date modifiedDate;
	
	@Column(name = "WAIVE_LATE_FEE")
	private Boolean waiveLateFee;
	
	@Column(name = "IS_ESCALATED_TO_ZO")
	private Boolean isEscalatedToZo;
	
}