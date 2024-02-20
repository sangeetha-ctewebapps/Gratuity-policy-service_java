package com.lic.epgs.gratuity.policy.renewalpolicy.aocmcalculation.entity;

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
@Table(name = "TMP_DEATH_CLAIM")
public class DeathClaimTMPEntity {
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "TMP_DEATH_CLAIM_ID_SEQ")
	@SequenceGenerator(name = "TMP_DEATH_CLAIM_ID_SEQ", sequenceName = "TMP_DEATH_CLAIM_ID_SEQ", allocationSize = 1)
	@Column(name = "DEATH_CLAIM_ID")
	private Long Id;
	
	@Column(name = "TMP_POLICY_ID")
	private Long tmpPolicyId;
	
	@Column(name = "NO_OF_CLAIMS")
	private Long noOfClaim;
	
	@Column(name ="AMOUNT_OF_CLAIMS")
	private Long amountOfClaim;
	
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
