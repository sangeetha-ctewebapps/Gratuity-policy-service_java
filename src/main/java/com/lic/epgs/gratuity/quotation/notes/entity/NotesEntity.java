package com.lic.epgs.gratuity.quotation.notes.entity;

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
@Table(name = "QSTG_NOTES")
public class NotesEntity {
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "QSTG_NOTES_ID_SEQ")
	@SequenceGenerator(name = "QSTG_NOTES_ID_SEQ", sequenceName = "QSTG_NOTES_ID_SEQ", allocationSize = 1)
	@Column(name = "NOTES_ID")
	private Long id;
	
	@Column(name = "QUOTATION_ID")
	private Long quotationId;
	
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
}
