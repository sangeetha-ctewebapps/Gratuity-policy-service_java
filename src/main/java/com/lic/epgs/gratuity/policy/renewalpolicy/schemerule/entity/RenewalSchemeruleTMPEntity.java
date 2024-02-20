package com.lic.epgs.gratuity.policy.renewalpolicy.schemerule.entity;

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
@Table(name = "PMST_TMP_SCHEMERULE")
public class RenewalSchemeruleTMPEntity {
	
		@Id
		@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "PMST_TMP_SCHRULE_ID_SEQ")
		@SequenceGenerator(name = "PMST_TMP_SCHRULE_ID_SEQ", sequenceName = "PMST_TMP_SCHRULE_ID_SEQ", allocationSize = 1)
		@Column(name = "SCHEMERULE_ID")
		private Long id;
		
		@Column(name = "POLICY_ID")
		private Long policyId;
	
		@Column(name = "POLICY_SERVICE_ID")
		private Long policyServiceId;
		
		@Column(name = "FCL_TYPE_ID")
		private Long fclTypeId;
		
		@Column(name = "PREMIUM_ADJUSTMENT_TYPE_ID")
		private Long premiumAadjustmentTypeId;
		
		@Column(name = "MINIMUM_GRATUITY_SERVICE_YEAR")
		private int minimumGratuityServiceYear;
		
		@Column(name = "MINIMUM_GRATUITY_SERVICE_MONTH")
		private int minimumGratuityServiceMonth;
		
		@Column(name = "MINIMUM_GRATUITY_SERVICE_DAY")
		private int minimumGratuityServiceDay;
		
		@Column(name = "FCL")
		private Double fcl;
		
		@Column(name = "IS_ACTIVE")
		private Boolean isActive;
		
		@Column(name = "CREATED_BY", length = 128)
		private String createdBy;
		//where this field in this entity
		@Column(name = "CREATED_DATE")
		private Date createdDate;
		
		@Column(name = "MODIFIED_BY", length = 128)
		private String modifiedBy;
		
		@Column(name = "MODIFIED_DATE")
		private Date modifiedDate;
		
		@Column(name = "TMP_POLICY_ID")
		private Long tmpPolicyId;
		
		@Column(name = "PMST_SCHEME_RULE_ID")
		private Long pmstSchemeRuleId;
		
		@Column(name = "PMST_HIS_SCHEMERULE_ID")
		private Long pmstHisSchemeRuleId;

	}


