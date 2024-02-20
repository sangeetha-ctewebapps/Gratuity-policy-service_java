package com.lic.epgs.gratuity.policyservices.premiumcollection.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.lic.epgs.gratuity.policy.claim.entity.PolicyTempSearchEntity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "PMST_LC_PREM_COLL_PROPS")
public class LCPremiumCollectionSearchEntity {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "LC_PREM_COLL_PROPS_ID")
	private Long id;

	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "TEMP_POLICY_ID", referencedColumnName = "POLICY_ID")
	private PolicyTempSearchEntity policyPremCollectProps;  
	
	@Column(name ="PREM_ADJ_STATUS")
	private Long premAdjStatus;
	
	
	@Column(name = "WAIVE_LATE_FEE")
	private Boolean waiveLateFee;
	
	@Column(name = "IS_ESCALATED_TO_ZO")
	private Boolean isEscalatedToZo;
	
	@Column(name="LC_PREM_COLL_NUMBER")
	private String lcPremCollNumber;
	
	
	
	
}
