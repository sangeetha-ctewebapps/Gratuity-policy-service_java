package com.lic.epgs.gratuity.policyservices.unit.transfer.dto;

import java.util.List;

import com.lic.epgs.gratuity.policyservices.memberlevel.transfer.entity.TransferNotesEntity;
import com.lic.epgs.gratuity.policyservices.unit.transfer.entity.UnitTransferNotesEntity;

public class UnitNotesResponse {
	
	private String message;
	private String transctionType;	
	private List<UnitTransferNotesEntity> noteList;
		
	public  UnitNotesResponse(String message, String transctionType, List<UnitTransferNotesEntity> noteList) {
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

	public List<UnitTransferNotesEntity> getNoteList() {
		return noteList;
	}

	public void setNoteList(List<UnitTransferNotesEntity> noteList) {
		this.noteList = noteList;
	}	

}
