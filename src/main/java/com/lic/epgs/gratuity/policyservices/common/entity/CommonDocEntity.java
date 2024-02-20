package com.lic.epgs.gratuity.policyservices.common.entity;

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
@Table(name = "PS_COMMON_DOC")
public class CommonDocEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "PS_COMMON_DOC_SEQ")
	@SequenceGenerator(name = "PS_COMMON_DOC_SEQ", sequenceName = "PS_COMMON_DOC_SEQ", allocationSize = 1)
	@Column(name = "DOCUMENT_ID")
	private Long documentId;

	@Column(name = "QUOTATION_ID")
	private Long quotationId;

	@Column(name = "DOCUMENT_TYPE_ID")
	private Long documentTypeId;

	@Column(name = "DOCUMENT_SUB_TYPE_ID")
	private Long documentSubTypeId;

	@Column(name = "EXPIRY_DATE")
	private Date expiryDate;

	@Column(name = "ISSUED_BY_ID")
	private Long issuedById;

	@Column(name = "ISSUED_DATE")
	private Date issuedDate;

	@Column(name = "DOCUMENT_NAME")
	private String documentName;

	@Column(name = "DOCUMENT_INDEX")
	private String documentIndex;

	@Column(name = "DOCUMENT_INDEX_NO")
	private Long documentIndexNo;

	@Column(name = "FOLDER_INDEX_NO")
	private Long folderIndexNo;

	@Column(name = "DOCUMENT_STATUS")
	private Long documentStatus;

	@Column(name = "POLICY_NUMBER")
	private String policyNumber;

	@Column(name = "SERVICE_NUMBER")
	private String serviceNumber;

	@Column(name = "REFERENCE_ID")
	private Long referenceId;

	@Column(name = "REFERENCE_SERVICE_TYPE")
	private String referenceServiceType;

	@Column(name = "IS_DELETED")
	private Boolean isDeleted;

	@Column(name = "CREATED_BY")
	private String createdBy;

	@Column(name = "CREATED_ON")
	private Date createdOn;

	@Column(name = "MODIFIED_BY")
	private String modifiedBy;

	@Column(name = "MODIFIED_ON")
	private Date modifiedOn;

}