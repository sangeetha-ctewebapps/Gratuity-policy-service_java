package com.lic.epgs.gratuity.policyservices.memberlevel.transfer.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author Nandini.R Date:12-08-2023
 *
 */

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "TRANSFER_REQUISITION")
public class TransferRequisitionEntity implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "TRANSFER_REQUISITION_ID_SEQUENCE")
	@SequenceGenerator(name = "TRANSFER_REQUISITION_ID_SEQUENCE", sequenceName = "TRANSFER_REQUISITION_ID_SEQUENCE", allocationSize = 1)
	@Column(name = "TRANSFER_REQUISITION_ID", unique = true, nullable = false, updatable = false, length = 10)
	private Long transferRequisitionId;


	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "TRANSFER_REQUEST_NUMBER_SEQ")
	@SequenceGenerator(name = "TRANSFER_REQUEST_NUMBER_SEQ", initialValue = 10000, allocationSize = 1, sequenceName = "TRANSFER_REQUEST_NUMBER_SEQ")
	@Column(name = "TRANSFER_REQUEST_NUMBER" )
	private Long transferRequestNumber;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy", timezone = "IST")
	@Column(name = "TRANSFER_REQUEST_DATE")
	private Date transferRequestDate;

	@Column(name = "TRANSFER_STATUS")
	private String transferStatus;

	@Column(name = "TRANSFER_SUB_STATUS")
	private String transferSubStatus;

	@Column(name = "LIC_ID")
	private Long licId;

	@Column(name = "ROLE")
	private String role;

	@Column(name = "LOCATION_TYPE")
	private String locationType;

	@Column(name = "SENT_TO")
	private String sentTo;
	
	@Column(name = "APPROVED_BY")
	private String approvedBy;
	
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy", timezone = "IST")
	@Column(name = "APPROVED_ON")
	private Date approvedOn;

	@Column(name = "REMARKS")
	private String remarks;

	@Column(name = "EFFECTIVE_START_DATE")
	private Date effectiveStartDate = new Date();

	@Column(name = "EFFECTIVE_END_DATE")
	private Date effectiveEndDate = new Date();
		
	@Column(name = "CONTRIBUTION_ADJUSTMENT_ID")
	private Long contributionAdjustmentId;
		
	@Column(name = "BATCH_ID")
	private Long batchId;
	
	@Column(name = "IS_BULK")
	private String isBulk;

	@Column(name = "CREATED_BY")
	private String createdBy;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy", timezone = "IST")
	@Column(name = "CREATED_ON")
	private Date createdOn;

	@Column(name = "MODIFIED_BY")
	private String modifiedBy;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy", timezone = "IST")
	@Column(name = "MODIFIED_ON")
	private Date modifiedOn;

}
