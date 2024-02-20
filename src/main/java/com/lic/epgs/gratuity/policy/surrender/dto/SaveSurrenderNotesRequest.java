package com.lic.epgs.gratuity.policy.surrender.dto;

import java.util.Date;

public class SaveSurrenderNotesRequest {

	private Long surrenderId;
	private String surrenderNote;
	private Long userId;
	private String modifiedBy;
	private Date modifiedOn;
	
	public Long getSurrenderId() {
		return surrenderId;
	}
	public void setSurrenderId(Long surrenderId) {
		this.surrenderId = surrenderId;
	}
	
	public String getSurrenderNote() {
		return surrenderNote;
	}
	public void setSurrenderNote(String surrenderNote) {
		this.surrenderNote = surrenderNote;
	}
	
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
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
