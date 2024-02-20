package com.lic.epgs.gratuity.policy.renewalpolicy.member.dto;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.lic.epgs.gratuity.policy.member.dto.PolicyMemberNomineeDto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class RenewalPolicyTMPMemberAppointeeDto {
	
	private Long id;
	private String code;
	private String name;
	private String relationship;
	private String contactNumber;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy", timezone = "asia/kolkata")
	private Date dateOfBirth;
	private String panNumber;
	private String aadharNumber;
	private String bankAccountNumber;
	private String accountType;
	private String ifscCode;
	private String bankName;
	private double percentage;
	private Boolean isActive;
	private String createdBy;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy", timezone = "asia/kolkata")
	private Date createdDate;
	private String modifiedBy;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy", timezone = "asia/kolkata")
	private Date modifiedDate;
	private Long pmstMemberAppointeeId;
	private String recordType;
	
	
	private RenewalPolicyTMPMemberNomineeDto nominee;

}
