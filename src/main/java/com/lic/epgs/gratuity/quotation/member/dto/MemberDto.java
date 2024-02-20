package com.lic.epgs.gratuity.quotation.member.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.Column;

import org.modelmapper.ModelMapper;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.lic.epgs.gratuity.quotation.member.entity.MemberAddressEntity;
import com.lic.epgs.gratuity.quotation.member.entity.MemberAppointeeEntity;
import com.lic.epgs.gratuity.quotation.member.entity.MemberBankAccount;
import com.lic.epgs.gratuity.quotation.member.entity.MemberEntity;
import com.lic.epgs.gratuity.quotation.member.entity.MemberNomineeEntity;
import com.lic.epgs.gratuity.quotation.premium.dto.GratuityCalculationDto;

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
public class MemberDto implements Serializable {
	
	private static final long serialVersionUID = 1L;

	private Long id;
	private Long quotationId;
	private Long memberBatchId;
	private String proposalPolicyNumber;
	private String licId;
	private String employeeCode;
    private String firstName;
    private String middleName;
    private String lastName;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy", timezone = "IST")
	private Date dateOfBirth;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy", timezone = "IST")
	private Date dateOfAppointment;
	private Date dojToScheme;
	private Long categoryId;
	private String salaryFrequency;
	private Double basicSalary;
	private String designation;
	private Long genderId;
	private String panNumber;
	private String aadharNumber;
	private Long landlineNo;
    private Long mobileNo;
	private String emailId;
	private String fatherName;
	private String spouseName; 
	private Long gratCalculationId;
	private Float lifeCoverPremium;
	private Float lifeCoverSumAssured;
	private Float totalGratuity;
	private Float accruedGratuity;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy", timezone = "asia/kolkata")	
	private Date valuationDate;
	private String communicationPreference;
	private String languagePreference;
	private Long statusId;
	  
	private Boolean isActive;
	private String createdBy;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy", timezone = "asia/kolkata")
	private Date createdDate;
	private String modifiedBy;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy", timezone = "asia/kolkata")
	private Date modifiedDate;
	private Boolean isMemberCodeUnique;
	
	private String bankName;

	private String bankBranch;
	
	
	private List<MemberAddressDto> addresses;
	private List<MemberAppointeeDto> appointees;
	private List<MemberBankAccountDto> bankAccounts;
	private List<MemberNomineeDto> nominees;
	private GratuityCalculationDto gratuityCalculationDto;
	private Boolean isExcludedAfterRein;
	
}
