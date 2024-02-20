package com.lic.epgs.gratuity.policy.document.entity;

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
@Table(name = "PSTG_DOCUMENT")
public class PolicyDocumentEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "PSTG_DOCUMENT_ID_SEQ")
	@SequenceGenerator(name = "PSTG_DOCUMENT_ID_SEQ", sequenceName = "PSTG_DOCUMENT_ID_SEQ", allocationSize = 1)
	@Column(name = "DOCUMENT_ID")
	private Long id;
	
	@Column(name = "POLICY_ID")
	private Long policyId;
	
	@Column(name = "DOCUMENT_TYPE_ID")
	private Long documentTypeId;
	
	@Column(name = "EXPIRY_DATE")
	private Date expiryDate;
	
	@Column(name = "DOCUMENT_SUBTYPE_ID")
	private Long documentSubTypeId;
	
	@Column(name = "ISSUEDBY_ID")
	private Long issuedById;
	
	@Column(name = "ISSUED_DATE")
	private Date issuedDate;
	
	@Column(name = "DOCUMENT_NAME")
	private String documentName;
	
	@Column(name = "DOCUMENT_INDEX")
	private String documentIndex;
	
	@Column(name = "DOCUMENT_INDEX_NUMBER")
	private Long documentIndexNo;
	
	@Column(name = "FOLDER_INDEX_NUMBER")
	private Long folderIndexNo;
	
	@Column(name = "DOC_STATUS")
	private Long documentStatus;
	
	@Column(name = "IS_DELETED")
	private Boolean isDeleted;
	
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
}
