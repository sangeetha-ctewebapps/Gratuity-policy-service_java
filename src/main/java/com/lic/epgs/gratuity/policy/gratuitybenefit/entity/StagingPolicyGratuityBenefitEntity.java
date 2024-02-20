package com.lic.epgs.gratuity.policy.gratuitybenefit.entity;

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

import com.lic.epgs.gratuity.quotation.gratuitybenefit.entity.GratuityBenefitPropsEntity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "PSTG_GRATUITY_BENEFIT")
public class StagingPolicyGratuityBenefitEntity {
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "PSTG_GRAT_BENEF_ID_SEQ")
	@SequenceGenerator(name = "PSTG_GRAT_BENEF_ID_SEQ", sequenceName = "PSTG_GRAT_BENEF_ID_SEQ", allocationSize = 1)
	@Column(name = "GRATUITY_BENEFIT_ID")
	private Long id;
	
	@Column(name = "POLICY_ID")
	private Long policyId;
	
	@OneToMany(mappedBy="gratuityBenefit", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Set<StagingPolicyGratuityBenefitPropsEntity> gratuityBenefits;
		
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
	

	@Column(name="GRATUITY_NO_SLAB_BENEFIT_TYPE_ID")
	private Long gratuityNoSlabBenefitTypeId;

}
