package com.lic.epgs.gratuity.quotation.member.entity;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.lic.epgs.gratuity.policy.member.entity.PolicyMemberEntity;
import com.lic.epgs.gratuity.policy.member.entity.PolicyMemberNomineeEntity;
import com.lic.epgs.gratuity.policy.renewalpolicy.member.dto.RenewalMemberImportDto;
import com.lic.epgs.gratuity.policy.renewalpolicy.member.entity.RenewalPolicyTMPMemberAddressEntity;
import com.lic.epgs.gratuity.policy.renewalpolicy.member.entity.RenewalPolicyTMPMemberAppointeeEntity;
import com.lic.epgs.gratuity.policy.renewalpolicy.member.entity.RenewalPolicyTMPMemberBankAccountEntity;
import com.lic.epgs.gratuity.policy.renewalpolicy.member.entity.RenewalPolicyTMPMemberEntity;
import com.lic.epgs.gratuity.policy.renewalpolicy.member.entity.RenewalPolicyTMPMemberNomineeEntity;

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
@Table(name = "QSTG_MEMBER_BULK_STG")
public class MemberBulkStgEntity {
	protected final static Logger logger = LogManager.getLogger(MemberBulkStgEntity.class);
	
	@Id
	@Column(name = "MEMBER_BULK_STG_ID")	
	private Long memberBulkStgId;
	
