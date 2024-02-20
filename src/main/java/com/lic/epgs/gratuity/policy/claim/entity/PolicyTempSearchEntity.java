package com.lic.epgs.gratuity.policy.claim.entity;

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

import com.lic.epgs.gratuity.policyservices.contributionadjustment.entity.ContributionAdjustmentPropsEntity;
import com.lic.epgs.gratuity.policyservices.contributionadjustment.entity.ContributionAdjustmentSearchPropsEntity;
import com.lic.epgs.gratuity.policyservices.premiumcollection.entity.LCPremiumCollectionSearchEntity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@ToString
@Table(name = "PMST_TMP_POLICY")
public class PolicyTempSearchEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "PMST_TMP_POLICY_ID_SEQ")
	@SequenceGenerator(name = "PMST_TMP_POLICY_ID_SEQ", sequenceName = "PMST_TMP_POLICY_ID_SEQ", allocationSize = 1)
	@Column(name = "POLICY_ID")
	private Long id;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "MPH_ID", referencedColumnName = "MPH_ID")
	private TempMphSearchEntity policyMPHTmp;

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

	@Column(name = "QUOTATION_NUMBER")
	private String quotationNumber;

	@Column(name = "CUSTOMER_NAME")
	private String customerName;

	@Column(name = "CUSTOMER_CODE")
	private String customerCode;

	@Transient
	@Column(name = "UNIT_ID")
	private String unitId;

	@Column(name = "UNIT_CODE")
	private String unitCode;

	@Transient
	@Column(name = "UNIT_OFFICE")
	private String unitOffice;

	@Column(name = "POLICY_NUMBER")
	private String policyNumber;

	@OneToMany(mappedBy = "policyTmp", cascade = CascadeType.ALL)
	private List<TempMemberSearchEntity> policySearch;

	@OneToMany(mappedBy = "policyTemp", cascade = CascadeType.ALL)
	private List<TempPolicyClaimPropsSearchEntity> tmpPolicySearch;

	@OneToMany(mappedBy = "policyTmpContriProps", cascade = CascadeType.ALL)
	private List<ContributionAdjustmentSearchPropsEntity> tmpContriAdjustSearch;

	@OneToMany(mappedBy = "policyPremCollectProps", cascade = CascadeType.ALL)
	private List<LCPremiumCollectionSearchEntity> tmpPremCollectPropsSearch;

}
