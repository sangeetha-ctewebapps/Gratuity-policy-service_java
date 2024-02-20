package com.lic.epgs.gratuity.policy.member.dto;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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
public class PolicyMemberDto implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long id;
	private Long policyId;
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
	private Date dateOfJoining;
	private Long categoryId;
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
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy", timezone = "IST")
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

	private String salaryFrequency;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy", timezone = "IST")
	  private Date dateOfAppointment;
	
	private List<PolicyMemberAddressDto> addresses;
	private List<PolicyMemberAppointeeDto> appointees;
	private List<PolicyMemberBankAccountDto> bankAccounts;
	private List<PolicyMemberNomineeDto> nominees;
	// private GratuityCalculationDto gratuityCalculationDto;
	 private String renewalUpdateType;
	 private Date lastPremiumCollectedDate;
	 private String  newLife;
	 private String gstInvoiceNumber;
	 private Date gstInvoiceDate;
	
	
}