	@Column(name = "MEMBER_BATCH_ID")	private Long memberBatchId;
	@Column(name = "PROPOSAL_POLICY_NUMBER")	private String proposalPolicyNumber;
	@Column(name = "LIC_ID")	private String licId;
	@Column(name = "EMPLOYEE_CODE")	private String employeeCode;
	@Column(name = "FIRST_NAME")	private String firstName;
	@Column(name = "MIDDLE_NAME")	private String middleName;
	@Column(name = "LAST_NAME")	private String lastName;
	@Column(name = "DATE_OF_BIRTH")	private Date dateOfBirth;
	@Column(name = "DATE_OF_APPOINTMENT")	private Date dateOfAppointment;
	@Column(name = "DOJ_TO_SCHEME")	private Date dojToScheme;
	@Column(name = "CATEGORY_ID")	private Long categoryId;
	@Column(name = "CATEGORY")	private String category;
	@Column(name = "BASIC_SALARY")	private Double basicSalary;
	@Column(name = "GENDER_ID") private Long genderId;
	@Column(name = "GENDER")	private String gender;
	@Column(name = "SALARY_FREQUENCY")	private String salaryFrequency;
	@Column(name = "PAN_NUMBER")	private String panNumber;
	@Column(name = "AADHAR_NUMBER")	private String aadharNumber;
	@Column(name = "CONTACT_NUMBER")	private String contactNumber;
	@Column(name = "EMAIL_ID")	private String emailId;
	@Column(name = "ADDRESS1_ADDRESS_TYPE_ID")	private Long address1AddressTypeId;
	@Column(name = "ADDRESS1_ADDRESS_TYPE")	private String address1AddressType;
	@Column(name = "ADDRESS1_PIN_CODE")	private String address1PinCode;
	@Column(name = "ADDRESS1_COUNTRY_ID")	private Long address1CountryId;
	@Column(name = "ADDRESS1_COUNTRY")	private String address1Country;
	@Column(name = "ADDRESS1_STATE_ID")	private Long address1StateId;
	@Column(name = "ADDRESS1_STATE")	private String address1State;
	@Column(name = "ADDRESS1_DISTRICT")	private String address1District;
	@Column(name = "ADDRESS1_CITY")	private String address1City;
	@Column(name = "ADDRESS1_CONTACT_NUMBER")	private String address1ContactNumber;
	@Column(name = "ADDRESS1_ADDRESS1")	private String address1Address1;
	@Column(name = "ADDRESS1_ADDRESS2")	private String address1Address2;
	@Column(name = "ADDRESS1_ADDRESS3")	private String address1Address3;
	@Column(name = "ADDRESS1_PRINTABLE")	private Boolean address1Printable;
	@Column(name = "ADDRESS2_ADDRESS_TYPE_ID")	private Long address2AddressTypeId;
	@Column(name = "ADDRESS2_ADDRESS_TYPE")	private String address2AddressType;
	@Column(name = "ADDRESS2_PIN_CODE")	private String address2PinCode;
	@Column(name = "ADDRESS2_COUNTRY_ID")	private Long address2CountryId;
	@Column(name = "ADDRESS2_COUNTRY")	private String address2Country;
	@Column(name = "ADDRESS2_STATE_ID")	private Long address2StateId;
	@Column(name = "ADDRESS2_STATE")	private String address2State;
	@Column(name = "ADDRESS2_DISTRICT")	private String address2District;
	@Column(name = "ADDRESS2_CITY")	private String address2City;
	@Column(name = "ADDRESS2_CONTACT_NUMBER")	private String address2ContactNumber;
	@Column(name = "ADDRESS2_ADDRESS1")	private String address2Address1;
	@Column(name = "ADDRESS2_ADDRESS2")	private String address2Address2;
	@Column(name = "ADDRESS2_ADDRESS3")	private String address2Address3;
	@Column(name = "ADDRESS2_PRINTABLE")	private Boolean address2Printable;
	@Column(name = "ADDRESS3_ADDRESS_TYPE_ID")	private Long address3AddressTypeId;
	@Column(name = "ADDRESS3_ADDRESS_TYPE")	private String address3AddressType;
	@Column(name = "ADDRESS3_PIN_CODE")	private String address3PinCode;
	@Column(name = "ADDRESS3_COUNTRY_ID")	private Long address3CountryId;
	@Column(name = "ADDRESS3_COUNTRY")	private String address3Country;
	@Column(name = "ADDRESS3_STATE_ID")	private Long address3StateId;
	@Column(name = "ADDRESS3_STATE")	private String address3State;
	@Column(name = "ADDRESS3_DISTRICT")	private String address3District;
	@Column(name = "ADDRESS3_CITY")	private String address3City;
	@Column(name = "ADDRESS3_CONTACT_NUMBER")	private String address3ContactNumber;
	@Column(name = "ADDRESS3_ADDRESS1")	private String address3Address1;
	@Column(name = "ADDRESS3_ADDRESS2")	private String address3Address2;
	@Column(name = "ADDRESS3_ADDRESS3")	private String address3Address3;
	@Column(name = "ADDRESS3_PRINTABLE")	private Boolean address3Printable;
	@Column(name = "BANK1_BANK_ACCOUNT_NUMBER")	private String bank1BankAccountNumber;
	@Column(name = "BANK1_ACCOUNT_TYPE_ID")	private Long bank1AccountTypeId;
	@Column(name = "BANK1_ACCOUNT_TYPE")	private String bank1AccountType;
	@Column(name = "BANK1_IFCS_CODE")	private String bank1IfcsCode;
	@Column(name = "BANK1_BANK_NAME_ID")	private Long bank1BankNameId;
	@Column(name = "BANK1_BANK_NAME")	private String bank1BankName;
	@Column(name = "BANK1_BANK_BRANCH_ID")	private Long bank1BankBranchId;
	@Column(name = "BANK1_BANK_BRANCH")	private String bank1BankBranch;
	@Column(name = "BANK2_BANK_ACCOUNT_NUMBER")	private String bank2BankAccountNumber;
	@Column(name = "BANK2_ACCOUNT_TYPE_ID")	private Long bank2AccountTypeId;
	@Column(name = "BANK2_ACCOUNT_TYPE")	private String bank2AccountType;
	@Column(name = "BANK2_IFCS_CODE")	private String bank2IfcsCode;
	@Column(name = "BANK2_BANK_NAME_ID")	private Long bank2BankNameId;
	@Column(name = "BANK2_BANK_NAME")	private String bank2BankName;
	@Column(name = "BANK2_BANK_BRANCH_ID")	private Long bank2BankBranchId;
	@Column(name = "BANK2_BANK_BRANCH")	private String bank2BankBranch;
	@Column(name = "BANK3_BANK_ACCOUNT_NUMBER")	private String bank3BankAccountNumber;
	@Column(name = "BANK3_ACCOUNT_TYPE_ID")	private Long bank3AccountTypeId;
	@Column(name = "BANK3_ACCOUNT_TYPE")	private String bank3AccountType;
	@Column(name = "BANK3_IFCS_CODE")	private String bank3IfcsCode;
	@Column(name = "BANK3_BANK_NAME_ID")	private Long bank3BankNameId;
	@Column(name = "BANK3_BANK_NAME")	private String bank3BankName;
	@Column(name = "BANK3_BANK_BRANCH_ID")	private Long bank3BankBranchId;
	@Column(name = "BANK3_BANK_BRANCH")	private String bank3BankBranch;
	@Column(name = "NOMINEE1_CODE")	private String nominee1Code;
	@Column(name = "NOMINEE1_NAME")	private String nominee1Name;
	@Column(name = "NOMINEE1_RELATION_SHIP_ID")	private Long nominee1RelationShipId;
	@Column(name = "NOMINEE1_RELATION_SHIP")	private String nominee1RelationShip;
	@Column(name = "NOMINEE1_CONTACT_NUMBER")	private String nominee1ContactNumber;
	@Column(name = "NOMINEE1_DATE_OF_BIRTH")	private Date nominee1DateOfBirth;
	@Column(name = "NOMINEE1_PAN_NUMBER")	private String nominee1PanNumber;
	@Column(name = "NOMINEE1_AADHAR_NUMBER")	private String nominee1AadharNumber;
	@Column(name = "NOMINEE1_BANK_ACCOUNT_NUMBER")	private String nominee1BankAccountNumber;
	@Column(name = "NOMINEE1_BANK_ACCOUNT_TYPE_ID")	private Long nominee1BankAccountTypeId;
	@Column(name = "NOMINEE1_BANK_ACCOUNT_TYPE")	private String nominee1BankAccountType;
	@Column(name = "NOMINEE1_IFCS_CODE")	private String nominee1IfcsCode;
	@Column(name = "NOMINEE1_BANK_NAME_ID")	private Long nominee1BankNameId;
	@Column(name = "NOMINEE1_BANK_NAME")	private String nominee1BankName;
	@Column(name = "NOMINEE1_BANK_BRANCH_ID")	private Long nominee1BankBranchId;
	@Column(name = "NOMINEE1_BANK_BRANCH")	private String nominee1BankBranch;
	@Column(name = "NOMINEE1_PERCENTAGE")	private Float nominee1Percentage;
	@Column(name = "NOMINEE2_CODE")	private String nominee2Code;
	@Column(name = "NOMINEE2_NAME")	private String nominee2Name;
	@Column(name = "NOMINEE2_RELATION_SHIP_ID")	private Long nominee2RelationShipId;
	@Column(name = "NOMINEE2_RELATION_SHIP")	private String nominee2RelationShip;
	@Column(name = "NOMINEE2_CONTACT_NUMBER")	private String nominee2ContactNumber;
	@Column(name = "NOMINEE2_DATE_OF_BIRTH")	private Date nominee2DateOfBirth;
	@Column(name = "NOMINEE2_PAN_NUMBER")	private String nominee2PanNumber;
	@Column(name = "NOMINEE2_AADHAR_NUMBER")	private String nominee2AadharNumber;
	@Column(name = "NOMINEE2_BANK_ACCOUNT_NUMBER")	private String nominee2BankAccountNumber;
	@Column(name = "NOMINEE2_BANK_ACCOUNT_TYPE_ID")	private Long nominee2BankAccountTypeId;
	@Column(name = "NOMINEE2_BANK_ACCOUNT_TYPE")	private String nominee2BankAccountType;
	@Column(name = "NOMINEE2_IFCS_CODE")	private String nominee2IfcsCode;
	@Column(name = "NOMINEE2_BANK_NAME_ID")	private Long nominee2BankNameId;
	@Column(name = "NOMINEE2_BANK_NAME")	private String nominee2BankName;
	@Column(name = "NOMINEE2_BANK_BRANCH_ID")	private Long nominee2BankBranchId;
	@Column(name = "NOMINEE2_BANK_BRANCH")	private String nominee2BankBranch;
	@Column(name = "NOMINEE2_PERCENTAGE")	private Float nominee2Percentage;
	@Column(name = "NOMINEE3_CODE")	private String nominee3Code;
	@Column(name = "NOMINEE3_NAME")	private String nominee3Name;
	@Column(name = "NOMINEE3_RELATION_SHIP_ID")	private Long nominee3RelationShipId;
	@Column(name = "NOMINEE3_RELATION_SHIP")	private String nominee3RelationShip;
	@Column(name = "NOMINEE3_CONTACT_NUMBER")	private String nominee3ContactNumber;
	@Column(name = "NOMINEE3_DATE_OF_BIRTH")	private Date nominee3DateOfBirth;
	@Column(name = "NOMINEE3_PAN_NUMBER")	private String nominee3PanNumber;
	@Column(name = "NOMINEE3_AADHAR_NUMBER")	private String nominee3AadharNumber;
	@Column(name = "NOMINEE3_BANK_ACCOUNT_NUMBER")	private String nominee3BankAccountNumber;
	@Column(name = "NOMINEE3_BANK_ACCOUNT_TYPE_ID")	private Long nominee3BankAccountTypeId;
	@Column(name = "NOMINEE3_BANK_ACCOUNT_TYPE")	private String nominee3BankAccountType;
	@Column(name = "NOMINEE3_IFCS_CODE")	private String nominee3IfcsCode;
	@Column(name = "NOMINEE3_BANK_NAME_ID")	private Long nominee3BankNameId;
	@Column(name = "NOMINEE3_BANK_NAME")	private String nominee3BankName;
	@Column(name = "NOMINEE3_BANK_BRANCH_ID")	private Long nominee3BankBranchId;
	@Column(name = "NOMINEE3_BANK_BRANCH")	private String nominee3BankBranch;
	@Column(name = "NOMINEE3_PERCENTAGE")	private Float nominee3Percentage;
	@Column(name = "APPOINTEE1_MEMBER_NOMINEE_ID")	private Long appointee1MemberNomineeId;
	@Column(name = "APPOINTEE1_MEMBER_NOMINEE")	private String appointee1MemberNominee;
	@Column(name = "APPOINTEE1_CODE")	private String appointee1Code;
	@Column(name = "APPOINTEE1_NAME")	private String appointee1Name;
	@Column(name = "APPOINTEE1_RELATION_SHIP_ID")	private Long appointee1RelationShipId;
	@Column(name = "APPOINTEE1_RELATION_SHIP")	private String appointee1RelationShip;
	@Column(name = "APPOINTEE1_CONTACT_NUMBER")	private String appointee1ContactNumber;
	@Column(name = "APPOINTEE1_DATE_OF_BIRTH")	private Date appointee1DateOfBirth;
	@Column(name = "APPOINTEE1_PAN_NUMBER")	private String appointee1PanNumber;
	@Column(name = "APPOINTEE1_AADHAR_NUMBER")	private String appointee1AadharNumber;
	@Column(name = "APPOINTEE1_BANK_ACCOUNT_NUMBER")	private String appointee1BankAccountNumber;
	@Column(name = "APPOINTEE1_ACCOUNT_TYPE_ID")	private Long appointee1AccountTypeId;
	@Column(name = "APPOINTEE1_ACCOUNT_TYPE")	private String appointee1AccountType;
	@Column(name = "APPOINTEE1_IFCS_CODE")	private String appointee1IfcsCode;
	@Column(name = "APPOINTEE1_BANK_NAME_ID")	private Long appointee1BankNameId;
	@Column(name = "APPOINTEE1_BANK_NAME")	private String appointee1BankName;
	@Column(name = "APPOINTEE1_BANK_BRANCH_ID")	private Long appointee1BankBranchId;
	@Column(name = "APPOINTEE1_BANK_BRANCH")	private String appointee1BankBranch;
	@Column(name = "APPOINTEE1_PERCENTAGE")	private Float appointee1Percentage;
	@Column(name = "APPOINTEE2_MEMBER_NOMINEE_ID")	private Long appointee2MemberNomineeId;
	@Column(name = "APPOINTEE2_MEMBER_NOMINEE")	private String appointee2MemberNominee;
	@Column(name = "APPOINTEE2_CODE")	private String appointee2Code;
	@Column(name = "APPOINTEE2_NAME")	private String appointee2Name;
	@Column(name = "APPOINTEE2_RELATION_SHIP_ID")	private Long appointee2RelationShipId;
	@Column(name = "APPOINTEE2_RELATION_SHIP")	private String appointee2RelationShip;
	@Column(name = "APPOINTEE2_CONTACT_NUMBER")	private String appointee2ContactNumber;
	@Column(name = "APPOINTEE2_DATE_OF_BIRTH")	private Date appointee2DateOfBirth;
	@Column(name = "APPOINTEE2_PAN_NUMBER")	private String appointee2PanNumber;
	@Column(name = "APPOINTEE2_AADHAR_NUMBER")	private String appointee2AadharNumber;
	@Column(name = "APPOINTEE2_BANK_ACCOUNT_NUMBER")	private String appointee2BankAccountNumber;
	@Column(name = "APPOINTEE2_ACCOUNT_TYPE_ID")	private Long appointee2AccountTypeId;
	@Column(name = "APPOINTEE2_ACCOUNT_TYPE")	private String appointee2AccountType;
	@Column(name = "APPOINTEE2_IFCS_CODE")	private String appointee2IfcsCode;
	@Column(name = "APPOINTEE2_BANK_NAME_ID")	private Long appointee2BankNameId;
	@Column(name = "APPOINTEE2_BANK_NAME")	private String appointee2BankName;
	@Column(name = "APPOINTEE2_BANK_BRANCH_ID")	private Long appointee2BankBranchId;
	@Column(name = "APPOINTEE2_BANK_BRANCH")	private String appointee2BankBranch;
	@Column(name = "APPOINTEE2_PERCENTAGE")	private Float appointee2Percentage;
	@Column(name = "APPOINTEE3_MEMBER_NOMINEE_ID")	private Long appointee3MemberNomineeId;
	@Column(name = "APPOINTEE3_MEMBER_NOMINEE")	private String appointee3MemberNominee;
	@Column(name = "APPOINTEE3_CODE")	private String appointee3Code;
	@Column(name = "APPOINTEE3_NAME")	private String appointee3Name;
	@Column(name = "APPOINTEE3_RELATION_SHIP_ID")	private Long appointee3RelationShipId;
	@Column(name = "APPOINTEE3_RELATION_SHIP")	private String appointee3RelationShip;
	@Column(name = "APPOINTEE3_CONTACT_NUMBER")	private String appointee3ContactNumber;
	@Column(name = "APPOINTEE3_DATE_OF_BIRTH")	private Date appointee3DateOfBirth;
	@Column(name = "APPOINTEE3_PAN_NUMBER")	private String appointee3PanNumber;
	@Column(name = "APPOINTEE3_AADHAR_NUMBER")	private String appointee3AadharNumber;
	@Column(name = "APPOINTEE3_BANK_ACCOUNT_NUMBER")	private String appointee3BankAccountNumber;
	@Column(name = "APPOINTEE3_ACCOUNT_TYPE_ID")	private Long appointee3AccountTypeId;
	@Column(name = "APPOINTEE3_ACCOUNT_TYPE")	private String appointee3AccountType;
	@Column(name = "APPOINTEE3_IFCS_CODE")	private String appointee3IfcsCode;
	@Column(name = "APPOINTEE3_BANK_NAME_ID")	private Long appointee3BankNameId;
	@Column(name = "APPOINTEE3_BANK_NAME")	private String appointee3BankName;
	@Column(name = "APPOINTEE3_BANK_BRANCH_ID")	private Long appointee3BankBranchId;
	@Column(name = "APPOINTEE3_BANK_BRANCH")	private String appointee3BankBranch;
	@Column(name = "APPOINTEE3_PERCENTAGE")	private Float appointee3Percentage;
	@Column(name = "RECORD_STATUS") private String recordStatus;
	@Column(name = "ERROR_CODE") private String errorCode;
	@Column(name = "IS_ACTIVE")	private Boolean isActive;
	@Column(name = "CREATED_BY")	private String createdBy;
	@Column(name = "CREATED_DATE")	private Date createdDate;
	@Column(name = "MODIFIED_BY")	private String modifiedBy;
	@Column(name = "MODIFIED_DATE")	private Date modifiedDate;
	
