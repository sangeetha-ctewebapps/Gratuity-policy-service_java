package com.lic.epgs.gratuity.quotation.member.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author Ismail Khan A
 *
 */

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
@Entity
@Table(name = "QSTG_MEMBER_BATCH_ERROR")
public class MemberBulkErrorEntity implements Serializable {

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "QSTG_MEMBER_BATCH_ERR_ID_SEQ")
	@SequenceGenerator(name = "QSTG_MEMBER_BATCH_ERR_ID_SEQ", sequenceName = "QSTG_MEMBER_BATCH_ERR_ID_SEQ", allocationSize = 1)
	@Column(name = "MEMBER_BATCH_ERROR_ID")
	private Long id;

	@Column(name = "MEMBER_BULK_STG_ID")
	private Long stagingId;

	@Column(name = "MEMBER_BATCH_ID")
	private Long batchId;
	
	@Column(name = "ERROR")
	private String error;

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
