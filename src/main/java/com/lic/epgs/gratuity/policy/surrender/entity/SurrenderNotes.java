package com.lic.epgs.gratuity.policy.surrender.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;


@Entity
@Table(name = "SURR_NOTES")
public class SurrenderNotes {
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE,generator="SURR_NOTES_SEQ")
	@SequenceGenerator(name="SURR_NOTES_SEQ", sequenceName="SURR_NOTES_SEQ", allocationSize=1) 
	@Column(name = "SURR_NOTES_ID")
	private Long surrenderNoteId;
	
	@Column(name = "NOTES_TEXT") 
	 private String notesText;
	
	@Column(name = "USER_ID") 
	 private Long userId;
	
	@Column(name = "SURRENDER_ID") 
	 private Long surrenderId;
	
	@Column(name = "MODIFIED_BY") 
	 private String modifiedBy;
	
	@Column(name = "MODIFIED_ON") 
	 private Date modifiedOn;
	
	public Long getSurrenderNoteId() {
		return surrenderNoteId;
	}

	public void setSurrenderNoteId(Long surrenderNoteId) {
		this.surrenderNoteId = surrenderNoteId;
	}

	public String getNotesText() {
		return notesText;
	}

	public void setNotesText(String notesText) {
		this.notesText = notesText;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Long getSurrenderId() {
		return surrenderId;
	}

	public void setSurrenderId(Long surrenderId) {
		this.surrenderId = surrenderId;
	}

	public String getModifiedBy() {
		return modifiedBy;
	}

	public void setModifiedBy(String modifiedBy) {
		this.modifiedBy = modifiedBy;
	}

	public Date getModifiedOn() {
		return modifiedOn;
	}

	public void setModifiedOn(Date modifiedOn) {
		this.modifiedOn = modifiedOn;
	}

	
}
