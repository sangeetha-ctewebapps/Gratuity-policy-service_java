package com.lic.epgs.gratuity.policy.renewalpolicy.gratuitybenefit.entity;

import java.util.Date;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.lic.epgs.gratuity.policy.gratuitybenefit.entity.PolicyGratuityBenefitPropsEntity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "PMST_TMP_GRATUITY_BENEFIT")
public class RenewalGratuityBenefitTMPEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "PMST_TMP_GRAT_BENEF_ID_SEQ")
	@SequenceGenerator(name = "PMST_TMP_GRAT_BENEF_ID_SEQ", sequenceName = "PMST_TMP_GRAT_BENEF_ID_SEQ", allocationSize = 1)
	@Column(name = "GRATUITY_BENEFIT_ID")
	private Long id;
	
	@Column(name = "POLICY_ID")
	private Long policyId;
	
	@OneToMany(mappedBy="renewalGratuityBenefitTMPEntity", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Set<RenewalsGratuityBenefitPropsTMPEntity> RenewalgratuityPropsBenefit;
		
	@Column(name = "CATEGORY_ID")
	private Long categoryId;
	
	@Column(name = "GRATUITY_BENEFIT_TYPE_ID")
	private Long gratutiyBenefitTypeId;
	
	@Column(name = "GRATUITY_SUB_BENEFIT_ID")
	private Long gratutiySubBenefitId;
	
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
	
	@Column(name = "TMP_POLICY_ID")
	private Long tmpPolicyId;
	
	@Column(name = "PMST_GRATUITY_BENEFIT_ID")
	private Long pmstGratutiyBenefitId;

	@Column(name = "PMST_HIS_GRATUITY_BENEFIT_ID")
	private Long pmstHisGratutiyBenefitId;
	
	@Column(name="GRATUITY_NO_SLAB_BENEFIT_TYPE_ID")
	private Long gratuityNoSlabBenefitTypeId;

	

}
