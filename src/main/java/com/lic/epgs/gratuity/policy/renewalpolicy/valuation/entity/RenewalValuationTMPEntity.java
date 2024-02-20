package com.lic.epgs.gratuity.policy.renewalpolicy.valuation.entity;

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
@Table(name = "PMST_TMP_VALUATION")
public class RenewalValuationTMPEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "PMST_TMP_VALUATION_ID_SEQ")
	@SequenceGenerator(name = "PMST_TMP_VALUATION_ID_SEQ", sequenceName = "PMST_TMP_VALUATION_ID_SEQ", allocationSize = 1)
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
	
	@Column(name = "TMP_POLICY_ID")
	private Long tmpPolicyId;
	
	@Column(name = "PMST_VALUATION_ID")
	private Long pmstValuationId;

	
	@Column(name = "PMST_HIS_VALUATION_ID")
	private Long pmstHisValuationId;
	

	
	}

	
	
	

