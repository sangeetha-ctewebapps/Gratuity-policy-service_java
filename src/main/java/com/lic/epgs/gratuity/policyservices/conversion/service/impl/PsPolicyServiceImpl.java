package com.lic.epgs.gratuity.policyservices.conversion.service.impl;

import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lic.epgs.gratuity.policy.member.entity.PolicyMemberAddressEntity;
import com.lic.epgs.gratuity.policy.member.entity.PolicyMemberAppointeeEntity;
import com.lic.epgs.gratuity.policy.member.entity.PolicyMemberBankAccount;
import com.lic.epgs.gratuity.policy.member.entity.PolicyMemberEntity;
import com.lic.epgs.gratuity.policy.member.entity.PolicyMemberNomineeEntity;
import com.lic.epgs.gratuity.policy.member.entity.TempMemberAddressEntity;
import com.lic.epgs.gratuity.policy.member.entity.TempMemberAppointeeEntity;
import com.lic.epgs.gratuity.policy.member.entity.TempMemberBankAccountEntity;
import com.lic.epgs.gratuity.policy.member.entity.TempMemberEntity;
import com.lic.epgs.gratuity.policy.member.entity.TempMemberNomineeEntity;
import com.lic.epgs.gratuity.policy.member.repository.PolicyMemberRepository;
import com.lic.epgs.gratuity.policy.member.repository.TempMemberAddressRepository;
import com.lic.epgs.gratuity.policy.member.repository.TempMemberAppointeeRepository;
import com.lic.epgs.gratuity.policy.member.repository.TempMemberBankAccountRepository;
import com.lic.epgs.gratuity.policy.member.repository.TempMemberRepository;
import com.lic.epgs.gratuity.policy.repository.MasterPolicyRepository;
import com.lic.epgs.gratuity.policyservices.conversion.repository.PsPolicyMemberAddressRepository;
import com.lic.epgs.gratuity.policyservices.conversion.repository.PsPolicyMemberAppointeeRepository;
import com.lic.epgs.gratuity.policyservices.conversion.repository.PsPolicyMemberBankAccountRepository;
import com.lic.epgs.gratuity.policyservices.conversion.repository.PsPolicyMemberNomineeRepository;
import com.lic.epgs.gratuity.policyservices.conversion.repository.PsTempMemberNomineeEntityRepository;
import com.lic.epgs.gratuity.policyservices.conversion.service.PsPolicyService;

@Service
public class PsPolicyServiceImpl implements PsPolicyService {
	@Autowired
	private MasterPolicyRepository masterPolicyRepository;
	@Autowired
	private PolicyMemberRepository policyMemberRepository;
	@Autowired
	private PsPolicyMemberAddressRepository psPolicyMemberAddressRepository;
	@Autowired
	PsPolicyMemberAppointeeRepository psPolicyMemberAppointeeRepository;
	@Autowired
	private PsPolicyMemberBankAccountRepository psPolicyMemberBankAccountRepository;
	@Autowired
	private PsPolicyMemberNomineeRepository psPsPolicyMemberNomineeRepository;

	@Autowired
	private TempMemberRepository pmstTempMember;
	@Autowired
	private TempMemberAddressRepository tempMemberAddressRepository;
	@Autowired
	private TempMemberAppointeeRepository tempMemberAppointeeRepository;
	@Autowired
	private TempMemberBankAccountRepository tempMemberBankAccountRepository;
	@Autowired
	private PsTempMemberNomineeEntityRepository psTempMemberNomineeEntityRepository;

	@Override
	public void savePolicyDataMasterToTemp(Long policyId, Long tempPolicyId) {
		savePmstTmpMember(policyId, tempPolicyId);
	}

