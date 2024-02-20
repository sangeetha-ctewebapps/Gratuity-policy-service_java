package com.lic.epgs.gratuity.policy.renewalpolicy.member.dto;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.lic.epgs.gratuity.quotation.member.dto.MemberAddressDto;
import com.lic.epgs.gratuity.quotation.member.dto.MemberAppointeeDto;
import com.lic.epgs.gratuity.quotation.member.dto.MemberBankAccountDto;
import com.lic.epgs.gratuity.quotation.member.dto.MemberNomineeDto;
import com.lic.epgs.gratuity.quotation.premium.dto.GratuityCalculationDto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class RenewalPolicyTMPMemberDto {

    private Long id;
    private Long policyId;
	private Long tmpPolicyId;
    private Long endorsementId;
    private Long pmstMemebrId;
    private String licId;
    private String employeeCode;
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
    private Date dateOfAppointment;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy", timezone = "IST")
    private Date dojToScheme;
    private Long categoryId;
    private String salaryFrequency;
    private String designation;
    private Double basicSalary;
    private String fatherName;
    private String spouseName;
    private Long gratCalculationId;
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
    private String recordType;
    private String proposalPolicyNumber;
   	private Long hasExceededFCL;
	private Long hasGroupExceededFCL;
	private Long policyServiceId;
    
	private List<RenewalPolicyTMPMemberAddressDto> addresses;
	private List<RenewalPolicyTMPMemberAppointeeDto> appointees;
	private List<RenewalPolicyTMPMemberBankAccountDto> bankAccounts;
	private List<RenewalPolicyTMPMemberNomineeDto> nominees;
	private GratuityCalculationDto gratuityCalculationDto;
	private String renewalUpdateType;
	private Date lastPremiumCollectedDate;
	private String  newLife;
    private String gstInvoiceNumber;	 
    private Date gstInvoiceDate;
}
