package com.lic.epgs.gratuity.quotation.member.dto;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
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
public class MemberNomineeDto {
	private Long id;
	private String code;
	private String name;
	private Long relationshipId;
	private String contactNumber;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy", timezone = "asia/kolkata")
	private Date dateOfBirth;
	private String panNumber;
	private String aadharNumber;
	private String bankAccountNumber;
	private Long bankAccountTypeId;
	private String ifscCode;
	private Long bankNameId;
	private Long bankBranchId;
	private double percentage;
	private Boolean isActive;
	private String createdBy;
	private Date createdDate;
	private String modifiedBy;
	private Date modifiedDate;

	private String bankName;

	private String bankBranch;
	
	private long genderId;
	
}
