package com.lic.epgs.gratuity.policy.renewalpolicy.entitiy;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "PMST_TMP_POLICY")
public class RenewalPolicyTMPEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "PMST_TMP_POLICY_ID_SEQ")
	@SequenceGenerator(name = "PMST_TMP_POLICY_ID_SEQ", sequenceName = "PMST_TMP_POLICY_ID_SEQ", allocationSize = 1)
	@Column(name = "POLICY_ID")
	private Long id;

	@Column(name = "MPH_ID")
	private Long masterpolicyHolder;

	@Column(name = "POLICY_NUMBER")
	private String policyNumber;

	@Column(name = "POLICY_SERVICE_ID")
	private Long policyServiceId;

	@Column(name = "QUOTATION_NUMBER")
	private String quotationNumber;

	@Column(name = "MASTER_QUOTATION_ID")
	private Long masterQuotationId;

	@Column(name = "MASTER_POLICY_ID")
	private Long masterPolicyId;

	@Column(name = "TMP_POLICY_ID")
	private Long tmpPolicyId;

	@Column(name = "GST_APPLICABLE_ID")
	private Long gstApplicableId;

	@Column(name = "QUOTATION_DATE")
	private Date quotationDate;

	@Column(name = "POLICY_DISPATCH_DATE")
	private Date policyDispatchDate;

	@Column(name = "POLICY_RECIEVED_DATE")
	private Date policyRecievedDate;

	@Column(name = "POLICY_START_DATE")
	private Date policyStartDate;

	@Column(name = "POLICY_END_DATE")
	private Date policyEndDate;

	@Column(name = "ANNUAL_RENEWAL_DATE")
	private Date annualRenewlDate;

	@Column(name = "LAST_PREMIUM_PAID")
	private Date lastPremiumPaid;

	@Column(name = "MAXIMUM_STAMP_FEE")
	private Float maximumStampFee;

	@Column(name = "TOTAL_MEMBER")
	private Long totalMember;

	@Column(name = "POLICY_STATUS_ID")
	private Long policyStatusId;

	@Column(name = "POLICY_SUB_STATUS_ID")
	private Long policySubStatusId;

	@Column(name = "POLICY_TAGGED_STATUS_ID")
	private Long policytaggedStatusId;

	@Column(name = "PRODUCT_ID")
	private Long productId;

	@Transient
	@Column(name = "PRODUCT_NAME")
	private String productName;

	@Transient
	@Column(name = "PRODUCT_VARIANT")
	private String productVariant;

	@Column(name = "PRODUCT_VARIANT_ID")
	private Long productVariantId;

	@Column(name = "LINE_OF_BUSINESS")
	private String lineOfBusiness;

	@Column(name = "CONTACT_PERSON_ID")
	private Long contactPersonId;

	@Column(name = "BANK_ACCOUNT_ID")
	private Long bankAccountId;

	@Column(name = "IS_APPROVAL_ESCALATED_TO_CO")
	private Long isApprovalEscalatedToGo;

	@Column(name = "IS_APPROVAL_ESCALATED_TO_ZO")
	private Long isApprovalEscalatedToZo;

	@Column(name = "IS_MIN_PAYMENT_RECEIVED")
	private Long isMinPaymentRecevied;

	@Column(name = "IS_FULL_PAYMENT_RECEIVED")
	private Long isFullPaymentReceived;

	@Transient
	@Column(name = "UNIT_ID")
	private String unitId;

	@Column(name = "UNIT_CODE")
	private String unitCode;

	@Transient
	@Column(name = "UNIT_OFFICE")
	private String unitOffice;

	@Column(name = "MARKETING_OFFICER_CODE")
	private String marketingOfficerCode;

	@Column(name = "MARKETING_OFFICER_NAME")
	private String marketingOfficerName;

	@Column(name = "INTERMEDIARY_CODE")
	private String intermediaryCode;

	@Column(name = "INTERMEDIARY_NAME")
	private String intermediaryName;

	@Column(name = "ADVANCE_OR_ARREARS")
	private String advanceOrArrears;

	@Column(name = "CONTRIBUTION_FREQUENCY_ID")
	private Long contributionFrequencyId;

	@Column(name = "MEMBER_UNIQUE_IDENTIFIER_ID")
	private Long memberUniqueIdendifierId;

	@Column(name = "RISK_COMMENCEMENT_DATE")
	private Date riskCommencementDate;

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

	@Column(name = "LAST_LIC_ID")
	private Long lastLicId;

	@Column(name = "CUSTOMER_NAME")
	private String customerName;

	@Column(name = "CUSTOMER_CODE")
	private String customerCode;

	@Column(name = "INDUSTRY_TYPE")
	private String industryType;

	@Column(name = "IS_STD_VALUES_DEVIATED")
	private Boolean isStdValuesDeviated;

	@Column(name = "QUOTATION_STATUS_ID")
	private Long quotationStatusId;

	@Column(name = "QUOTATION_SUB_STATUS_ID")
	private Long quotationSubStatusId;

	@Column(name = "QUOTATION_TAGGED_STATUS_ID")
	private Long quotationTaggedStatusId;

	@Column(name = "REJECTED_REASON", length = 255)
	private String rejectedReason;

	@Column(name = "REJECTED_REMARKS", length = 2000)
	private String rejectedRemarks;

	@Column(name = "PMST_HIS_POLICY_ID")
	private Long pmstHisPolicyId;

	@Column(name = "PAYTO")
	private Long payto;
	
	@Column(name="PROPOSAL_NUMBER")
	private String proposalNumber;
	
	@Column(name = "NEXT_DUE_DATE")
	private Date nextDueDate;
	
	@Column(name ="DATE_OF_COMMENCEMENT")
	private Date dateOfCommencement;
}
