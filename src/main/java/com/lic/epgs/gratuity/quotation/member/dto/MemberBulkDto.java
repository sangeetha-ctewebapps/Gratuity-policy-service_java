package com.lic.epgs.gratuity.quotation.member.dto;

import java.io.Serializable;
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
public class MemberBulkDto implements Serializable {
	
	private static final long serialVersionUID = 1L;

	private Long memberBulkStgId;
	private Long memberBatchId;
	private String proposalPolicyNumber;
	private String licId;
	private String employeeCode;
	private String firstName;
    private String middleName;
    private String lastName;
	private String name;
	private Date dateOfBirth;
	private Date dateOfAppointment;
	private Date dojToScheme;
	private Long categoryId;
	private String category;
	private String salaryFrequency;
	private double basicSalary;
	private Date dateOfCommencement;
	private String gender;
	private String panNumber;
	private String aadharNumber;
	private String contactNumber;
	private String emailId;
	private Long termTypeId;
	private String termType;
	private String loanNumber;
	private Long borrowerTypeId;
	private String borrowerType;
	private Long typeOfLifeAssuredId;
	private String typeOfLifeAssured;
	private String borrowerSequenceNumber;
	private Long moratoriumPeriodId;
	private String moratoriumPeriod;
	private Long fclTypeId;
	private String fclType;
	private Long address1AddressTypeId;
	private String address1AddressType;
	private String address1PinCode;
	private Long address1CountryId;
	private String address1Country;
	private Long address1StateId;
	private String address1State;
	private String address1District;
	private String address1City;
	private String address1ContactNumber;
	private String address1Address1;
	private String address1Address2;
	private String address1Address3;
	private Boolean address1Printable;
	private Long address2AddressTypeId;
	private String address2AddressType;
	private String address2PinCode;
	private Long address2CountryId;
	private String address2Country;
	private Long address2StateId;
	private String address2State;
	private String address2District;
	private String address2City;
	private String address2ContactNumber;
	private String address2Address1;
	private String address2Address2;
	private String address2Address3;
	private Boolean address2Printable;
	private Long address3AddressTypeId;
	private String address3AddressType;
	private String address3PinCode;
	private Long address3CountryId;
	private String address3Country;
	private Long address3StateId;
	private String address3State;
	private String address3District;
	private String address3City;
	private String address3ContactNumber;
	private String address3Address1;
	private String address3Address2;
	private String address3Address3;
	private Boolean address3Printable;
	private String bank1BankAccountNumber;
	private Long bank1AccountTypeId;
	private String bank1AccountType;
	private String bank1IfcsCode;
	private Long bank1BankNameId;
	private String bank1BankName;
	private Long bank1BankBranchId;
	private String bank1BankBranch;
	private String bank2BankAccountNumber;
	private Long bank2AccountTypeId;
	private String bank2AccountType;
	private String bank2IfcsCode;
	private Long bank2BankNameId;
	private String bank2BankName;
	private Long bank2BankBranchId;
	private String bank2BankBranch;
	private String bank3BankAccountNumber;
	private Long bank3AccountTypeId;
	private String bank3AccountType;
	private String bank3IfcsCode;
	private Long bank3BankNameId;
	private String bank3BankName;
	private Long bank3BankBranchId;
	private String bank3BankBranch;
	private String nominee1Code;
	private String nominee1Name;
	private Long nominee1RelationShipId;
	private String nominee1RelationShip;
	private String nominee1ContactNumber;
	private Date nominee1DateOfBirth;
	private String nominee1PanNumber;
	private String nominee1AadharNumber;
	private String nominee1BankAccountNumber;
	private Long nominee1BankAccountTypeId;
	private String nominee1BankAccountType;
	private String nominee1IfcsCode;
	private Long nominee1BankNameId;
	private String nominee1BankName;
	private Long nominee1BankBranchId;
	private String nominee1BankBranch;
	private String nominee1Percentage;
	private String nominee2Code;
	private String nominee2Name;
	private Long nominee2RelationShipId;
	private String nominee2RelationShip;
	private String nominee2ContactNumber;
	private String nominee2DateOfBirth;
	private String nominee2PanNumber;
	private String nominee2AadharNumber;
	private String nominee2BankAccountNumber;
	private Long nominee2BankAccountTypeId;
	private String nominee2BankAccountType;
	private String nominee2IfcsCode;
	private Long nominee2BankNameId;
	private String nominee2BankName;
	private Long nominee2BankBranchId;
	private String nominee2BankBranch;
	private String nominee2Percentage;
	private String nominee3Code;
	private String nominee3Name;
	private Long nominee3RelationShipId;
	private String nominee3RelationShip;
	private String nominee3ContactNumber;
	private String nominee3DateOfBirth;
	private String nominee3PanNumber;
	private String nominee3AadharNumber;
	private String nominee3BankAccountNumber;
	private Long nominee3BankAccountTypeId;
	private String nominee3BankAccountType;
	private String nominee3IfcsCode;
	private Long nominee3BankNameId;
	private String nominee3BankName;
	private Long nominee3BankBranchId;
	private String nominee3BankBranch;
	private String nominee3Percentage;
	private Long appointee1MemberNomineeId;
	private String appointee1MemberNominee;
	private String appointee1Code;
	private String appointee1Name;
	private Long appointee1RelationShipId;
	private String appointee1RelationShip;
	private String appointee1ContactNumber;
	private Date appointee1DateOfBirth;
	private String appointee1PanNumber;
	private String appointee1AadharNumber;
	private String appointee1BankAccountNumber;
	private Long appointee1AccountTypeId;
	private String appointee1AccountType;
	private String appointee1IfcsCode;
	private Long appointee1BankNameId;
	private String appointee1BankName;
	private Long appointee1BankBranchId;
	private String appointee1BankBranch;
	private String appointee1Percentage;
	private Long appointee2MemberNomineeId;
	private String appointee2MemberNominee;
	private String appointee2Code;
	private String appointee2Name;
	private Long appointee2RelationShipId;
	private String appointee2RelationShip;
	private String appointee2ContactNumber;
	private String appointee2DateOfBirth;
	private String appointee2PanNumber;
	private String appointee2AadharNumber;
	private String appointee2BankAccountNumber;
	private Long appointee2AccountTypeId;
	private String appointee2AccountType;
	private String appointee2IfcsCode;
	private Long appointee2BankNameId;
	private String appointee2BankName;
	private Long appointee2BankBranchId;
	private String appointee2BankBranch;
	private String appointee2Percentage;
	private Long appointee3MemberNomineeId;
	private String appointee3MemberNominee;
	private String appointee3Code;
	private String appointee3Name;
	private Long appointee3RelationShipId;
	private String appointee3RelationShip;
	private String appointee3ContactNumber;
	private Date appointee3DateOfBirth;
	private String appointee3PanNumber;
	private String appointee3AadharNumber;
	private String appointee3BankAccountNumber;
	private Long appointee3AccountTypeId;
	private String appointee3AccountType;
	private String appointee3IfcsCode;
	private Long appointee3BankNameId;
	private String appointee3BankName;
	private Long appointee3BankBranchId;
	private String appointee3BankBranch;
	private String appointee3Percentage;
	private String recordStatus;
	private String errorCode;
	private Boolean isActive;
	private String createdBy;
	private Date createdDate;
	private String modifiedBy;
	private Date modifiedDate;

}