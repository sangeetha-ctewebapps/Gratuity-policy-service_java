package com.lic.epgs.gratuity.policy.member.dto;

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
public class TempMemberBankAccountDto {
	
	private Long id;
	private String bankName;
	private String bankAccountNumber;
	private String accountType;
	private String bankBranch;
	private String bankAddress;
	private String countryCode;
	private String stdCode;
	private String ifscCodeAvailable;
	private String ifscCode;
	private String emailId;
	private String landlineNumber;
	private Boolean isActive;
	private String createdBy;
	private Date createdDate;
	private String modifiedBy;
	private Date modifiedDate;
	private Long pmstMemberBankAccountId;
	private Long recordType;
	

	
	private String bnkName;

	
	
}
