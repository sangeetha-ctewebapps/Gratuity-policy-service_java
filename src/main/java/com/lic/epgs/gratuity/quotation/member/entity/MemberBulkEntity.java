package com.lic.epgs.gratuity.quotation.member.entity;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

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
@Entity
@Table(name = "MEMBER_BULK")
public class MemberBulkEntity {
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "MEMBER_BULK_ID_SEQUENCE")
	@SequenceGenerator(name = "MEMBER_BULK_ID_SEQUENCE", sequenceName = "MEMBER_BULK_ID_SEQUENCE", allocationSize = 1)
	@Column(name = "MEMBER_BULK_ID", length = 10)
	private Long memberId;

	@Column(name = "QUOTATION_ID", length = 10)
	private Long quotationId;

	@Column(name = "PROPOSAL_NUMBER")
	private String proposalNumber;

	@Column(name = "BATCH_ID")
	private Long batchId;

	@Column(name = "FILE_ID")
	private String fileId;

	@Column(name = "MEMBER_SHIP_ID", length = 50)
	private String memberShipId;

	@Column(name = "LIC_ID", length = 255)
	private String licId;

	@Column(name = "MEMBER_STATUS", length = 20)
	private String memberStatus;

	@Column(name = "FIRSTNAME", length = 50)
	private String firstName;

	@Column(name = "MIDDLE_NAME", length = 50)
	private String middleName;

	@Column(name = "LAST_NAME", length = 50)
	private String lastName;

	@Column(name = "FATHER_NAME", length = 50)
	private String fatherName;

	@Column(name = "SPOUSE_NAME", length = 50)
	private String spouseName;

	@Column(name = "DATEOFBIRTH")
	private String dateOfBirth;

	@Column(name = "GENDER", length = 10)
	private String gender;

	@Column(name = "PAN", length = 10)
	private String pan;

	@Column(name = "AGE", length = 3)
	private Integer age;

	@Column(name = "AADHAR_NUMBER", length = 12)
	private Long aadharNumber;

	@Column(name = "DATEOFJOINING")
	private String dateOfJoining;

	@Column(name = "DATEOFJOINING_SCHEME")
	private String dateOfJoiningScheme;

	@Column(name = "DATEOF_RETIREMENT")
	private String dateOfRetirement;

	@Column(name = "RETIREMENT_AGE")
	private Integer retirementAge;

	@Column(name = "ANNUM_SALARY")
	private BigDecimal annumSalary;

	@Column(name = "DESIGNATION", length = 50)
	private String designation;

	@Column(name = "ROLE", length = 50)
	private String role;

	@Column(name = "MEMBERSHIP_NUMBER", length = 20)
	private String membershipNumber;

	@Column(name = "TYPEOF_MEMEBERSHIP", length = 20)
	private String typeOfMembershipNo;

	@Column(name = "PHONE", length = 10)
	private Long memberPhone;

	@Column(name = "EMAILID", length = 50)
	private String emailId;

	@Column(name = "LANGUAGE_PREFERENCE", length = 50)
	private String languagePreference;

	@Column(name = "COMMUNICATION_PREFERENCE", length = 50)
	private String communicationPreference;

	@Column(name = "CATEGORY", length = 10)
	private Integer category;

	@Column(name = "EMPLOYER_CONTRIBUTION", precision = 20, scale = 8)
	private BigDecimal employerContribution;

	@Column(name = "EMPLOYEE_CONTRIBUTION", precision = 20, scale = 8)
	private BigDecimal employeeContribution;

	@Column(name = "VOLUNTARY_CONTRIBUTION", precision = 20, scale = 8)
	private BigDecimal voluntaryContribution;

	@Column(name = "TOTAL_CONTRIBUTION", precision = 20, scale = 8)
	private BigDecimal totalContribution;

	@Column(name = "ANNUITY_OPTION", length = 50)
	private String annuityOption;

	@Column(name = "TOTAL_INTERESTED_ACCURED", precision = 20, scale = 8)
	private BigDecimal totalInterestedAccured;

	@Column(name = "FREQUENCY", length = 10)
	private Integer frequency;

	@Column(name = "CREATED_BY", length = 10)
	private Integer createdBy;

	@Column(name = "CREATED_ON")
	private Date createdOn = new Date();

	@Column(name = "MODIFIED_BY", length = 10)
	private Integer modifiedBy;

	@Column(name = "MODIFIED_ON")
	private Date modifiedOn = new Date();

	@Column(name = "MBR_ADDRESS_1")
	private String addressLineOne;

	@Column(name = "PINCODE")
	private Integer pinCode;

	@Column(name = "BANK_NAME")
	private String bankName;

	@Column(name = "ACCOUNT_NUMBER")
	private String accountNumber;

	@Column(name = "IFSC_CODE")
	private String ifscCode;

	@Column(name = "NOMINEE_NAME")
	private String nomineeName;

	@Column(name = "NOMINEE_ADDRESS_1")
	private String nomineeddressOne;

	@Column(name = "NOMINEE_PHONE", length = 10)
	private Long nomineePhone;

	@Column(name = "NOMINEE_AGE")
	private Integer nomineeAge;

	@Column(name = "NOMINEE_DOB")
	private String nomineeDob;

	@Column(name = "NOMINEE_RELATIONSHIP")
	private String relationShip;

	@Column(name = "APPOINTEE_NAME", length = 50)
	private String appointeeName;

	@Column(name = "APPOINTEE_ADDRESS_1")
	private String appointeeAddress;

	@Column(name = "APPOINTEE_CONTACT")
	private Long appointeeContactNumber1;

	@Column(name = "APPOINTEE_AGE")
	private Integer appointeeAge;

	@Column(name = "APPOINTEE_DOB")
	private String appointeeDob;

	@Column(name = "APPOINTEE_RELATIONSHIP")
	private String apointeeRelationship;

	@Column(name = "IS_ACTIVE")
	private Boolean isActive;

}
