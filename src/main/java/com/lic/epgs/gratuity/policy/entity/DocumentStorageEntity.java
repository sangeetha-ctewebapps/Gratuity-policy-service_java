package com.lic.epgs.gratuity.policy.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Builder
@Table(name = "PMST_DOC_STORE")
public class DocumentStorageEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "PMST_DOC_STORE_ID_SEQ")
	@SequenceGenerator(name = "PMST_DOC_STORE_ID_SEQ", sequenceName = "PMST_DOC_STORE_ID_SEQ", allocationSize = 1)
	@Column(name = "DOC_STORE_ID")
	private Long id;

	@Column(name = "POLICY_ID")
	private Long policyid;

	@Column(name = "DOC_TYPE_ID")
	private Long doctypeId;

	@Lob
	@Column(name = "DOC_BLOB")
	private byte[] docBlob;

	@Column(name = "IS_ACTIVE")
	private boolean isActive;

	@Column(name = "CREATED_BY")
	private String createdBy;

	@Column(name = "CREATED_DATE")
	private Date createdDate;

	@Column(name = "MODIFIED_BY")
	private String modifiedby;

	@Column(name = "MODIFIED_DATE")
	private Date modifiedDate;

	

}