	@Column(name = "NOMINEE1_GENDER_ID") private Long nominee1GenderId;
	@Column(name = "NOMINEE2_GENDER_ID") private Long nominee2GenderId;
	@Column(name = "NOMINEE3_GENDER_ID") private Long nominee3GenderId;
	
//	For Claims
	
	@Column(name = "MODE_OF_EXIT") private Long modeOfExit;
	@Column(name = "DATE_OF_EXIT") private Date dateOfExit;
	@Column(name = "DATE_OF_DEATH") private Date dateOfDeath;
	@Column(name = "REASON_FOR_DEATH_ID") private Long reasonForDeathId;
	@Column(name = "REASON_FOR_DEATH_OTHER") private String reasonForDeathOther;
	@Column(name = "PLACE_OF_DEATH") private String placeOfDeath;
	@Column(name = "PMST_MEMBER_ID") private Long pmstMemberId;
	@Column(name = "MODE_OF_EXIT_NAME") private String modeOfExitName;
	@Column(name = "REASON_FOR_DEATH") private String reasonForDeath;
	
//	end
	private Boolean nominee1AssociatedWithAppointee = false;
	private Boolean nominee2AssociatedWithAppointee = false;
	private Boolean nominee3AssociatedWithAppointee = false;
	
	public MemberEntity toMemberEntity(Long quotationId, String username) {
		Long memberStatusActive = 146l;

		MemberEntity entity = new MemberEntity();
		entity.setStatusId(memberStatusActive);
		entity.setFirstName(this.firstName);
		entity.setMiddleName(this.middleName);
		entity.setLastName(this.lastName);
		entity.setAadharNumber(this.aadharNumber);
		entity.setBasicSalary(this.basicSalary);
		entity.setCategoryId(this.categoryId);
		entity.setEmployeeCode(this.employeeCode);
		if(this.contactNumber != null) {
			entity.setLandlineNo(Long.parseLong(this.contactNumber));
		}
		entity.setCreatedBy(username);
		entity.setCreatedDate(new Date());
		entity.setDateOfBirth(this.dateOfBirth);
		entity.setDateOfAppointment(this.dateOfAppointment);
		entity.setEmailId(this.emailId);
		entity.setGenderId(this.genderId);
		entity.setIsActive(true);
		entity.setLicId(this.licId);
		entity.setProposalPolicyNumber(this.proposalPolicyNumber);
		entity.setDojToScheme(this.dojToScheme);
		entity.setSalaryFrequency(this.salaryFrequency);
		entity.setMemberBatchId(this.memberBatchId);
		entity.setPanNumber(this.panNumber);
		entity.setQuotationId(quotationId);
		
		entity.setAddresses(toMemberAddresses(entity));
		entity.setAppointees(toMemberAppointees(entity));
		entity.setBankAccounts(toMemberBankAccounts(entity));
		entity.setNominees(toMemberNominees(entity));
		
		return entity;		
	}
	
