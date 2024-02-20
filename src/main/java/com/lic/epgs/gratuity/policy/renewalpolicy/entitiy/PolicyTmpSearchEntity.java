package com.lic.epgs.gratuity.policy.renewalpolicy.entitiy;

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
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

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
public class PolicyTmpSearchEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "PMST_TMP_POLICY_ID_SEQ")
	@SequenceGenerator(name = "PMST_TMP_POLICY_ID_SEQ", sequenceName = "PMST_TMP_POLICY_ID_SEQ", allocationSize = 1)
	@Column(name = "POLICY_ID")
	private Long id;

	@Column(name = "POLICY_NUMBER")
	private String policyNumber;

	@Column(name = "CUSTOMER_NAME")
	private String customerName;

	@Column(name = "INTERMEDIARY_NAME")
	private String intermediaryName;

	@Column(name = "LINE_OF_BUSINESS")
	private String lineOfBusiness;

	@Transient
	@Column(name = "PRODUCT_NAME")
	private String productName;

	@Transient
	@Column(name = "PRODUCT_VARIANT")
	private String productVariant;

	@Column(name = "PRODUCT_ID")
	private Long productId;

	@Column(name = "PRODUCT_VARIANT_ID")
	private Long productVariantId;

	@Transient
	@Column(name = "UNIT_ID")
	private String unitId;

	@Column(name = "UNIT_CODE")
	private String unitCode;

	@Transient
	@Column(name = "UNIT_OFFICE")
	private String unitOffice;

	@Column(name = "POLICY_STATUS_ID")
	private Long policyStatusId;

	@Column(name = "POLICY_SUB_STATUS_ID")
	private Long policySubStatusId;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy", timezone = "asia/kolkata")
	@Column(name = "POLICY_START_DATE")
	private Date policyStartDate;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy", timezone = "asia/kolkata")
	@Column(name = "POLICY_END_DATE")
	private Date policyEndDate;

	@Column(name = "POLICY_SERVICE_ID")
	private Long policyServiceid;

	@Column(name = "IS_ACTIVE")
	private Boolean isActive;

	@Column(name = "MASTER_POLICY_ID")
	private Long masterPolicyId;

	@Column(name = "QUOTATION_STATUS_ID")
	private Long quotationStatusId;

	@Column(name = "QUOTATION_SUB_STATUS_ID")
	private Long quotationSubStatusId;

	@Column(name = "QUOTATION_TAGGED_STATUS_ID")
	private Long quotationTaggedStatusId;

	@Column(name = "QUOTATION_NUMBER")
	private String quotationNumber;

	@JsonIgnore
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "POLICY_SERVICE_ID", insertable = false, updatable = false)
	private PolicyServiceSearchEntity policyTmp;

	@Column(name = "FCL_UPTO_AGE ")
	private Integer fclUptoAge;

	@Column(name = "MID_LEAVER_ALLOWED")
	private Boolean midLeaverAllowed;

	@Column(name = "MID_JOINER_ALLOWED")
	private Boolean midJoinerAllowed;

}