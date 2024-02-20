package com.lic.epgs.gratuity.policyservices.aom.dto;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class DocumentUploadDto {

	private Long id;
	private Long masterPolicyId;
	private Long documentTypeId;
	private Long statudId;
	private Long documentIndexNumber;
	private long folderIndexNumber;
	private Long tmpPolicyId;
	private String moduleType;
	private String username;
	private String fileBase64;
	private String createdBy;
	@JsonFormat(shape=JsonFormat.Shape.STRING, pattern="dd-MM-yyyy", timezone="IST")
	private Date createdDate;
	private String modifiedBy;
	@JsonFormat(shape=JsonFormat.Shape.STRING, pattern="dd-MM-yyyy", timezone="IST")
	private Date modifiedDate;
}