	private Set<MemberAddressEntity> toMemberAddresses(MemberEntity memberEntity) {
		Set<MemberAddressEntity> entities = new HashSet<>();
		if(this.address1AddressTypeId != null) {
			entities.add(MemberAddressEntity.builder()
					.member(memberEntity)
					.addressTypeId(this.address1AddressTypeId)
					.pinCode(this.address1PinCode)
					.countryId(this.address1CountryId)
					.stateId(this.address1StateId)
					.district(this.address1District)
					.city(this.address1City)
					.contactNumber(this.address1ContactNumber)
					.address1(this.address1Address1)
					.address2(this.address1Address2)
					.address3(this.address1Address3)
					.isPrintable(this.address1Printable)
					.isActive(true)
					.createdBy(memberEntity.getCreatedBy())
					.createdDate(new Date())
					.build());
		}
		if(this.address2AddressTypeId != null) {
			entities.add(MemberAddressEntity.builder()
					.member(memberEntity)
					.addressTypeId(this.address2AddressTypeId)
					.pinCode(this.address2PinCode)
					.countryId(this.address2CountryId)
					.stateId(this.address2StateId)
					.district(this.address2District)
					.city(this.address2City)
					.contactNumber(this.address2ContactNumber)
					.address2(this.address2Address2)
					.address2(this.address2Address2)
					.address3(this.address2Address3)
					.isPrintable(this.address2Printable)
					.isActive(true)
					.createdBy(memberEntity.getCreatedBy())
					.createdDate(new Date())
					.build());
		}
		if(this.address3AddressTypeId != null) {
			entities.add(MemberAddressEntity.builder()
					.member(memberEntity)
					.addressTypeId(this.address3AddressTypeId)
					.pinCode(this.address3PinCode)
					.countryId(this.address3CountryId)
					.stateId(this.address3StateId)
					.district(this.address3District)
					.city(this.address3City)
					.contactNumber(this.address3ContactNumber)
					.address3(this.address3Address3)
					.address3(this.address3Address3)
					.address3(this.address3Address3)
					.isPrintable(this.address3Printable)
					.isActive(true)
					.createdBy(memberEntity.getCreatedBy())
					.createdDate(new Date())
					.build());
		}
		
		return entities;
	}
	
