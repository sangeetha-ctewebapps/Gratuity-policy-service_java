package com.lic.epgs.gratuity.policyservices.aom.dto;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.lic.epgs.gratuity.quotation.member.dto.MemberAddressDto;
import com.lic.epgs.gratuity.quotation.member.dto.MemberAppointeeDto;
import com.lic.epgs.gratuity.quotation.member.dto.MemberBankAccountDto;
import com.lic.epgs.gratuity.quotation.member.dto.MemberNomineeDto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class MemberTmpDto {

	private Long id;

	private String proposalPolicyNumber;

	private Long policyId;

	private Long tmpPolicyId;

	private Long endorsementId;

	private Long pmstMemberId;

	private String licId;

	private String employeeCode;

	private String firstName;

	private String middleName;

	private String lastName;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy", timezone = "Asia/Kolkata")
	private Date dateOfBirth;

	private Long genderId;

	private String panNumber;

	private String aadharNumber;

	private Long landlineNo;

	private Long mobileNo;

	private String emailId;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy", timezone = "Asia/Kolkata")
	private Date dateOfAppointment;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy", timezone = "Asia/Kolkata")
	private Date dojToScheme;

	private Long categoryId;

	private String salaryFrequency;

	private String designation;

	private Double basicSalary;

	private String fatherName;

	private String spouseName;

	private Long statusId;

	private float lifeCoverPremium;

	private float lifeCoverSumAssured;

	private float accruedGratuity;

	private float totalGratuity;

	private Long gratCalculationId;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy", timezone = "Asia/Kolkata")
	private Date valuationDate;

	private String communicationPreference;

	private String languagePreference;

	private Boolean isActive;

	private String createdBy;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy", timezone = "Asia/Kolkata")
	private Date createdDate;

	private String modifiedBy;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy", timezone = "Asia/Kolkata")
	private Date modifiedDate;

	private String recordType;

	private List<MemberAppointeeDto> appointees;

	private List<MemberAddressDto> addresses;

	private List<MemberBankAccountDto> bankAccounts;

	private List<MemberNomineeDto> nominees;
}
