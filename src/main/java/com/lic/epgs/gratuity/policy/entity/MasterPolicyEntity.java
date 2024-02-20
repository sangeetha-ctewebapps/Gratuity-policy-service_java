package com.lic.epgs.gratuity.policy.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
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
@Table(name = "PMST_POLICY")
public class MasterPolicyEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "PMST_POLICY_SEQ")
	@SequenceGenerator(name = "PMST_POLICY_SEQ", sequenceName = "PMST_POLICY_SEQ", allocationSize = 1)
	@Column(name = "POLICY_ID")
	private Long id;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "MPH_ID", referencedColumnName = "MPH_ID", insertable = false, updatable = false)
	private MphSearchEntity policyMPHTmp;

	@Column(name = "MPH_ID")
	private Long masterpolicyHolder;

	@Column(name = "POLICY_NUMBER")
	private String policyNumber;

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

	@Column(name = "POLICY_TAGGED_STATUS_ID")
	private Long policyTaggedStatusId;

	@Column(name = "PRODUCT_ID")
	private Long productId;

	@Transient
	@Column(name = "PRODUCT_NAME")
	private String productName;

	@Transient
	@Column(name = "PRODUCT_VARIANT")
	private String productVariant;

	@Column(name = "LINE_OF_BUSINESS")
	private String lineOfBusiness;

	@Column(name = "CONTACT_PERSON_ID")
	private Long contactPersonId;

	@Column(name = "BANK_ACCOUNT_ID")
	private Long bankAccountId;

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

	@Column(name = "PREMIUM_MODE")
	private Long contributionFrequencyId;

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

	@Column(name = "GST_APPLICABLE_ID")
	private Long gstApplicableId;

	@Column(name = "RISK_COMMENCEMENT_DATE")
	private Date riskCommencementDate;

	@Column(name = "PAYTO")
	private Long payto;

	@Column(name = "PRODUCT_VARIANT_ID")
	private Long productVariantId;

	@Column(name = "PROPOSAL_NUMBER")
	private String proposalNumber;

	@Column(name = "OLD_POLICY_NUMBER")
	private String oldPolicyNumber;

	@Column(name = "DATE_OF_COMMENCEMENT")
	private Date dateOfCommencement;

	@Column(name = "NEXT_DUE_DATE")
	private Date nextDueDate;
	
	@Column(name ="FCL_UPTO_AGE ")
	private Integer fclUptoAge;
	
	@Column(name ="MID_LEAVER_ALLOWED")
	private Boolean midLeaverAllowed;
	
	@Column(name ="MID_JOINER_ALLOWED")
	private Boolean midJoinerAllowed;

}