	private Set<MemberAppointeeEntity> toMemberAppointees(MemberEntity memberEntity) {
		Set<MemberAppointeeEntity> entities = new HashSet<>();
		MemberNomineeEntity memberNomineeEntity1 = new MemberNomineeEntity();
		if (this.appointee1MemberNominee != null && this.nominee1Name != null) {
			if (this.appointee1MemberNominee.equals(this.nominee1Name)) {
				memberNomineeEntity1 = toMemberNominee(memberEntity, 1);
			}
		}
		if (this.appointee1MemberNominee != null && this.nominee2Name != null) {
			if (this.appointee1MemberNominee.equals(this.nominee2Name)) {
				memberNomineeEntity1 = toMemberNominee(memberEntity, 2);
			}
		}
		if (this.appointee1MemberNominee != null && this.nominee3Name != null) {
			if (this.appointee1MemberNominee.equals(this.nominee3Name)) {
				memberNomineeEntity1 = toMemberNominee(memberEntity, 3);
			}
		}
		if(memberNomineeEntity1.getName() != null) {
			entities.add(MemberAppointeeEntity.builder()
					.member(memberEntity)
					.nominee(memberNomineeEntity1)
					.code(this.appointee1Code)
					.name(this.appointee1Name)
					.relationShipId(this.appointee1RelationShipId)
					.contactNumber(this.appointee1ContactNumber)
					.dateOfBirth(this.appointee1DateOfBirth)
					.panNumber(this.appointee1PanNumber)
					.aadharNumber(this.appointee1AadharNumber)
					.bankAccountNumber(this.appointee1BankAccountNumber)
					.accountTypeId(this.appointee1AccountTypeId)
					.ifscCode(this.appointee1IfcsCode)
					.bankNameId(this.appointee1BankNameId)
					.bankBranchId(this.appointee1BankBranchId)
					.bankName(this.appointee1BankName)
					.bankBranch(this.appointee1BankBranch)
					.percentage(this.appointee1Percentage)
					.isActive(true)
					.createdBy(memberEntity.getCreatedBy())
					.createdDate(new Date())
					.build());
		}
		
		MemberNomineeEntity memberNomineeEntity2 = new MemberNomineeEntity();
		if (this.appointee2MemberNominee != null && this.nominee1Name != null) {
			if (this.appointee2MemberNominee.equals(this.nominee1Name)) {
				memberNomineeEntity2 = toMemberNominee(memberEntity, 1);
			}
		}
		if (this.appointee2MemberNominee != null && this.nominee2Name != null) {
			if (this.appointee2MemberNominee.equals(this.nominee2Name)) {
				memberNomineeEntity2 = toMemberNominee(memberEntity, 2);
			}
		}
		if (this.appointee2MemberNominee != null && this.nominee3Name != null) {
			if (this.appointee2MemberNominee.equals(this.nominee3Name)) {
				memberNomineeEntity2 = toMemberNominee(memberEntity, 3);
			}
		}
		if(memberNomineeEntity2.getName() != null) {
			entities.add(MemberAppointeeEntity.builder()
					.member(memberEntity)
					.nominee(memberNomineeEntity2)
					.code(this.appointee2Code)
					.name(this.appointee2Name)
					.relationShipId(this.appointee2RelationShipId)
					.contactNumber(this.appointee2ContactNumber)
					.dateOfBirth(this.appointee2DateOfBirth)
					.panNumber(this.appointee2PanNumber)
					.aadharNumber(this.appointee2AadharNumber)
					.bankAccountNumber(this.appointee2BankAccountNumber)
					.accountTypeId(this.appointee2AccountTypeId)
					.ifscCode(this.appointee2IfcsCode)
					.bankNameId(this.appointee2BankNameId)
					.bankBranchId(this.appointee2BankBranchId)
					.bankName(this.appointee2BankName)
					.bankBranch(this.appointee2BankBranch)
					.percentage(this.appointee2Percentage)
					.isActive(true)
					.createdBy(memberEntity.getCreatedBy())
					.createdDate(new Date())
					.build());
		}
		
		MemberNomineeEntity memberNomineeEntity3 = new MemberNomineeEntity();
		if (this.appointee3MemberNominee != null && this.nominee1Name != null) {
			if (this.appointee3MemberNominee.equals(this.nominee1Name)) {
				memberNomineeEntity3 = toMemberNominee(memberEntity, 1);
			}
		}
		if (this.appointee3MemberNominee != null && this.nominee2Name != null) {
			if (this.appointee3MemberNominee.equals(this.nominee2Name)) {
				memberNomineeEntity3 = toMemberNominee(memberEntity, 2);
			}
		}
		if (this.appointee3MemberNominee != null && this.nominee3Name != null) {
			if (this.appointee3MemberNominee.equals(this.nominee3Name)) {
				memberNomineeEntity3 = toMemberNominee(memberEntity, 3);
			}
		}
		if(memberNomineeEntity3.getName() != null) {
			entities.add(MemberAppointeeEntity.builder()
					.member(memberEntity)
					.nominee(memberNomineeEntity3)
					.code(this.appointee3Code)
					.name(this.appointee3Name)
					.relationShipId(this.appointee3RelationShipId)
					.contactNumber(this.appointee3ContactNumber)
					.dateOfBirth(this.appointee3DateOfBirth)
					.panNumber(this.appointee3PanNumber)
					.aadharNumber(this.appointee3AadharNumber)
					.bankAccountNumber(this.appointee3BankAccountNumber)
					.accountTypeId(this.appointee3AccountTypeId)
					.ifscCode(this.appointee3IfcsCode)
					.bankNameId(this.appointee3BankNameId)
					.bankBranchId(this.appointee3BankBranchId)
					.bankName(this.appointee3BankName)
					.bankBranch(this.appointee3BankBranch)
					.percentage(this.appointee3Percentage)
					.isActive(true)
					.createdBy(memberEntity.getCreatedBy())
					.createdDate(new Date())
					.build());
		}
		return entities;
	}
	
	private Set<MemberBankAccount> toMemberBankAccounts(MemberEntity memberEntity) {
		Set<MemberBankAccount> entities = new HashSet<>();
		if(this.bank1BankAccountNumber != null) {
			entities.add(MemberBankAccount.builder()
					.member(memberEntity)
					.bankAccountNumber(this.bank1BankAccountNumber)
					.accountTypeId(this.bank1AccountTypeId)
					.ifscCode(this.bank1IfcsCode)
					.bankNameId(this.bank1BankNameId)
					.bankBranchId(this.bank1BankBranchId)
					.bankName(this.bank1BankName)
					.bankBranch(this.bank1BankBranch)
					.isActive(true)
					.createdBy(memberEntity.getCreatedBy())
					.createdDate(new Date())
					.build());
		}
		if(this.bank2BankAccountNumber != null) {
			entities.add(MemberBankAccount.builder()
					.member(memberEntity)
					.bankAccountNumber(this.bank2BankAccountNumber)
					.accountTypeId(this.bank2AccountTypeId)
					.ifscCode(this.bank2IfcsCode)
					.bankNameId(this.bank2BankNameId)
					.bankBranchId(this.bank2BankBranchId)
					.bankName(this.bank2BankName)
					.bankBranch(this.bank2BankBranch)
					.isActive(true)
					.createdBy(memberEntity.getCreatedBy())
					.createdDate(new Date())
					.build());
		}
		if(this.bank3BankAccountNumber != null) {
			entities.add(MemberBankAccount.builder()
					.member(memberEntity)
					.bankAccountNumber(this.bank3BankAccountNumber)
					.accountTypeId(this.bank3AccountTypeId)
					.ifscCode(this.bank3IfcsCode)
					.bankNameId(this.bank3BankNameId)
					.bankBranchId(this.bank3BankBranchId)
					.bankName(this.bank3BankName)
					.bankBranch(this.bank3BankBranch)
       				.isActive(true)
					.createdBy(memberEntity.getCreatedBy())
					.createdDate(new Date())
					.build());
		}
		
		return entities;
	}
	
	private Set<MemberNomineeEntity> toMemberNominees(MemberEntity memberEntity) {
		Set<MemberNomineeEntity> entities = new HashSet<>();
		if (!this.nominee1AssociatedWithAppointee) {
			if(this.nominee1Name  != null) {
				entities.add(MemberNomineeEntity.builder()
						.member(memberEntity)
						.code(this.nominee1Code)
						.name(this.nominee1Name)
						.relationshipId(this.nominee1RelationShipId)
						.contactNumber(this.nominee1ContactNumber)
						.dateOfBirth(this.nominee1DateOfBirth)
						.panNumber(this.nominee1PanNumber)
						.aadharNumber(this.nominee1AadharNumber)
						.bankAccountNumber(this.nominee1BankAccountNumber)
						.bankAccountTypeId(this.nominee1BankAccountTypeId)
						.ifscCode(this.nominee1IfcsCode)
						.bankNameId(this.nominee1BankNameId)
						.bankBranchId(this.nominee1BankBranchId)
						.bankNameId(this.nominee1BankNameId)
						.bankBranchId(this.nominee1BankBranchId)
						.percentage(this.nominee1Percentage)
						.isActive(true)
						.createdBy(memberEntity.getCreatedBy())
						.createdDate(new Date())
						.build());
			}
			
		}
		if (!this.nominee2AssociatedWithAppointee) {
			if(this.nominee2Name  != null) {
				entities.add(MemberNomineeEntity.builder()
						.member(memberEntity)
						.code(this.nominee2Code)
						.name(this.nominee2Name)
						.relationshipId(this.nominee2RelationShipId)
						.contactNumber(this.nominee2ContactNumber)
						.dateOfBirth(this.nominee2DateOfBirth)
						.panNumber(this.nominee2PanNumber)
						.aadharNumber(this.nominee2AadharNumber)
						.bankAccountNumber(this.nominee2BankAccountNumber)
						.bankAccountTypeId(this.nominee2BankAccountTypeId)
						.ifscCode(this.nominee2IfcsCode)
						.bankNameId(this.nominee2BankNameId)
						.bankBranchId(this.nominee2BankBranchId)
						.bankName(this.nominee2BankName)		
						.bankBranch(this.nominee2BankBranch)
						.percentage(this.nominee2Percentage)
						.isActive(true)
						.createdBy(memberEntity.getCreatedBy())
						.createdDate(new Date())
						.build());
			}
		}
		if (!this.nominee3AssociatedWithAppointee) {
			if(this.nominee3Name  != null) {
				entities.add(MemberNomineeEntity.builder()
						.member(memberEntity)
						.code(this.nominee3Code)
						.name(this.nominee3Name)
						.relationshipId(this.nominee3RelationShipId)
						.contactNumber(this.nominee3ContactNumber)
						.dateOfBirth(this.nominee3DateOfBirth)
						.panNumber(this.nominee3PanNumber)
						.aadharNumber(this.nominee3AadharNumber)
						.bankAccountNumber(this.nominee3BankAccountNumber)
						.bankAccountTypeId(this.nominee3BankAccountTypeId)
						.ifscCode(this.nominee3IfcsCode)
						.bankNameId(this.nominee3BankNameId)
						.bankBranchId(this.nominee3BankBranchId)
						.bankName(this.nominee3BankName)
						.bankBranch(this.nominee3BankBranch)
						.percentage(this.nominee3Percentage)
						.isActive(true)
						.createdBy(memberEntity.getCreatedBy())
						.createdDate(new Date())
						.build());
			}
		
		}
		return entities;
	}
	
