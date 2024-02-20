package com.lic.epgs.gratuity.policyservices.common.dto;

import java.io.Serializable;
import java.util.Date;

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
public class CommonDocsDto implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long quotationId;
	private Long documentTypeId;
	private Long documentSubTypeId;
	private Date expiryDate;
	private Long issuedById;
	private Date issuedDate;
	private String fileDropRef;
	private String documentName;
	private String documentIndex;
	private Long documentIndexNo;
	private Long folderIndexNo;
	private Long documentStatus;
	private String policyNumber;
	private String serviceNumber;
	private Long referenceId;
	private String referenceServiceType;
	private Boolean isDeleted;
	private String createdBy;
	private String type;

}
