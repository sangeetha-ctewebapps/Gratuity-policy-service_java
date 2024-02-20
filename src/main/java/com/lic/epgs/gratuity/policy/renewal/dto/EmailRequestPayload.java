package com.lic.epgs.gratuity.policy.renewal.dto;

import java.io.File;
import java.util.List;

public class EmailRequestPayload {
	
	private String to;
	private String cc;
	private String subject;
	private String emailBody;
	private File file;
	private List<EmailPdfFileDto> pdfFile;
	private List<EmailPdfTwoFileDto> twoPdfFile;

	public String getTo() {
		return to;
	}

	public void setTo(String to) {
		this.to = to;
	}

	public File getfile() {
		return file;
	}

	public void setfile(File file) {
		this.file = file;
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

	
	public List<EmailPdfFileDto> getPdfFile() {
		return pdfFile;
	}

	public void setPdfFile(List<EmailPdfFileDto> pdfFile) {
		this.pdfFile = pdfFile;
	}
	
	public EmailRequestPayload() {
		super();
	}

	public String getCc() {
		return cc;
	}

	public void setCc(String cc) {
		this.cc = cc;
	}

}


