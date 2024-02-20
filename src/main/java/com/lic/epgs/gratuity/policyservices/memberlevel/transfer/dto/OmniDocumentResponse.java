package com.lic.epgs.gratuity.policyservices.memberlevel.transfer.dto;

public class OmniDocumentResponse {
	
	String status;
	String message;
	String description;
	String documentIndexNo;
	String folderIndexNo;
	
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getDocumentIndexNo() {
		return documentIndexNo;
	}
	public void setDocumentIndexNo(String documentIndexNo) {
		this.documentIndexNo = documentIndexNo;
	}
	public String getFolderIndexNo() {
		return folderIndexNo;
	}
	public void setFolderIndexNo(String folderIndexNo) {
		this.folderIndexNo = folderIndexNo;
	}
	
	

}
