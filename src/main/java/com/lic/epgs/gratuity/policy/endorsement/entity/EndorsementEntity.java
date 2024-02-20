package com.lic.epgs.gratuity.policy.endorsement.entity;

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
@Table(name = "PMST_ENDORSEMENT")
public class EndorsementEntity {
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "PMST_ENDORSEMENT_ID_SEQ")
	@SequenceGenerator(name = "PMST_ENDORSEMENT_ID_SEQ", sequenceName = "PMST_ENDORSEMENT_ID_SEQ", allocationSize = 1)
	@Column(name = "ENDORSEMENT_ID")
	private Long id;

	
	@Column(name = "ENDORSEMENT_NUMBER")
	private String endorsementNumber;
	
	@Column(name = "POLICY_ID")
	private Long policyId;
	
	@Column(name = "ENDORSEMENT_STATUS_ID")
	private Long endorsementStatusId;
	
	@Column(name = "ENDORSEMENT_SUB_STATUS_ID")
	private Long endorsementSubStatusId;
	
	@Column(name = "REQUEST_DATE")
	private Date requestDate;
	
	@Column(name = "REQUEST_RECEIVED_FROM")
	private String requestReceivedFrom;
	
	@Column(name = "ENDORSEMENT_TYPE_ID")
	private Long endorsementTypeId;
	
	@Column(name = "ENDORSEMENT_SUB_TYPE_ID")
	private Long endorsementSubtypeId;
	
	@Column(name = "SERVICE_TYPE_ID")
	private Long serviceTypeId;
	
	@Column(name = "EFFECTIVE_TYPE_ID")
	private Long effectiveTypeId;
	
	@Column(name = "EFFECTIVE_DATE")
	private Date effectiveDate;
	
	@Column(name = "SERVICE_NUMBER")
	private String serviceNumber;
	
	@Column(name = "COMMUNICATION_OPTION_ID")
	private Long communicationOptionId;
	
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