	private MemberNomineeEntity toMemberNominee(MemberEntity memberEntity, int index) {
		if (index == 1) {
			this.nominee1AssociatedWithAppointee=true;
			return MemberNomineeEntity.builder()
					.member(memberEntity)
					.code(this.nominee1Code)
					.name(this.nominee1Name)
					.relationshipId(this.nominee1RelationShipId)
					.contactNumber(this.nominee1ContactNumber)
					.dateOfBirth(this.nominee1DateOfBirth)
					.panNumber(this.nominee1PanNumber)
					.aadharNumber(this.nominee1AadharNumber)
					.bankAccountNumber(this.nominee1BankAccountNumber)
					.bankAccountTypeId(this.nominee1BankAccountTypeId)
					.ifscCode(this.nominee1IfcsCode)
					.bankNameId(this.nominee1BankNameId)
					.bankBranchId(this.nominee1BankBranchId)
					.bankName(this.nominee1BankName)
					.bankBranch(this.nominee1BankBranch)
					.percentage(this.nominee1Percentage)
					.isActive(true)
					.createdBy(memberEntity.getCreatedBy())
					.createdDate(new Date())
					.build();
		}
		if (index == 2) {
			this.nominee2AssociatedWithAppointee=true;
			return MemberNomineeEntity.builder()
					.member(memberEntity)
					.code(this.nominee2Code)
					.name(this.nominee2Name)
					.relationshipId(this.nominee2RelationShipId)
					.contactNumber(this.nominee2ContactNumber)
					.dateOfBirth(this.nominee2DateOfBirth)
					.panNumber(this.nominee2PanNumber)
					.aadharNumber(this.nominee2AadharNumber)
					.bankAccountNumber(this.nominee2BankAccountNumber)
					.bankAccountTypeId(this.nominee2BankAccountTypeId)
					.ifscCode(this.nominee2IfcsCode)
					.bankNameId(this.nominee2BankNameId)
					.bankBranchId(this.nominee2BankBranchId)
					.bankName(this.nominee2BankName)		
					.bankBranch(this.nominee2BankBranch)
					.percentage(this.nominee2Percentage)
					.isActive(true)
					.createdBy(memberEntity.getCreatedBy())
					.createdDate(new Date())
					.build();
		}
		if (index == 3) {
			this.nominee3AssociatedWithAppointee=true;
			return MemberNomineeEntity.builder()
			.member(memberEntity)
			.code(this.nominee3Code)
			.name(this.nominee3Name)
			.relationshipId(this.nominee3RelationShipId)
			.contactNumber(this.nominee3ContactNumber)
			.dateOfBirth(this.nominee3DateOfBirth)
			.panNumber(this.nominee3PanNumber)
			.aadharNumber(this.nominee3AadharNumber)
			.bankAccountNumber(this.nominee3BankAccountNumber)
			.bankAccountTypeId(this.nominee3BankAccountTypeId)
			.ifscCode(this.nominee3IfcsCode)
			.bankNameId(this.nominee3BankNameId)
			.bankBranchId(this.nominee3BankBranchId)
			.bankName(this.nominee3BankName)
			.bankBranch(this.nominee3BankBranch)
			.percentage(this.nominee3Percentage)
			.isActive(true)
			.createdBy(memberEntity.getCreatedBy())
			.createdDate(new Date())
			.build();
		}
		return null;
	}

