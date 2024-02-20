package com.lic.epgs.gratuity.policy.member.dto;

import java.util.Date;
import java.util.List;

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
public class TempMemberDto {


	private Long id;
	private Long policyId;
    private Long endorsementId;
    private Long pmstMemberId;
	private String licId;
	private String code;
	private String firstName;
	private String middleName;
	private String lastName;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy", timezone = "IST")
	private Date dateOfBirth;
	private Long genderId;
	private String panNumber;
	private String aadharNumber;
	private Long landlineNo;
	private Long mobileNo;
	private String emailId;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy", timezone = "IST")
	private Date dateOfJoining;
	private Long categoryId;
	private String designation;
	private Double basicSalary;
	private String fatherName;
	private String spouseName;
	private Long gratCalculationId;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy", timezone = "IST")
	private Date dateOfCommencement;
	
	private Long termTypeId;
	private Long fclTypeId;
    private Long statusId;
    private float lifeCoverPremium;
    private float lifeCoverSumAssured;
    private float accruedGratuity;
    private float totalGratuity;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy", timezone = "asia/kolkata")
    private Date valuationDate;
    private String communicationPreference;
    private String languagePreference;
	private Boolean isActive;
	private String createdBy;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy", timezone = "asia/kolkata")
	private Date createdDate;
	private String modifiedBy;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy", timezone = "asia/kolkata")
	private Date modifiedDate;
	 private Long recordType;
	 private String  newLife;
	 
		private List<TempMemberAddressDto> addresses;
		private List<TempMemberAppointeeDto> appointees;
		private List<TempMemberBankAccountDto> bankAccounts;
		private List<TempMemberNomineeDto> nominees;
}
