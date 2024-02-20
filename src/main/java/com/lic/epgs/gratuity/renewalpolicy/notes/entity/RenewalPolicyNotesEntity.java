package com.lic.epgs.gratuity.renewalpolicy.notes.entity;

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
@Table(name = "PMST_TMP_NOTES")
public class RenewalPolicyNotesEntity {
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "PMST_TMP_NOTES_ID_SEQ")
	@SequenceGenerator(name = "PMST_TMP_NOTES_ID_SEQ", sequenceName = "PMST_TMP_NOTES_ID_SEQ", allocationSize = 1)
	@Column(name = "NOTES_ID")
	private Long id;
	
	@Column(name = "POLICY_ID")
	private Long policyId;
	
	@Column(name = "NOTES", length = 1000)
	private String note;
	
	@Column(name = "NOTES_TYPE")
	private String notesType;
	
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
	
	@Column(name = "ENDORSEMENT_ID")
	private Long endorsementId;
	
	@Column(name = "TMP_POLICY_ID")
	private Long tmpPolicyId;

}