	public void savePmstTmpMember(Long policyId, Long tempPolicyId) {
		List<PolicyMemberEntity> policyMemberList = policyMemberRepository.findByPolicyId(policyId);
		try {

			for (Iterator policyItr = policyMemberList.iterator(); policyItr.hasNext();) {
				PolicyMemberEntity memberEntity = (PolicyMemberEntity) policyItr.next();

				TempMemberEntity tempMemberEntity = new TempMemberEntity();
				
				tempMemberEntity.setPolicyId(policyId);
				tempMemberEntity.setTmpPolicyId(tempPolicyId);

				tempMemberEntity.setProposalPolicyNumber(memberEntity.getProposalPolicyNumber());
				tempMemberEntity.setPmstMemberId(memberEntity.getId());
				tempMemberEntity.setLicId(memberEntity.getLicId());
				tempMemberEntity.setEmployeeCode(memberEntity.getEmployeeCode());
				tempMemberEntity.setFirstName(memberEntity.getFirstName());
				tempMemberEntity.setLastName(memberEntity.getLastName());
				tempMemberEntity.setMiddleName(memberEntity.getMiddleName());
				tempMemberEntity.setDateOfBirth(memberEntity.getDateOfBirth());
				tempMemberEntity.setGenderId(memberEntity.getGenderId());
				tempMemberEntity.setPanNumber(memberEntity.getPanNumber());
				tempMemberEntity.setAadharNumber(memberEntity.getAadharNumber());
				tempMemberEntity.setLandlineNo(memberEntity.getLandlineNo());
				tempMemberEntity.setMobileNo(memberEntity.getMobileNo());
				tempMemberEntity.setDateOfAppointment(memberEntity.getDateOfAppointment());
				tempMemberEntity.setDojToScheme(memberEntity.getDojToScheme());
				tempMemberEntity.setCategoryId(memberEntity.getCategoryId());
				tempMemberEntity.setSalaryFrequency(memberEntity.getSalaryFrequency());
				tempMemberEntity.setDesignation(memberEntity.getDesignation());
				tempMemberEntity.setBasicSalary(memberEntity.getBasicSalary());
				tempMemberEntity.setFatherName(memberEntity.getFatherName());
				tempMemberEntity.setSpouseName(memberEntity.getSpouseName());
				tempMemberEntity.setStatusId(memberEntity.getStatusId());
				tempMemberEntity.setLifeCoverPremium(memberEntity.getLifeCoverPremium());
				tempMemberEntity.setLifeCoverSumAssured(memberEntity.getLifeCoverSumAssured());
				tempMemberEntity.setTotalGratuity(memberEntity.getTotalGratuity());
				tempMemberEntity.setGratCalculationId(memberEntity.getGratCalculationId());
				tempMemberEntity.setValuationDate(memberEntity.getValuationDate());
				tempMemberEntity.setCommunicationPreference(memberEntity.getCommunicationPreference());
				tempMemberEntity.setLanguagePreference(memberEntity.getLanguagePreference());
				tempMemberEntity.setIsActive(memberEntity.getIsActive());
				tempMemberEntity.setCreatedBy(memberEntity.getCreatedBy());
				tempMemberEntity.setCreatedDate(memberEntity.getCreatedDate());
				tempMemberEntity.setModifiedBy(memberEntity.getModifiedBy());
				tempMemberEntity.setModifiedDate(new Date());
				
				// SET ADDRESS DETAILS
				List<PolicyMemberAddressEntity> policyMemberAddressEntity = psPolicyMemberAddressRepository
						.findAllByMemberId(memberEntity.getId());
				Set<TempMemberAddressEntity> setTempMemberAddressEntity = new HashSet<>();
				for (Iterator addItr = policyMemberAddressEntity.iterator(); addItr.hasNext();) {
					PolicyMemberAddressEntity memberAddressEntity = (PolicyMemberAddressEntity) addItr.next();

					TempMemberAddressEntity tempMemberAddressEntity = new TempMemberAddressEntity();
					
					tempMemberAddressEntity.setTempMember(tempMemberEntity);
					tempMemberAddressEntity.setAddressTypeId(memberAddressEntity.getAddressTypeId());
					tempMemberAddressEntity.setPinCode(memberAddressEntity.getPinCode());
					tempMemberAddressEntity.setCountry(memberAddressEntity.getCountry());
					tempMemberAddressEntity.setStateName(memberAddressEntity.getStateName());
					tempMemberAddressEntity.setDistrict(memberAddressEntity.getDistrict());
					tempMemberAddressEntity.setCity(memberAddressEntity.getCity());
					tempMemberAddressEntity.setAddress1(memberAddressEntity.getAddress1());
					tempMemberAddressEntity.setAddress2(memberAddressEntity.getAddress2());
					tempMemberAddressEntity.setAddress3(memberAddressEntity.getAddress3());
					tempMemberAddressEntity.setIsActive(memberAddressEntity.getIsActive());
					tempMemberAddressEntity.setCreatedBy(memberAddressEntity.getCreatedBy());
					tempMemberAddressEntity.setCreatedDate(memberAddressEntity.getCreatedDate());
					tempMemberAddressEntity.setModifiedBy(memberAddressEntity.getModifiedBy());
					tempMemberAddressEntity.setModifiedDate(memberAddressEntity.getModifiedDate());
					tempMemberAddressEntity.setModifiedDate(new Date());
					tempMemberAddressEntity.setPmstMemberAddressId(memberAddressEntity.getId());
					// tempMemberAddressEntity.setRecordType(policyId);

					setTempMemberAddressEntity.add(tempMemberAddressEntity);
				}
				tempMemberEntity.setAddresses(setTempMemberAddressEntity);	
				
				// SET APPOINTEE DETAILS
				List<PolicyMemberAppointeeEntity> policyMemberAppointeeEntity = psPolicyMemberAppointeeRepository
						.findAllByMemberId(memberEntity.getId());
				Set<TempMemberAppointeeEntity> setTempMemberAppointeeEntity = new HashSet<>();
				for (Iterator appointeeItr = policyMemberAppointeeEntity.iterator(); appointeeItr.hasNext();) {
					PolicyMemberAppointeeEntity memberAppointeeEntity = (PolicyMemberAppointeeEntity) appointeeItr.next();
					TempMemberAppointeeEntity tempMemberAppointeeEntity = new TempMemberAppointeeEntity();

					tempMemberAppointeeEntity.setTempMember(tempMemberEntity);
					tempMemberAppointeeEntity.setCode(memberAppointeeEntity.getCode());
					tempMemberAppointeeEntity.setName(memberAppointeeEntity.getName());
					tempMemberAppointeeEntity.setRelationship(memberAppointeeEntity.getRelationship());
					tempMemberAppointeeEntity.setContactNumber(memberAppointeeEntity.getContactNumber());
					tempMemberAppointeeEntity.setDateOfBirth(memberAppointeeEntity.getDateOfBirth());
					tempMemberAppointeeEntity.setPanNumber(memberAppointeeEntity.getPanNumber());
					tempMemberAppointeeEntity.setAadharNumber(memberAppointeeEntity.getAadharNumber());
					tempMemberAppointeeEntity.setBankAccountNumber(memberAppointeeEntity.getBankAccountNumber());
					tempMemberAppointeeEntity.setAccountType(memberAppointeeEntity.getAccountType());
					tempMemberAppointeeEntity.setIfscCode(memberAppointeeEntity.getIfscCode());
					tempMemberAppointeeEntity.setBankName(memberAppointeeEntity.getBankName());

					tempMemberAppointeeEntity.setCreatedBy(memberAppointeeEntity.getCreatedBy());
					tempMemberAppointeeEntity.setCreatedDate(memberAppointeeEntity.getCreatedDate());
					// tempMemberAppointeeEntity.set(memberAppointeeEntity.);
					// tempMemberAppointeeEntity.setCreatedDate(memberAppointeeEntity.);
					// tempMemberAppointeeEntity.setPmstMemberAppointeeId(memberAppointeeEntity.get);
					// tempMemberAppointeeEntity.setRecordType(memberAppointeeEntity.get);
					tempMemberAppointeeEntity.setBankBranch(memberAppointeeEntity.getBankBranch());
					
					setTempMemberAppointeeEntity.add(tempMemberAppointeeEntity);
				}
				tempMemberEntity.setAppointees(setTempMemberAppointeeEntity);
				
				List<PolicyMemberBankAccount> policyMemberBankAccountList = psPolicyMemberBankAccountRepository
						.findAllByMemberId(memberEntity.getId());
				Set<TempMemberBankAccountEntity> setTempMemberBankAccountEntity =new HashSet<>();
				
				for (Iterator bankAcItr = policyMemberBankAccountList.iterator(); bankAcItr.hasNext();) {
					PolicyMemberBankAccount memberBankAccount = (PolicyMemberBankAccount) bankAcItr.next();
					TempMemberBankAccountEntity tempMemberBankAccountEntity = new TempMemberBankAccountEntity();
					
					tempMemberBankAccountEntity.setTempMember(tempMemberEntity);
					tempMemberBankAccountEntity.setBankName(memberBankAccount.getBankName());
					tempMemberBankAccountEntity.setBankAccountNumber(memberBankAccount.getBankAccountNumber());
					tempMemberBankAccountEntity.setAccountType(memberBankAccount.getAccountType());
					tempMemberBankAccountEntity.setBankBranch(memberBankAccount.getBankBranch());
					tempMemberBankAccountEntity.setBankAddress(memberBankAccount.getBankAddress());
					tempMemberBankAccountEntity.setCountryCode(memberBankAccount.getCountryCode());
					tempMemberBankAccountEntity.setStdCode(memberBankAccount.getStdCode());
					tempMemberBankAccountEntity.setIfscCodeAvailable(memberBankAccount.getIfscCodeAvailable());
					tempMemberBankAccountEntity.setIfscCode(memberBankAccount.getIfscCode());
					tempMemberBankAccountEntity.setEmailId(memberBankAccount.getEmailId());
					tempMemberBankAccountEntity.setLandlineNumber(memberBankAccount.getLandlineNumber());
					tempMemberBankAccountEntity.setIsActive(memberBankAccount.getIsActive());
					tempMemberBankAccountEntity.setCreatedBy(memberBankAccount.getCreatedBy());
					tempMemberBankAccountEntity.setCreatedDate(memberBankAccount.getCreatedDate());
					tempMemberBankAccountEntity.setModifiedBy(memberBankAccount.getModifiedBy());
					tempMemberBankAccountEntity.setModifiedDate(new Date());
					tempMemberBankAccountEntity.setPmstMemberBankAccountId(memberBankAccount.getId());
					// tempMemberBankAccountEntity.setRecordType(memberBankAccount.gett);
					setTempMemberBankAccountEntity.add(tempMemberBankAccountEntity);
				}
				tempMemberEntity.setBankAccounts(setTempMemberBankAccountEntity);
				
				List<PolicyMemberNomineeEntity> listPolicyMemberNomineeEntity = psPsPolicyMemberNomineeRepository
						.findAllByMemberId(memberEntity.getId());
				Set<TempMemberNomineeEntity> setTempMemberNomineeEntity = new HashSet<>();
				for (Iterator nomineeItr  = listPolicyMemberNomineeEntity.iterator(); nomineeItr.hasNext();) {
					PolicyMemberNomineeEntity memberNomineeEntity = (PolicyMemberNomineeEntity) nomineeItr.next();
					TempMemberNomineeEntity tempMemberNomineeEntity = new TempMemberNomineeEntity();

					tempMemberNomineeEntity.setNomineeName(memberNomineeEntity.getNomineeName());
					tempMemberNomineeEntity.setRelationship(memberNomineeEntity.getRelationship());
					tempMemberNomineeEntity.setAddressOne(memberNomineeEntity.getAddressOne());
					tempMemberNomineeEntity.setAddressTwo(memberNomineeEntity.getAddressTwo());
					tempMemberNomineeEntity.setAddressThree(memberNomineeEntity.getAddressThree());
					tempMemberNomineeEntity.setNomineeDistrict(memberNomineeEntity.getNomineeDistrict());
					tempMemberNomineeEntity.setNomineeState(memberNomineeEntity.getNomineeState());
					tempMemberNomineeEntity.setNomineeCountry(memberNomineeEntity.getNomineeCountry());
					tempMemberNomineeEntity.setNomineePincode(memberNomineeEntity.getNomineePincode());
					tempMemberNomineeEntity.setNomineeEmailId(memberNomineeEntity.getNomineeEmailId());
					tempMemberNomineeEntity.setNomineePhone(memberNomineeEntity.getNomineePhone());
					tempMemberNomineeEntity.setDateOfBirth(memberNomineeEntity.getDateOfBirth());
					tempMemberNomineeEntity.setAge(memberNomineeEntity.getAge());
					tempMemberNomineeEntity.setNomineeAadharNumber(memberNomineeEntity.getNomineeAadharNumber());
					tempMemberNomineeEntity.setPercentage(memberNomineeEntity.getPercentage());
					tempMemberNomineeEntity.setIsActive(memberNomineeEntity.getIsActive());
					tempMemberNomineeEntity.setCreatedBy(memberNomineeEntity.getCreatedBy());
					tempMemberNomineeEntity.setCreatedDate(memberNomineeEntity.getCreatedDate());
					tempMemberNomineeEntity.setModifiedBy(memberNomineeEntity.getModifiedBy());
					tempMemberNomineeEntity.setModifiedDate(new Date());
					tempMemberNomineeEntity.setPmstMemberNomineeId(memberNomineeEntity.getId());
					tempMemberNomineeEntity.setGenderId(memberNomineeEntity.getGenderId());
					
					tempMemberNomineeEntity.setTempMember(tempMemberEntity);
					// tempMemberNomineeEntity.setPanNumber(memberNomineeEntity.);
					// tempMemberNomineeEntity.setRecordType(memberNomineeEntity.getR);

					//psTempMemberNomineeEntityRepository.save(tempMemberNomineeEntity);
					setTempMemberNomineeEntity.add(tempMemberNomineeEntity);
				}
				tempMemberEntity.setNominees(setTempMemberNomineeEntity);
				
				pmstTempMember.save(tempMemberEntity);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
