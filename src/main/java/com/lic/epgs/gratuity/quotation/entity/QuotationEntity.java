package com.lic.epgs.gratuity.quotation.entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.lic.epgs.gratuity.quotation.member.entity.MemberBatchEntity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author Gopi
 *
 */

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "QSTG_QUOTATION")
public class QuotationEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "QSTG_QUOTATION_ID_SEQ")
	@SequenceGenerator(name = "QSTG_QUOTATION_ID_SEQ", sequenceName = "QSTG_QUOTATION_ID_SEQ", allocationSize = 1)
	@Column(name = "QUOTATION_ID")
	private Long id;
	
	
	@Column(name = "QUOTATION_NUMBER")
	private String number;
	
	@Column(name = "PROPOSAL_NUMBER")
	private String proposalNumber;
	
	@Column(name = "QUOTATION_DATE")
	private Date date;
	
	@Column(name = "STATUS_ID")
	private Long statusId;
	
	@Column(name = "SUB_STATUS_ID")
	private Long subStatusId;
	
	@Column(name = "TAGGED_STATUS_ID")
	private Long taggedStatusID;
	
	@Column(name = "DATE_OF_COMMENCEMENT")
	private Date dateOfCommencement;
	
	@Column(name = "GST_APPLICABLE_ID")
	private Long gstApplicableId;
	
	@Column(name = "CAT_FOR_GST_NON_APPLICABLE_ID")
	private Long categoryForGstNonApplicableId;
	
	@Column(name = "CONTACT_PERSON_ID")
	private Long contactPersonId;
	
	
	@Column(name = "REJECTED_REASON", length = 255)
	private String rejectedReason;
	
	@Column(name = "REJECTED_REMARKS", length = 2000)
	private String rejectedRemarks;

	
	@Column(name = "BANK_ACCOUNT_ID")
	private Long bankAccountId;
	
	@Column(name = "CUSTOMER_CODE", length = 20)
	private String customerCode;
	
	@Column(name = "UNIT_OFFICE_ID")
	private Long unitOfficeId;
	
	@Column(name = "PRODUCT_ID")
	private Long productId;
	
	@Column(name = "PRODUCT_VARIANT_ID")
	private Long productVariantId;
	
	@Column(name = "BUSINESS_TYPE_ID")
	private Long businessTypeId;
	
	@Column(name = "RISK_GROUP_ID")
	private Long riskGroupId;
	
	@Column(name = "GROUP_SIZE_ID")
	private Long groupSizeId;
	
	@Column(name = "GROUP_SUMASSURED_ID")
	private Long groupSumAssuredId;
	
	@Column(name = "FREQUENCY_ID")
	private Long frequencyId;
	

	@Column(name = "IS_APPROVAL_ESCALATED_TO_ZO")
	private Boolean isEscalatedToZO;
	
	@Column(name = "IS_APPROVAL_ESCALATED_TO_CO")
	private Boolean isEscalatedToCO;
	
	@Column(name = "MEMBER_UNIQUE_IDENTIFIER_ID")
	private Long memberUniqueIdentifierId;
	
	@Column(name = "IS_STD_VALUES_DEVIATED")
	private Boolean isStdValuesDeviated;
	
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
	
	
	@Column(name = "PAYTO")
	 private  Long payto;
	
	@Column(name = "UNIT_CODE")
	private String unitCode;
	
	@Column(name = "INDUSTRY_TYPE")
	private  String industryType;

	@Column(name = "ZO_ESCALATION_REASON")
	private String zoEscalationReason;
	
	@Column(name ="FCL_UPTO_AGE ")
	private Integer fclUptoAge;
	
	@Column(name ="REINSURANCE_APPLICABLE") 
	private Boolean reinsuranceApplicable;
	
	@Column(name ="MID_LEAVER_ALLOWED")
	private Boolean midLeaverAllowed;
	
	@Column(name ="MID_JOINER_ALLOWED")
	private Boolean midJoinerAllowed;
	
}
