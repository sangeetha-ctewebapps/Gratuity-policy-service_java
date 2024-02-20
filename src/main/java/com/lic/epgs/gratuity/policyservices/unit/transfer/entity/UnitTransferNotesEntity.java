package com.lic.epgs.gratuity.policyservices.unit.transfer.entity;

import java.io.Serializable;
import java.util.Date;

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
@Table(name = "UNIT_TRANSFER_NOTES")
public class UnitTransferNotesEntity implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "UNIT_TRANSFER_NOTES_ID_SEQUENCE")
	@SequenceGenerator(name = "UNIT_TRANSFER_NOTES_ID_SEQUENCE", sequenceName = "UNIT_TRANSFER_NOTES_ID_SEQUENCE", allocationSize = 1)
	@Column(name = "UNIT_TRANSFER_NOTES_ID", unique = true, nullable = false, updatable = false, length = 10)
	private Long unitTransferNotesId;

	@Column(name = "UNIT_TRANSFER_REQUISITION_ID")
	private Long unitTransferRequisitionId;

	@Column(name = "UNIT_NOTE")
	private String unitNote;

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
