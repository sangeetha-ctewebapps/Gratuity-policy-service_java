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

import com.lic.epgs.gratuity.quotation.member.entity.MasterMemberNomineeEntity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "PMST_TMP_CLAIM_PROPS")
public class TempPolicyClaimPropsSearchEntity {
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "PMST_TMP_CLAIM_PROPS_ID_SEQ")
	@SequenceGenerator(name = "PMST_TMP_CLAIM_PROPS_ID_SEQ", sequenceName = "PMST_TMP_CLAIM_PROPS_ID_SEQ", allocationSize = 1)
	@Column(name = "CLAIM_PROPS_ID")
	private Long id;
	
	@Column(name = "MODE_OF_EXIT")
	private Long modeOfExit;
	
	@Column(name = "ONBOARD_NUMBER")
	private String onboardNumber;
	
	@Column(name = "CLAIM_STATUS_ID")
	private Long claimStatusId;
	
	@Column(name = "IS_ACTIVE")
	private Boolean isActive;
	
	@Column(name = "PAYOUT_NUMBER")
	private String payoutNo;
	
	@Column(name = "INTIMATION_NUMBER")
	private String initimationNumber;
	
	@Column(name ="CLAIM_TYPE")
	private String claimType;
	
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "TMP_POLICY_ID", referencedColumnName = "POLICY_ID")
	private PolicyTempSearchEntity policyTemp;  

	
//	(In child table)
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "TMP_MEMBER_ID", referencedColumnName = "MEMBER_ID")
	private TempMemberSearchEntity memberTmp;  
	
}
