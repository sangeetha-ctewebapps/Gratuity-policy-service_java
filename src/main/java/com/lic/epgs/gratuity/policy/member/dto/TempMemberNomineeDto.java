package com.lic.epgs.gratuity.policy.member.dto;

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
public class TempMemberNomineeDto {

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
	private long age;
	private String nomineeAadharNumber;
	private double percentage;
	private Boolean isActive;
	private String createdBy;
	private Date createdDate;
	private String modifiedBy;
	private Date modifiedDate;
	private Long pmstMemberNomineeId;
	private Long recordType;
	
	private String bankName;

	private String bankBranch;

	private String bankAccountNumber;

	private String accountType;

	private String ifscCode;
	
	private String panNumber;
	
	private Long genderId;
}
