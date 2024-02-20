package com.lic.epgs.gratuity.policyservices.aom.model;

import java.util.List;
import java.util.Map;

public class EmailRequest {

	private String to;
	private String cc;
	private String subject;
	private String emailBody;
	private List<Map<String,Object>> pdfFile;
	private String bcc;
	
	public String getBcc() {
		return bcc;
	}
	public void setBcc(String bcc) {
		this.bcc = bcc;
	}
	public String getTo() {
		return to;
	}
	public void setTo(String to) {
		this.to = to;
	}
	public String getCc() {
		return cc;
	}
	public void setCc(String cc) {
		this.cc = cc;
	}
	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}
	public String getEmailBody() {
		return emailBody;
	}
	public void setEmailBody(String emailBody) {
		this.emailBody = emailBody;
	}
	public List<Map<String, Object>> getPdfFile() {
		return pdfFile;
	}
	public void setPdfFile(List<Map<String, Object>> pdfFile) {
		this.pdfFile = pdfFile;
	}
}
