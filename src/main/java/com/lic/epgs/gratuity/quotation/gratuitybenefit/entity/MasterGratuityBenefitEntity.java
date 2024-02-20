package com.lic.epgs.gratuity.quotation.gratuitybenefit.entity;

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

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author Ismail Khan A
 *
 */

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "QMST_GRATUITY_BENEFIT")
public class MasterGratuityBenefitEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "QMST_GRAT_BENEF_ID_SEQ")
	@SequenceGenerator(name = "QMST_GRAT_BENEF_ID_SEQ", sequenceName = "QMST_GRAT_BENEF_ID_SEQ", allocationSize = 1)
	@Column(name = "GRATUITY_BENEFIT_ID")
	private Long id;
	
	@Column(name = "QUOTATION_ID")
	private Long quotationId;
	
	@OneToMany(mappedBy="gratuityBenefit", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Set<MasterGratuityBenefitPropsEntity> gratuityBenefits;
		
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
