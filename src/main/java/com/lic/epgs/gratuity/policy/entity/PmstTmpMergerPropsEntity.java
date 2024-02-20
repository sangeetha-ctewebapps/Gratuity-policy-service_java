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
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;



@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "PMST_TMP_MERGER_PROPS")
public class PmstTmpMergerPropsEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "PMST_TMP_MERGER_PROPS_ID_SEQ")
	@SequenceGenerator(name = "PMST_TMP_MERGER_PROPS_ID_SEQ", sequenceName = "PMST_TMP_MERGER_PROPS_ID_SEQ", allocationSize = 1)
	@Column(name="TMP_MERGER_PROPS_ID")
	private Long id;
	
	@Column(name="MERGER_REQUEST_NUMBER")
	private String mergerRequestNumber;
	
	@Column(name="MERGER_REQUEST_DATE")
	private Date mergerRequestDate;
	
	@Column(name="MERGER_TYPE")
	private Integer mergerType;
	
	@Column(name="SOURCE_TMP_POLICY_ID")
	private Long sourceTmpPolicyID;
	
	@Column(name="DESTINATION_TMP_POLICY_ID")
	private Long destinationTmpPolicyID;
	
	
	@Column(name="SOURCE_POLICY_ID")
	private Long sourcePolicyID;
	
	@Column(name="DESTINATION_POLICY_ID")
	private Long destinationPolicyID;
	
	@Column(name="SOURCE_ACCRUED_INTEREST")
	private Double sourceAccruedInterest;
	
	@Column(name="SOURCE_PRIOR_TOTAL_FUND")
	private Double sourcePriorTotalFund;
	
	
	@Column(name="SOURCE_PRIOR_FUND_VALUE")
	private Double sourcePriorFundValue;
	
	
	@Column(name="DESTINATION_PRIOR_FUND_VALUE")
	private Double destinationPriorFundValue;
	
	
	@Column(name="DESTINATION_PRIOR_TOTAL_VALUE")
	private Double destinationPriorTotalValue;
	
	
	@Column(name="DEDUCT_FROM")
	private Long deductFrom;
	
	@Column(name="STATUS_ID")
	private Long statusID;
	
	
	@Column(name="IS_ACTIVE")
	private Boolean isActive;
	
	
	@Column(name="CREATED_BY")
	private String createdBy;
	
	@Column(name="CREATED_DATE")
	private Date createdDate;
	
	@Column(name="MODIFIED_BY")
	private String modifiedBy;
	
	@Column(name="MODIFIED_DATE")
	private Date modifiedDate;
	
	@Column(name="REFUND_PREM_AMOUNT")
	private Double premiumRefundAmount;
	
	@Column(name="REFUND_GST_AMOUNT")
	private Double gstRefundAmount;

	@Column(name="PREMIUM_REFUND_REQUIRED")
	private String isPremiumRefundRequired;
}

