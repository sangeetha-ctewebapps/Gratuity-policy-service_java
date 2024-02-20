package com.lic.epgs.gratuity.policy.gratuitybenefit.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.lic.epgs.gratuity.quotation.gratuitybenefit.entity.MasterGratuityBenefitEntity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "PMST_GRATUITY_BENEFIT_PROPS")
public class PolicyGratuityBenefitPropsEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "PMST_GRAT_BENEF_PROPS_ID_SEQ")
	@SequenceGenerator(name = "PMST_GRAT_BENEF_PROPS_ID_SEQ", sequenceName = "PMST_GRAT_BENEF_PROPS_ID_SEQ", allocationSize = 1)
	@Column(name = "GRATUITY_BENEFIT_PROPS_ID")
	private Long id;
	
	@ManyToOne
    @JoinColumn(name="GRATUITY_BENEFIT_ID", nullable=false)
	private PolicyGratuityBenefitEntity gratuityBenefit;
	
	@Column(name = "NO_OF_YEARS_OF_SERVICE")
	private Integer numberOfYearsOfService;
	
	@Column(name = "NO_OF_DAYS_WAGE")
	private Integer numberOfDaysWage;
	
	@Column(name = "NO_OF_WORKING_DAYS_PER_MONTH")
	private Integer numberOfWorkingDaysPerMonth;
	
	@Column(name = "GRATUITY_CEILING_AMOUNT")
	private Double gratuityCellingAmount;
	
	@Column(name = "MONTHLY_CEILING")
	private Double monthlyCelling;
	
	@Column(name = "SALARY_CEILING")
	private Double salaryCelling;
	
	@Column(name = "RETIREMENT_AGE")
	private Integer retirementAge;
	
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




}
