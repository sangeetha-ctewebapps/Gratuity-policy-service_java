package com.lic.epgs.gratuity.policyservices.aom.entity;

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

import com.lic.epgs.gratuity.policy.claim.entity.TempMphSearchEntity;
import com.lic.epgs.gratuity.policy.renewalpolicy.entitiy.PolicyServiceSearchEntity;

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
public class AOMSearchEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "PMST_TMP_POLICY_ID_SEQ")
	@SequenceGenerator(name = "PMST_TMP_POLICY_ID_SEQ", sequenceName = "PMST_TMP_POLICY_ID_SEQ", allocationSize = 1)
	@Column(name = "POLICY_ID")
	private Long id;

	@Column(name = "CUSTOMER_NAME")
	private String customerName;

	@Column(name = "POLICY_NUMBER")
	private String policyNumber;

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

	@Column(name = "QUOTATION_STATUS_ID")
	private Long quotationStatusId;

	@Transient
	@Column(name = "UNIT_ID")
	private String unitId;

	@Column(name = "UNIT_CODE")
	private String unitCode;

	@Column(name = "MASTER_POLICY_ID")
	private Long masterPolicyId;

	@Transient
	@Column(name = "UNIT_OFFICE")
	private String unitOffice;

	@Column(name = "POLICY_STATUS_ID")
	private Long policyStatusId;

	@Column(name = "POLICY_TAGGED_STATUS_ID")
	private Long policytaggedStatusId;

	@Column(name = "QUOTATION_TAGGED_STATUS_ID")
	private Long quotationTaggedStatusId;

	@Column(name = "CUSTOMER_CODE")
	private String customerCode;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "MPH_ID", referencedColumnName = "MPH_ID")
	private TempMphSearchEntity policyMPHTmp;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "POLICY_SERVICE_ID", insertable = false, updatable = false)
	private PolicyServiceSearchEntity policyTmp;

}