	public RenewalPolicyTMPMemberEntity toPolicyTmpEntity(Long masterMemberId, Long pmstPolicyId,RenewalMemberImportDto renewalMemberImportDto) {
		Long renewalMemberStatusid=146l;
		RenewalPolicyTMPMemberEntity renewalEntity = new RenewalPolicyTMPMemberEntity(); 
		renewalEntity.setFirstName(this.firstName);
		renewalEntity.setMiddleName(this.middleName);
		renewalEntity.setLastName(this.lastName);
		renewalEntity.setAadharNumber(this.aadharNumber);
		renewalEntity.setBasicSalary(this.basicSalary);
		renewalEntity.setCategoryId(this.categoryId);
		renewalEntity.setEmployeeCode(this.employeeCode);
		renewalEntity.setTmpPolicyId(renewalMemberImportDto.getTmpPolicyId());
		renewalEntity.setNewLife("Y");	
		if(masterMemberId != null) {
		renewalEntity.setPmstMemebrId(masterMemberId);
		renewalEntity.setPolicyId(pmstPolicyId);	
		renewalEntity.setNewLife("N");	
		}
		System.out.println("pmstPolicyId="+renewalEntity.getPmstMemebrId());
		if(this.contactNumber != null) {
			renewalEntity.setLandlineNo(Long.parseLong(this.contactNumber));
		}
		renewalEntity.setCreatedBy(renewalMemberImportDto.getUsername());
		renewalEntity.setCreatedDate(new Date());
		renewalEntity.setDateOfBirth(this.dateOfBirth);
		renewalEntity.setDateOfAppointment(this.dateOfAppointment);
		renewalEntity.setEmailId(this.emailId);
		renewalEntity.setGenderId(this.genderId);
		renewalEntity.setIsActive(true);
		renewalEntity.setLicId(this.licId);
		renewalEntity.setProposalPolicyNumber(this.proposalPolicyNumber);
		renewalEntity.setDojToScheme(this.dojToScheme);
		renewalEntity.setSalaryFrequency(this.salaryFrequency);
		renewalEntity.setPanNumber(this.panNumber);
		renewalEntity.setStatusId(renewalMemberStatusid);
		renewalEntity.setRecordType(this.recordStatus);
		
		renewalEntity.setAddresses(toRenewalMemberAddresses(renewalEntity));
		renewalEntity.setAppointees(toRenewalMemberAppointees(renewalEntity));
		renewalEntity.setNominees(toRenewalMemberNominees(renewalEntity));
		renewalEntity.setBankAccounts(toRenewalMemberBankAccounts(renewalEntity));
		System.out.println("renewalmemberStatusId="+renewalEntity.getStatusId());
		return renewalEntity;		
	}

	
	private Set<RenewalPolicyTMPMemberAddressEntity> toRenewalMemberAddresses(
			RenewalPolicyTMPMemberEntity renewalEntity) {
		Set<RenewalPolicyTMPMemberAddressEntity> entities = new HashSet<>();
		if(this.address1AddressTypeId != null) {
			entities.add(RenewalPolicyTMPMemberAddressEntity.builder()
					.member(renewalEntity)
					.addressTypeId(this.address1AddressTypeId)
					.pinCode(this.address1PinCode)
					.country(this.address1Country)
					.stateName(this.address1State)
					.district(this.address1District)
					.city(this.address1City)
					.address1(this.address1Address1)
					.address2(this.address1Address2)
					.address3(this.address1Address3)
					.isActive(true)
					.createdBy(renewalEntity.getCreatedBy())
					.createdDate(new Date())
					.build());
		}
		if(this.address2AddressTypeId != null) {
			entities.add(RenewalPolicyTMPMemberAddressEntity.builder()
					.member(renewalEntity)
					.addressTypeId(this.address2AddressTypeId)
					.pinCode(this.address2PinCode)
					.country(this.address2Country)
					.stateName(this.address2State)
					.district(this.address2District)
					.city(this.address2City)
					.address2(this.address2Address2)
					.address2(this.address2Address2)
					.address3(this.address2Address3)
					.isActive(true)
					.createdBy(renewalEntity.getCreatedBy())
					.createdDate(new Date())
					.build());
		}
		if(this.address3AddressTypeId != null) {
			entities.add(RenewalPolicyTMPMemberAddressEntity.builder()
					.member(renewalEntity)
					.addressTypeId(this.address3AddressTypeId)
					.pinCode(this.address3PinCode)
					.country(this.address3Country)
					.stateName(this.address3State)
					.district(this.address3District)
					.city(this.address3City)
					.address3(this.address3Address3)
					.address3(this.address3Address3)
					.address3(this.address3Address3)
					.isActive(true)
					.createdBy(renewalEntity.getCreatedBy())
					.createdDate(new Date())
					.build());
		}
		
		return entities;
	}

	
	
	private Set<RenewalPolicyTMPMemberAppointeeEntity> toRenewalMemberAppointees(RenewalPolicyTMPMemberEntity renewalEntity) {
		Set<RenewalPolicyTMPMemberAppointeeEntity> entities = new HashSet<>();
		RenewalPolicyTMPMemberNomineeEntity memberNomineeEntity1 = new RenewalPolicyTMPMemberNomineeEntity();
		if (this.appointee1MemberNominee != null && this.nominee1Name != null) {
			if (this.appointee1MemberNominee.equals(this.nominee1Name)) {
				memberNomineeEntity1 = toRenewalMemberNominee(renewalEntity, 1);
			}
		}
		if (this.appointee1MemberNominee != null && this.nominee2Name != null) {
			if (this.appointee1MemberNominee.equals(this.nominee2Name)) {
				memberNomineeEntity1 = toRenewalMemberNominee(renewalEntity, 2);
			}
		}
		if (this.appointee1MemberNominee != null && this.nominee3Name != null) {
			if (this.appointee1MemberNominee.equals(this.nominee3Name)) {
				memberNomineeEntity1 = toRenewalMemberNominee(renewalEntity, 3);
			}
		}
		if(memberNomineeEntity1.getNomineeName() != null) {
			entities.add(RenewalPolicyTMPMemberAppointeeEntity.builder()
					.member(renewalEntity)
					.nominee(memberNomineeEntity1)
					.code(this.appointee1Code)
					.name(this.appointee1Name)
					.relationship(this.appointee1RelationShip)
					.contactNumber(this.appointee1ContactNumber)
					.dateOfBirth(this.appointee1DateOfBirth)
					.panNumber(this.appointee1PanNumber)
					.aadharNumber(this.appointee1AadharNumber)
					.bankAccountNumber(this.appointee1BankAccountNumber)
					.accountType(this.appointee1AccountType)
					.ifscCode(this.appointee1IfcsCode)
					.bankName(this.appointee1BankName)
					.percentage(this.appointee1Percentage)
					.isActive(true)
					.createdBy(renewalEntity.getCreatedBy())
					.createdDate(new Date())
					.build());
		}
		
		RenewalPolicyTMPMemberNomineeEntity memberNomineeEntity2 = new RenewalPolicyTMPMemberNomineeEntity();
		if (this.appointee2MemberNominee != null && this.nominee1Name != null) {
			if (this.appointee2MemberNominee.equals(this.nominee1Name)) {
				memberNomineeEntity2 = toRenewalMemberNominee(renewalEntity, 1);
			}
		}
		if (this.appointee2MemberNominee != null && this.nominee2Name != null) {
			if (this.appointee2MemberNominee.equals(this.nominee2Name)) {
				memberNomineeEntity2 = toRenewalMemberNominee(renewalEntity, 2);
			}
		}
		if (this.appointee2MemberNominee != null && this.nominee3Name != null) {
			if (this.appointee2MemberNominee.equals(this.nominee3Name)) {
				memberNomineeEntity2 = toRenewalMemberNominee(renewalEntity, 3);
			}
		}
		if(memberNomineeEntity2.getNomineeName() != null) {
			entities.add(RenewalPolicyTMPMemberAppointeeEntity.builder()
					.member(renewalEntity)
					.nominee(memberNomineeEntity2)
					.code(this.appointee2Code)
					.name(this.appointee2Name)
					.relationship(this.appointee2RelationShip)
					.contactNumber(this.appointee2ContactNumber)
					.dateOfBirth(this.appointee2DateOfBirth)
					.panNumber(this.appointee2PanNumber)
					.aadharNumber(this.appointee2AadharNumber)
					.bankAccountNumber(this.appointee2BankAccountNumber)
					.accountType(this.appointee2AccountType)
					.ifscCode(this.appointee2IfcsCode)
					.bankName(this.appointee2BankName)
					.percentage(this.appointee2Percentage)
					.isActive(true)
					.createdBy(renewalEntity.getCreatedBy())
					.createdDate(new Date())
					.build());
		}
		
		RenewalPolicyTMPMemberNomineeEntity memberNomineeEntity3 = new RenewalPolicyTMPMemberNomineeEntity();
		if (this.appointee3MemberNominee != null && this.nominee1Name != null) {
			if (this.appointee3MemberNominee.equals(this.nominee1Name)) {
				memberNomineeEntity3 = toRenewalMemberNominee(renewalEntity, 1);
			}
		}
		if (this.appointee3MemberNominee != null && this.nominee2Name != null) {
			if (this.appointee3MemberNominee.equals(this.nominee2Name)) {
				memberNomineeEntity3 = toRenewalMemberNominee(renewalEntity, 2);
			}
		}
		if (this.appointee3MemberNominee != null && this.nominee3Name != null) {
			if (this.appointee3MemberNominee.equals(this.nominee3Name)) {
				memberNomineeEntity3 = toRenewalMemberNominee(renewalEntity, 3);
			}
		}
		if(memberNomineeEntity3.getNomineeName() != null) {
			entities.add(RenewalPolicyTMPMemberAppointeeEntity.builder()
					.member(renewalEntity)
					.nominee(memberNomineeEntity3)
					.code(this.appointee3Code)
					.name(this.appointee3Name)
					.relationship(this.appointee3RelationShip)
					.contactNumber(this.appointee3ContactNumber)
					.dateOfBirth(this.appointee3DateOfBirth)
					.panNumber(this.appointee3PanNumber)
					.aadharNumber(this.appointee3AadharNumber)
					.bankAccountNumber(this.appointee3BankAccountNumber)
					.accountType(this.appointee3AccountType)
					.ifscCode(this.appointee3IfcsCode)
					.bankName(this.appointee3BankName)
					.percentage(this.appointee3Percentage)
					.isActive(true)
					.createdBy(renewalEntity.getCreatedBy())
					.createdDate(new Date())
					.build());
		}
		return entities;
	}
	
	
	private Set<RenewalPolicyTMPMemberNomineeEntity> toRenewalMemberNominees(RenewalPolicyTMPMemberEntity renewalEntity) {
		Set<RenewalPolicyTMPMemberNomineeEntity> entities = new HashSet<>();
		if (!this.nominee1AssociatedWithAppointee) {
			if(this.nominee1Name  != null) {
				entities.add(RenewalPolicyTMPMemberNomineeEntity.builder()
						.member(renewalEntity)
						.nomineeName(this.nominee1Name)
						.relationship(this.nominee1RelationShip)
						.dateOfBirth(this.nominee1DateOfBirth)
						.nomineeAadharNumber(this.nominee1AadharNumber)
						.percentage(this.nominee1Percentage)
						.isActive(true)
						.createdBy(renewalEntity.getCreatedBy())
						.createdDate(new Date())
						.build());
			}
			
		}
		if (!this.nominee2AssociatedWithAppointee) {
			if(this.nominee2Name  != null) {
				entities.add(RenewalPolicyTMPMemberNomineeEntity.builder()
						.member(renewalEntity)
						.member(renewalEntity)
						.nomineeName(this.nominee1Name)
						.relationship(this.nominee1RelationShip)
						.dateOfBirth(this.nominee1DateOfBirth)
						.nomineeAadharNumber(this.nominee1AadharNumber)
						.percentage(this.nominee1Percentage)
						.isActive(true)
						.createdBy(renewalEntity.getCreatedBy())
						.createdDate(new Date())
						.build());
			}
		}
		if (!this.nominee3AssociatedWithAppointee) {
			if(this.nominee3Name  != null) {
				entities.add(RenewalPolicyTMPMemberNomineeEntity.builder()
						.member(renewalEntity)
						.nomineeName(this.nominee1Name)
						.relationship(this.nominee1RelationShip)
						.dateOfBirth(this.nominee1DateOfBirth)
						.nomineeAadharNumber(this.nominee1AadharNumber)
						.percentage(this.nominee1Percentage)
						.isActive(true)
						.createdBy(renewalEntity.getCreatedBy())
						.createdDate(new Date())
						.build());
			}
		
		}
		return entities;
	}

