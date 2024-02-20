package com.lic.epgs.gratuity.policyservices.common.entity;

import java.io.Serializable;
import java.util.Date;

import javax.validation.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data

@JsonIgnoreProperties(ignoreUnknown = true)
public class PolicyServiceDocumentDto implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long documentId;
	private Long policyId;
	private Long serviceModuleId;
	private Long serviceId;
	private String serviceNumber;
	private String requirement;
	private String status;
	private String comments;
	private String documentIndex;
	private Integer folderIndex;
	private String createdBy;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy", timezone = "asia/kolkata")
	private Date createdOn = new Date();
	private String modifiedBy;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy", timezone = "asia/kolkata")
	private Date modifiedOn = new Date();
	private Boolean isActive;

	private Long conversionId;
	private Long mergeId;
	private Long memberAdditionId;
	private Long policyDtlsId;
	private Long freeLookId;
	private Long trnsfrId;

	@JsonProperty(required = true)
	@NotBlank(message = "User Role is mandatory i.e., MAKER/CHECKER")
	private String role;

	@JsonProperty(required = true)
	@NotBlank(message = "Service Type is mandatory i.e., POLICY/POLICYCONVERSION/POLICYMERGER/FREELOOKCANCEL/POLICYDETAILSCHANGE/MEMBEROFADDITION/MEMBERTRASFERINOUT/")
	private String serviceType;

	@JsonProperty(required = true)
	@NotBlank(message = "Page is mandatory i.e., INPROGRESS/EXISTING")
	private String page;

	@JsonProperty(required = true)
	@NotBlank(message = "Login user is required i.e., MAKER/CHECKER username")
	private String loginUser;

	@JsonProperty(required = true)
	@NotBlank(message = "UnitId is mandatory i.e., G605")
	private String unitId;
}
