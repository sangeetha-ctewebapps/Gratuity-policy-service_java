package com.lic.epgs.gratuity.policy.endorsement.dto;

import java.util.Date;

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
public class EndorsementDto {

	private Long id;
	private String endorsementNumber;
	private Long policyId;
	private Long endorsementStatusId;
	private Long endorsementSubStatusId;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy", timezone = "asia/kolkata")
	private Date requestDate;
	private String requestReceivedFrom;
	private Long endorsementTypeId;
	private Long endorsementSubtypeId;
	private Long serviceTypeId;
	private Long effectiveTypeId;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy", timezone = "asia/kolkata")
	private Date effectiveDate;
	private String serviceNumber;
	private Long communicationOptionId;
	private Boolean isActive;
	private String createdBy;
	
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy", timezone = "asia/kolkata")
	private Date createdDate;
	
	private String modifiedBy;
	
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy", timezone = "asia/kolkata")
	private Date modifiedDate;
	
	private Boolean isInitiated;
}
