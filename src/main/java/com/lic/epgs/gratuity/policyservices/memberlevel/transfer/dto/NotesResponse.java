package com.lic.epgs.gratuity.policyservices.memberlevel.transfer.dto;

import java.util.List;

import com.lic.epgs.gratuity.policyservices.memberlevel.transfer.entity.TransferNotesEntity;

public class NotesResponse {
	
	private String message;
	private String transctionType;	
	private List<TransferNotesEntity> noteList;
		
	public  NotesResponse(String message, String transctionType, List<TransferNotesEntity> noteList) {
		super();
		this.message = message;
		this.transctionType = transctionType;		
		this.noteList = noteList;
	}

	public String getMessage() {
		return message;
	}


	public void setMessage(String message) {
		this.message = message;
	}


	public String getTransctionType() {
		return transctionType;
	}


	public void setTransctionType(String transctionType) {
		this.transctionType = transctionType;
	}

	public List<TransferNotesEntity> getNoteList() {
		return noteList;
	}

	public void setNoteList(List<TransferNotesEntity> noteList) {
		this.noteList = noteList;
	}	

}
