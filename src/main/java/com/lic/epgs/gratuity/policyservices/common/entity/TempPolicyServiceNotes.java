package com.lic.epgs.gratuity.policyservices.common.entity;

import java.util.Date;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.lic.epgs.gratuity.mph.entity.MPHAddressEntity;
import com.lic.epgs.gratuity.mph.entity.MPHBankEntity;
import com.lic.epgs.gratuity.mph.entity.MPHRepresentativesEntity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "PS_PMST_TMP_NOTES")
public class TempPolicyServiceNotes {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "PS_PMST_TMP_NOTES_ID_SEQ")
	@SequenceGenerator(name = "PS_PMST_TMP_NOTES_ID_SEQ", sequenceName = "PS_PMST_TMP_NOTES_ID_SEQ", allocationSize = 1)
	@Column(name = "NOTES_ID")
	private Long id;

	@Column(name = "PMST_TMP_POLICY_ID")
	private Long referenceId;

	@Column(name = "POLICY_NUMBER")
	private String policyNumber;

	@Column(name = "SERVICE_NUMBER")
	private String serviceNumber;

	@Column(name = "NOTES")
	private String notes;

	@Column(name = "NOTES_TYPE")
	private String referenceServiceType;

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
