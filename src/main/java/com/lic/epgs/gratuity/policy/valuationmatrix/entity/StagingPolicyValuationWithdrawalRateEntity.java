package com.lic.epgs.gratuity.policy.valuationmatrix.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "PSTG_VALUATIONWITHDRAWALRATE")
public class StagingPolicyValuationWithdrawalRateEntity {
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "PSTG_VALWITHDRAWALRATE_ID_SEQ")
	@SequenceGenerator(name = "PSTG_VALWITHDRAWALRATE_ID_SEQ", sequenceName = "PSTG_VALWITHDRAWALRATE_ID_SEQ", allocationSize = 1)
	@Column(name = "VALUATIONWITHDRAWALRATE_ID ")
	private Long id;
	
	@Column(name = "POLICY_ID ")
	private Long policyId;
	
	@Column(name = "FROM_AGE_BAND ")
	private Long fromAgeBand;
	
	@Column(name = "TO_AGE_BAND ")
	private Long toAgeBand;
	
	@Column(name = "RATE ")
	private Long rate;

	@Column(name = "IS_ACTIVE")
	private Boolean isActive;

	@Column(name = "CREATED_BY")
	private String createdBy;

	@Column(name = "CREATED_DATE")
	private Date createdDate;

	@Column(name = "MODIFIED_BY")
	private String modifiedBy;

	@Column(name = "MODIFIED_DATE")
	private Date modifiedDate;


}
