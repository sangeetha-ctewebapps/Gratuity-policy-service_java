package com.lic.epgs.gratuity.policyservices.conversion.entity;

import java.io.Serializable;
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

/**
 * @author Logesh.D Date:20-09-2022
 *
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "POLICY_CONVERSION_TEMP")
public class PolicyLevelConversionTempEntity implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "CONV_TEMP_ID_Sequence")
	@SequenceGenerator(name = "CONV_TEMP_ID_Sequence", sequenceName = "CONV_TEMP_ID_Sequence", allocationSize = 1)
	@Column(name = "CONVERSION_ID", unique = true, nullable = false, updatable = false, length = 10)
	private Long conversionId;

	@Column(name = "SERVICE_ID")
	private Long serviceId;

	@Column(name = "CONVERSION_DATE")
	private Date conversionDate = new Date();

	@Column(name = "PREV_POLICY_ID")
	private Long prevPolicyId;

	@Column(name = "PREV_POLICY_NO", length = 20)
	private String prevPolicyNo;

	@Column(name = "CLAIM_PENDING", length = 2)
	private String claimPending;

	@Column(name = "NEW_POLICY_ID")
	private Long newPolicyId;

	@Column(name = "NEW_POLICY_NO", length = 20)
	private String newPolicyNo;

	@Column(name = "NEW_POLICY_ARD")
	private Date newPolicyAnnualRenewalDate = new Date();

	@Column(name = "NEW_POLICY_STATUS")
	private Long newpolicyStatus;

	@Column(name = "NEW_POLICY_ISSUE_DT")
	private Date newpolicyIssueDate = new Date();

	@Column(name = "NEW_POLICY_MPH_CODE", length = 20)
	private String newPolicyMphCode;

	@Column(name = "NEW_POLICY_MPH_NAME")
	private String newPolicyMphName;

	@Column(name = "NEW_POLICY_PROD")
	private Long newPolicyProduct;

	@Column(name = "NEW_POLICY_VARIANT")
	private Long newPolicyVariant;

	@Column(name = "NO_OF_CATG")
	private Long noOfCatalogue;

	@Column(name = "PREV_FUND_BALANCE", length = 15)
	private Long prevFundBalancde;

	@Column(name = "CREATED_BY")
	private String createdBy;

	@Column(name = "CREATED_ON")
	private Date createdOn = new Date();

	@Column(name = "MODIFIED_BY")
	private String modifiedBy;

	@Column(name = "MODIFIED_ON")
	private Date modifiedOn = new Date();

	@Column(name = "CONVERSION_STATUS")
	private Integer conversionStatus;

	@Column(name = "CONVERSION_WORKFLOW_STATUS")
	private Integer conversionWorkflowStatus;

	@Column(name = "IS_ACTIVE")
	private Boolean isActive = true;

	@Column(name = "UNIT_CODE")
	private String unitCode;

	@Column(name = "REJECTION_REASON_CODE")
	private String rejectionReasonCode;

	@Column(name = "REJECTION_REMARKS")
	private String rejectionRemarks;

}