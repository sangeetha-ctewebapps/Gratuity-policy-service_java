package com.lic.epgs.gratuity.policy.renewalpolicy.member.dto;

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
public class RenewalPolicyTMPMemberNomineeDto {
	private Long id;
	private String nomineeName;
	private String relationship;
	private String addressOne;
	private String addressTwo;
	private String addressThree;
	private String nomineeDistrict;
	private String nomineeState;
	private String nomineeCountry;
	private Long nomineePincode;
	private String nomineeEmailId;
	private Long nomineePhone;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy", timezone = "asia/kolkata")
	private Date dateOfBirth;
	private Long age;
	private String nomineeAadharNumber;
	private Double percentage;
	private Boolean isActive;
	private String createdBy;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy", timezone = "asia/kolkata")
	private Date createdDate;
	private String modifiedBy;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy", timezone = "asia/kolkata")
	private Date modifiedDate;
	private Long pmstMemberNomineeId;
	private String recordType;

	private String bankAccNumber;
	private String accountType;
	private String ifcsCode;
	private String bankName;
	private String bankBranch;
	private Long genderId;
	private String panNumber;
}
