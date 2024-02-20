package com.lic.epgs.gratuity.quotation.member.entity;

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
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author Gopi
 *
 */

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "QSTG_MEMBER_BATCH_FILE")
public class MemberBatchFileEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "QSTG_MEMBER_BATCH_FILE_ID_SEQ")
	@SequenceGenerator(name = "QSTG_MEMBER_BATCH_FILE_ID_SEQ", sequenceName = "QSTG_MEMBER_BATCH_FILE_ID_SEQ", allocationSize = 1)
	@Column(name = "BATCH_FILE_ID")
	private Long id;

	@Lob
	@Column(name = "SUCCESS_METADETA")
	private byte[] successFile;
	
	@Lob
	@Column(name = "RAW_METADETA")
	private byte[] rawFile;

	@Lob
	@Column(name = "FAILED_METADETA")
	private byte[] failedFile;
	
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
