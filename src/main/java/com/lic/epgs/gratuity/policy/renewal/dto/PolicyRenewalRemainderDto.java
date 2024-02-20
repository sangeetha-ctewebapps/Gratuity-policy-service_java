package com.lic.epgs.gratuity.policy.renewal.dto;

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
public class PolicyRenewalRemainderDto {
	private Long id;
	private Date annualrenewaldate;
	private String createdBy;
	private Date createdDate;
	private String emailResponse;
	private String emailSubject;
	private String emailTo;
	private Boolean isActive;
	private String modifiedBy;
	private Date modifiedDate;
	private Date remindedDateTime;
	private Long policyId;
}
