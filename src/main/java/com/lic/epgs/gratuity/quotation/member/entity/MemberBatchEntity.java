package com.lic.epgs.gratuity.quotation.member.entity;

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

/**
 * @author Ismail Khan A
 *
 */

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "QSTG_MEMBER_BATCH")
public class MemberBatchEntity {
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "QSTG_MEMBER_BATCH_ID_SEQ")
	@SequenceGenerator(name = "QSTG_MEMBER_BATCH_ID_SEQ", sequenceName = "QSTG_MEMBER_BATCH_ID_SEQ", allocationSize = 1)
	@Column(name = "BATCH_ID")
	private Long batchId;
	
	@Column(name = "BATCH_FILE_ID")
	private Long batchFileId;
	
	@Column(name = "QUOTATION_ID")
	private Long quotationId;

	@Column(name = "SUCCESS_COUNT")
	private Long successCount = 0L;

	@Column(name = "FAILED_COUNT")
	private Long failedCount = 0L;

	@Column(name = "TOTAL_COUNT")
	private Long totalCount = 0L;

	@Column(name = "FILE_NAME")
	private String fileName;
	
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
	
	@Column(name = "PMST_POLICY_ID")
	private Long pmstPolicyId;
	
	@Column(name = "TMP_POLICY_ID")
	private Long tmpPolicyId;
	
}