	private Set<RenewalPolicyTMPMemberBankAccountEntity> toRenewalMemberBankAccounts(
			RenewalPolicyTMPMemberEntity renewalEntity) {
		Set<RenewalPolicyTMPMemberBankAccountEntity> entities = new HashSet<>();
		if(this.bank1BankAccountNumber != null) {
			entities.add(RenewalPolicyTMPMemberBankAccountEntity.builder()
					.member(renewalEntity)
					.bankAccountNumber(this.bank1BankAccountNumber)
					.accountType(this.bank1AccountType)
					.ifscCode(this.bank1IfcsCode)
					.bankName(this.bank1BankName)
					.bankBranch(this.bank1BankBranch)
					.isActive(true)
					.createdBy(renewalEntity.getCreatedBy())
					.createdDate(new Date())
					.build());
		}
		if(this.bank2BankAccountNumber != null) {
			entities.add(RenewalPolicyTMPMemberBankAccountEntity.builder()
					.member(renewalEntity)
					.bankAccountNumber(this.bank2BankAccountNumber)
					.accountType(this.bank1AccountType)
					.ifscCode(this.bank2IfcsCode)
					.bankName(this.bank2BankName)
					.bankBranch(this.bank2BankBranch)
					.isActive(true)
					.createdBy(renewalEntity.getCreatedBy())
					.createdDate(new Date())
					.build());
		}
		if(this.bank3BankAccountNumber != null) {
			entities.add(RenewalPolicyTMPMemberBankAccountEntity.builder()
					.member(renewalEntity)
					.bankAccountNumber(this.bank3BankAccountNumber)
					.accountType(this.bank3AccountType)
					.ifscCode(this.bank3IfcsCode)
					.bankName(this.bank3BankName)
					.bankBranch(this.bank3BankBranch)
					.isActive(true)
					.createdBy(renewalEntity.getCreatedBy())
					.createdDate(new Date())
					.build());
		}
		return entities;
	}


	private RenewalPolicyTMPMemberNomineeEntity toRenewalMemberNominee(RenewalPolicyTMPMemberEntity renewalEntity, int index) {
		if (index == 1) {
			this.nominee1AssociatedWithAppointee=true;
			return RenewalPolicyTMPMemberNomineeEntity.builder()
					.member(renewalEntity)
					.nomineeName(this.nominee1Name)
					.relationship(this.nominee1RelationShip)
					.dateOfBirth(this.nominee1DateOfBirth)
					.nomineeAadharNumber(this.nominee1AadharNumber)
					.percentage(this.nominee1Percentage)
					.isActive(true)
					.createdBy(renewalEntity.getCreatedBy())
					.createdDate(new Date())
					.build();
		}
		if (index == 2) {
			this.nominee2AssociatedWithAppointee=true;
			return RenewalPolicyTMPMemberNomineeEntity.builder()
					.member(renewalEntity)
					.nomineeName(this.nominee1Name)
					.relationship(this.nominee1RelationShip)
					.dateOfBirth(this.nominee1DateOfBirth)
					.nomineeAadharNumber(this.nominee1AadharNumber)
					.percentage(this.nominee1Percentage)
					.isActive(true)
					.createdBy(renewalEntity.getCreatedBy())
					.createdDate(new Date())
					.build();		}
		if (index == 3) {
			this.nominee3AssociatedWithAppointee=true;
			return RenewalPolicyTMPMemberNomineeEntity.builder()
					.member(renewalEntity)
					.nomineeName(this.nominee1Name)
					.relationship(this.nominee1RelationShip)
					.dateOfBirth(this.nominee1DateOfBirth)
					.nomineeAadharNumber(this.nominee1AadharNumber)
					.percentage(this.nominee1Percentage)
					.isActive(true)
					.createdBy(renewalEntity.getCreatedBy())
					.createdDate(new Date())
					.build();
		}
		return null;
	}
	
}
