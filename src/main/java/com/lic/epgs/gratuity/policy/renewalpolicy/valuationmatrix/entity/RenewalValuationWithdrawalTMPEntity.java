package com.lic.epgs.gratuity.policy.renewalpolicy.valuationmatrix.entity;

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
@Table(name = "PMST_TMP_VALUATWITHDRAWALRATE")
public class RenewalValuationWithdrawalTMPEntity {


	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "PMST_TMP_VAL_WDRL_RT_ID_SEQ")
	@SequenceGenerator(name = "PMST_TMP_VAL_WDRL_RT_ID_SEQ", sequenceName = "PMST_TMP_VAL_WDRL_RT_ID_SEQ", allocationSize = 1)
	@Column(name = "VALUATIONWITHDRAWALRATE_ID ")
	private Long  id;
	
	@Column(name = "POLICY_ID")
	private Long policyId;
	
	@Column(name = "FROM_AGE_BAND")
	private Long fromAgeBand;
	
	@Column(name = "TO_AGE_BAND")
	private Long toAgeband;
	
	@Column(name = "RATE")
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

	@Column(name = "TMP_POLICY_ID")
	private Long tmpPolicyId;
	
	@Column(name = "PMST_VALWITHDRRATE_ID")
	private Long pmstValWithDrawalId;
	
	@Column(name = "PMST_HIS_VALUATWDRATE_ID")
	private Long pmstHisValWithDrawalId;
	
	@Column(name = "RECORDTYPE")
	private String recordType;
	
}
