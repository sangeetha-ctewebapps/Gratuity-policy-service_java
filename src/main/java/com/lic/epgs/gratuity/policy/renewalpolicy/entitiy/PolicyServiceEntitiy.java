package com.lic.epgs.gratuity.policy.renewalpolicy.entitiy;

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
@Table(name = "PMST_POLICY_SERVICE")
public class PolicyServiceEntitiy {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "PMST_POLICY_SERVICE_ID_SEQ")
	@SequenceGenerator(name = "PMST_POLICY_SERVICE_ID_SEQ", sequenceName = "PMST_POLICY_SERVICE_ID_SEQ", allocationSize = 1)
	@Column(name = "POLICY_SERVICE_ID")
	private Long Id;

	@Column(name = "POLICY_ID")
	private Long policyId;

	@Column(name = "PMST_HIS_POLICY_ID")
	private Long pmstHisPolicyId;

	@Column(name = "SERVICE_TYPE")
	private String serviceType;

	@Column(name = "MEMBER_ID")
	private Long memeberId;

	@Column(name = "REFERENCE_NUMBER")
	private String referenceNumber;

	@Column(name = "IS_ACTIVE")
	private Boolean isActive;

	@Column(name = "CREATED_BY", length = 128, updatable = false)
	private String createdBy;

	@Column(name = "CREATED_DATE", updatable = false)
	private Date createdDate;

	@Column(name = "MODIFIED_BY", length = 128)
	private String modifiedBy;

	@Column(name = "MODIFIED_DATE")
	private Date modifiedDate;

}
