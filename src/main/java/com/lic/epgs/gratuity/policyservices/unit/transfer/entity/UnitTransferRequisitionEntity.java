package com.lic.epgs.gratuity.policyservices.unit.transfer.entity;

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
@Table(name = "UNIT_TRANSFER_REQUISITION")
public class UnitTransferRequisitionEntity implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "UNIT_TRANSFER_REQUISITION_ID_SEQUENCE")
	@SequenceGenerator(name = "UNIT_TRANSFER_REQUISITION_ID_SEQUENCE", sequenceName = "UNIT_TRANSFER_REQUISITION_ID_SEQUENCE", allocationSize = 1)
	@Column(name = "UNIT_TRANSFER_REQUISITION_ID", unique = true, nullable = false, updatable = false, length = 10)
	private Long unitTransferRequisitionId;

	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "UNIT_TRANSFER_REQUEST_NUMBER_SEQ")
	@SequenceGenerator(name = "UNIT_TRANSFER_REQUEST_NUMBER_SEQ", initialValue = 10000, allocationSize = 1, sequenceName = "UNIT_TRANSFER_REQUEST_NUMBER_SEQ")
	@Column(name = "UNIT_TRANSFER__REQUEST_NUMBER")
	private Long unitTransferRequestNumber;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy", timezone = "IST")
	@Column(name = "UNIT_TRANSFER_REQUEST_DATE")
	private Date unitTransferRequestDate;

	@Column(name = "UNIT_TRANSFER_STATUS")
	private String unitTransferStatus;

	@Column(name = "UNIT_TRANSFER_SUB_STATUS")
	private String unitTransferSubStatus;

	@Column(name = "LIC_ID")
	private Long licId;

	@Column(name = "ROLE")
	private String role;

	@Column(name = "LOCATION_TYPE")
	private String locationType;

	@Column(name = "SENT_TO")
	private String sentTo;

	@Column(name = "REMARKS")
	private String remarks;

	@Column(name = "EFFECTIVE_START_DATE")
	private Date effectiveStartDate = new Date();

	@Column(name = "EFFECTIVE_END_DATE")
	private Date effectiveEndDate = new Date();

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
