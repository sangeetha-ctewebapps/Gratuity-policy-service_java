package com.lic.epgs.gratuity.policy.renewalpolicy.entitiy;

import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.lic.epgs.gratuity.policy.claim.entity.TempMemberSearchEntity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "PMST_POLICY_SERVICE")
public class PolicyServiceSearchEntity {
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "PMST_POLICY_SERVICE_ID_SEQ")
	@SequenceGenerator(name = "PMST_POLICY_SERVICE_ID_SEQ", sequenceName = "PMST_POLICY_SERVICE_ID_SEQ", allocationSize = 1)
	@Column(name = "POLICY_SERVICE_ID")
	private Long Id;
	
	@Column(name = "PMST_HIS_POLICY_ID")
	private Long pmstHisPolicyId;

	@Column(name = "SERVICE_TYPE")
	private String serviceType;
	
	@Column(name = "IS_ACTIVE")
	private Boolean isActive;
	
	
	
	@OneToMany(mappedBy = "policyTmp", cascade = CascadeType.ALL)
	private List<PolicyTmpSearchEntity> serviceSearch;

}