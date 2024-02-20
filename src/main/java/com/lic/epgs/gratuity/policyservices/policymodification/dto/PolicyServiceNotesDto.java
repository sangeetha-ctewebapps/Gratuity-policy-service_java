package com.lic.epgs.gratuity.policyservices.policymodification.dto;

import java.io.Serializable;
import java.util.Date;

import javax.validation.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class PolicyServiceNotesDto implements Serializable {
	private static final long serialVersionUID = 1L;

	private Long serviceNoteId;

	private Long policyId;

	private Long serviceModuleId;

	private String noteContents;

	private String createdBy;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy", timezone = "asia/kolkata")
	private Date createdOn;

	private Long serviceId;

	private Boolean isActive;

	private Long conversionId;

	private Long mergeId;

	private Long memberAdditionId;

	private Long policyDtlsId;

	private Long freeLookId;

	private Long transferId;

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
	
	private Long memberDtlsId;
}
