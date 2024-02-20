package com.lic.epgs.gratuity.quotation.member.dto;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
/**
 * @author Ismail Khan A
 *
 */

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class MemberBankAccountDto {
	private Long id;
	private String bankAccountNumber;
	private Long accountTypeId;
	private String ifscCode;
	private Long bankNameId;
	private Long bankBranchId;
	private Boolean isActive;
	private String createdBy;
	private Date createdDate;
	private String modifiedBy;
	private Date modifiedDate;	
	private String bankName;
	private String bankBranch;
}
