package com.lic.epgs.gratuity.policyservices.contributionadjustment.entity;

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
@Table(name = "PMST_CONTRI_ADJ_PROPS")
public class ContributionAdjustmentSearchPropsEntity {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "CONTRI_ADJ_PROPS_ID")
	private Long id;
	
	@Column(name = "CONRI_ADJ_NUMBER")
	private String contriAdjNumber;
	
	@Column(name = "PSTG_CONTRI_DETAIL_ID")
	private Long pstgContriDetailId;
	
	
	@Column(name = "PMST_CONTRI_DETAIL_ID")
	private Long pmstContriDetailId;
	

	
	@Column(name = "CONRI_ADJ_STATUS")
	private Long contriAdjStatus;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "TEMP_POLICY_ID", referencedColumnName = "POLICY_ID")
	private PolicyTempSearchEntity policyTmpContriProps;  


	
	@Column(name = "IS_ACTIVE")
	private Boolean isActive;
	
	@Column(name = "CREATED_BY", length = 128)
	private String createdBy;
	



}
