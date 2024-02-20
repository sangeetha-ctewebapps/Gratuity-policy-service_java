package com.lic.epgs.gratuity.policy.valuation.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "PSTG_VALUATION")
public class PolicyValuationEntity {
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "PSTG_VALUATION_ID_SEQ")
	@SequenceGenerator(name = "PSTG_VALUATION_ID_SEQ", sequenceName = "PSTG_VALUATION_ID_SEQ", allocationSize = 1)
	@Column(name = "VALUATION_ID ")
	private Long id;
	
	@Column(name = "POLICY_ID")
	private Long policyId;
	
	
	@Column(name = "SALARY_ESCALATION")
	private Float salaryEscalation;
	
	@Column(name = "DISCOUNT_RATE")
	private Float discountRate;
	
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
