package com.lic.epgs.gratuity.policyservices.aom.entity;

import java.io.Serializable;
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
@Table(name = "PMST_TMP_AOM_PROPS")
public class PolicyTmpAOMProps implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "AOM_PROPS_ID_SEQ")
	@SequenceGenerator(name = "AOM_PROPS_ID_SEQ", sequenceName = "AOM_PROPS_ID_SEQ", allocationSize = 1)
	@Column(name = "AOM_PROPS_ID", unique = true, nullable = false, updatable = false, length = 10)
	private Long aOMPropsId;

	@Column(name = "TMP_POLICY_ID")
	private Long tmpPolicyId;

	@Column(name = "TMP_MEMBER_ID")
	private Long tmpMemberId;

	@Column(name = "SERVICE_NUMBER")
	private Long serviceNumber;

	@Column(name = "AOM_STATUS_ID")
	private Long aomStatusId;

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

	@Column(name = "PSGT_CONTRIBUTION_DETAIL_ID")
	private Long pstgContributionDetailId;

	@Column(name = "PMST_CONTRIBUTION_DETAIL_ID")
	private Long pmstContributionDetailId;

}