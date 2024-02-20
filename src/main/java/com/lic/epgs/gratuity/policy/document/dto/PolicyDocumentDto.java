package com.lic.epgs.gratuity.policy.document.dto;

import java.util.Date;

import javax.persistence.Column;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class PolicyDocumentDto {
	
	private Long id;
	private Long policyId;
	private Long documentTypeId;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy", timezone = "asia/kolkata")
	private Date expiryDate;
	private Long documentSubTypeId;
	private Long issuedById;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy", timezone = "asia/kolkata")
	private Date issuedDate;
	private String documentName;
	private String documentIndex;

	private Long documentIndexNo;
	

	private Long folderIndexNo;
	private Long documentStatus;
	private Boolean isDeleted;
	private Boolean isActive;
	private String createdBy;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy", timezone = "asia/kolkata")
	private Date createdDate;
	private String modifiedBy;
	private Date modifiedDate;
}
