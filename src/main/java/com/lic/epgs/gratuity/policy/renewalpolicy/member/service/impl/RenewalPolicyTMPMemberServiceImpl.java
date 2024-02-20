package com.lic.epgs.gratuity.policy.renewalpolicy.member.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lic.epgs.gratuity.common.dto.ApiResponseDto;
import com.lic.epgs.gratuity.policy.renewalpolicy.member.dto.RenewalMemberImportDto;
import com.lic.epgs.gratuity.policy.renewalpolicy.member.dto.RenewalPolicyTMPMemberAddressDto;
import com.lic.epgs.gratuity.policy.renewalpolicy.member.dto.RenewalPolicyTMPMemberAppointeeDto;
import com.lic.epgs.gratuity.policy.renewalpolicy.member.dto.RenewalPolicyTMPMemberBankAccountDto;
import com.lic.epgs.gratuity.policy.renewalpolicy.member.dto.RenewalPolicyTMPMemberDto;
import com.lic.epgs.gratuity.policy.renewalpolicy.member.dto.RenewalPolicyTMPMemberNomineeDto;
import com.lic.epgs.gratuity.policy.renewalpolicy.member.entity.RenewalPolicyTMPMemberAddressEntity;
import com.lic.epgs.gratuity.policy.renewalpolicy.member.entity.RenewalPolicyTMPMemberAppointeeEntity;
import com.lic.epgs.gratuity.policy.renewalpolicy.member.entity.RenewalPolicyTMPMemberBankAccountEntity;
import com.lic.epgs.gratuity.policy.renewalpolicy.member.entity.RenewalPolicyTMPMemberEntity;
import com.lic.epgs.gratuity.policy.renewalpolicy.member.entity.RenewalPolicyTMPMemberNomineeEntity;
import com.lic.epgs.gratuity.policy.renewalpolicy.member.helper.RenewalPolicyTMPMemberHelper;
import com.lic.epgs.gratuity.policy.renewalpolicy.member.repository.RenewalPolicyTMPMemberAddressRepository;
import com.lic.epgs.gratuity.policy.renewalpolicy.member.repository.RenewalPolicyTMPMemberAppointeeRepository;
import com.lic.epgs.gratuity.policy.renewalpolicy.member.repository.RenewalPolicyTMPMemberBankRepository;
import com.lic.epgs.gratuity.policy.renewalpolicy.member.repository.RenewalPolicyTMPMemberNomineeRepository;
import com.lic.epgs.gratuity.policy.renewalpolicy.member.repository.RenewalPolicyTMPMemberRepository;
import com.lic.epgs.gratuity.policy.renewalpolicy.member.service.RenewalTMPMemberService;
import com.lic.epgs.gratuity.quotation.member.dto.MemberBatchDto;
import com.lic.epgs.gratuity.quotation.member.dto.MemberBulkResponseDto;
import com.lic.epgs.gratuity.quotation.member.helper.BulkMemberUploadHelper;

@Service
public class RenewalPolicyTMPMemberServiceImpl implements RenewalTMPMemberService {

	@Autowired
	private RenewalPolicyTMPMemberRepository renewalPolicyTMPMemberRepository;

	@Autowired
	private RenewalPolicyTMPMemberNomineeRepository renewalPolicyTMPMemberNomineeRepository;

	@Autowired
	private BulkMemberUploadHelper memberHelper;

	@Autowired
	private RenewalPolicyTMPMemberAddressRepository renewalPolicyTMPMemberAddressRepository;

	@Autowired
	private RenewalPolicyTMPMemberBankRepository renewalPolicyTMPMemberBankRepository;

	@Autowired
	private RenewalPolicyTMPMemberAppointeeRepository renewalPolicyTMPMemberAppointeeRepository;

	@Override
	public ApiResponseDto<List<RenewalPolicyTMPMemberDto>> memberViewDetail(Long tmpPolicyId) {

		List<RenewalPolicyTMPMemberEntity> renewalPolicyTMPMemberEntities = renewalPolicyTMPMemberRepository
				.findBytmpPolicyId(tmpPolicyId);

		return ApiResponseDto.success(renewalPolicyTMPMemberEntities.stream()
				.map(RenewalPolicyTMPMemberHelper::entityToDto).collect(Collectors.toList()));
	}

	@Override
	public ApiResponseDto<RenewalPolicyTMPMemberDto> MemberDetail(Long memberId) {

		RenewalPolicyTMPMemberEntity renewalPolicyTMPMemberEntity = renewalPolicyTMPMemberRepository.findById(memberId)
				.get();
		return ApiResponseDto.success(RenewalPolicyTMPMemberHelper.entityToDto(renewalPolicyTMPMemberEntity));
	}

	@Transactional
	@Override
	public ApiResponseDto<MemberBulkResponseDto> importMemberData(RenewalMemberImportDto renewalMemberImportDto) {

		memberHelper.saveMembersFromStgMember(renewalMemberImportDto);

		return ApiResponseDto.success(new MemberBulkResponseDto());
	}

	@Override
	public ApiResponseDto<MemberBatchDto> memberTotalCount(Long tempPolicyId) {
		MemberBatchDto memberBatchDto = new MemberBatchDto();
		memberBatchDto
				.setMemberImportCount(Long.toString(renewalPolicyTMPMemberRepository.numberMemberCount(tempPolicyId)));
		return ApiResponseDto.success(memberBatchDto);

	}

	@Override
	public ApiResponseDto<RenewalPolicyTMPMemberDto> getMemberDetailNominee(
			RenewalPolicyTMPMemberNomineeDto renewalPolicyTMPMemberNomineeDto, Long memberId) {

		RenewalPolicyTMPMemberEntity renewalPolicyTMPMemberEntity = renewalPolicyTMPMemberRepository.findById(memberId)
				.get();

		RenewalPolicyTMPMemberNomineeEntity renewalPolicyTMPMemberNomineeEntity = RenewalPolicyTMPMemberHelper
				.nomineeDtotoEntity(renewalPolicyTMPMemberNomineeDto);
		List<RenewalPolicyTMPMemberNomineeEntity> getmemberNominee = new ArrayList<RenewalPolicyTMPMemberNomineeEntity>();
		renewalPolicyTMPMemberNomineeEntity.setCreatedDate(new Date());
		renewalPolicyTMPMemberNomineeEntity.setCreatedBy(renewalPolicyTMPMemberNomineeDto.getCreatedBy());
		renewalPolicyTMPMemberNomineeEntity.setIsActive(true);
		renewalPolicyTMPMemberNomineeEntity.setMember(renewalPolicyTMPMemberEntity);
		getmemberNominee.add(renewalPolicyTMPMemberNomineeEntity);
		getmemberNominee.addAll(renewalPolicyTMPMemberEntity.getNominees());
		getmemberNominee = renewalPolicyTMPMemberNomineeRepository.saveAll(getmemberNominee);

		Set<RenewalPolicyTMPMemberNomineeEntity> setMemberNominee = new HashSet<>(getmemberNominee);
		renewalPolicyTMPMemberEntity.setNominees(setMemberNominee);
		return ApiResponseDto.created(RenewalPolicyTMPMemberHelper.entityToDto(renewalPolicyTMPMemberRepository.findById(renewalPolicyTMPMemberEntity.getId()).get()));
	}

	@Override
	public ApiResponseDto<RenewalPolicyTMPMemberDto> uploaTempdMemberBank(
			RenewalPolicyTMPMemberBankAccountDto renewalPolicyTMPMemberBankAccountDto, Long memberId) {

		RenewalPolicyTMPMemberEntity memberEntity = renewalPolicyTMPMemberRepository.findById(memberId).get();

		RenewalPolicyTMPMemberBankAccountEntity memberBankAccount = RenewalPolicyTMPMemberHelper
				.bankDtoToEntity(renewalPolicyTMPMemberBankAccountDto);

		List<RenewalPolicyTMPMemberBankAccountEntity> getMemberBankAccount = new ArrayList<RenewalPolicyTMPMemberBankAccountEntity>();

		memberBankAccount.setCreatedDate(new Date());
		memberBankAccount.setCreatedBy(renewalPolicyTMPMemberBankAccountDto.getCreatedBy());
		memberBankAccount.setIsActive(true);

		memberBankAccount.setMember(memberEntity);

		getMemberBankAccount.add(memberBankAccount);

		getMemberBankAccount.addAll(memberEntity.getBankAccounts());

		getMemberBankAccount = renewalPolicyTMPMemberBankRepository.saveAll(getMemberBankAccount);
		
		Set<RenewalPolicyTMPMemberBankAccountEntity> setMemberBankAccount = new HashSet<>(getMemberBankAccount);


		memberEntity.setBankAccounts(setMemberBankAccount);
		return ApiResponseDto.created(RenewalPolicyTMPMemberHelper.entityToDto(renewalPolicyTMPMemberRepository.findById(memberEntity.getId()).get()));
	}

	@Override
	public ApiResponseDto<RenewalPolicyTMPMemberDto> uploadTempMemberAppointee(
			RenewalPolicyTMPMemberAppointeeDto renewalPolicyTMPMemberAppointeeDto, Long nomineeId, Long memberId) {
		// TODO Auto-generated method stub

		RenewalPolicyTMPMemberEntity memberEntity = renewalPolicyTMPMemberRepository.findById(memberId).get();

		RenewalPolicyTMPMemberNomineeEntity memberNomineeEntity = renewalPolicyTMPMemberNomineeRepository
				.findById(nomineeId).get();

		List<RenewalPolicyTMPMemberAppointeeEntity> getMemberAppointeeEntity = new ArrayList<RenewalPolicyTMPMemberAppointeeEntity>();

		RenewalPolicyTMPMemberAppointeeEntity memberAppointeeEntity = RenewalPolicyTMPMemberHelper
				.appointeeDtotoEntity(renewalPolicyTMPMemberAppointeeDto);

		memberAppointeeEntity.setMember(memberEntity);
		memberAppointeeEntity.setNominee(memberNomineeEntity);
		memberAppointeeEntity.setCreatedDate(new Date());
		memberAppointeeEntity.setCreatedBy(renewalPolicyTMPMemberAppointeeDto.getCreatedBy());
		memberAppointeeEntity.setIsActive(true);
		getMemberAppointeeEntity.add(memberAppointeeEntity);
		getMemberAppointeeEntity.addAll(memberEntity.getAppointees());

		List<RenewalPolicyTMPMemberAppointeeEntity> tt = renewalPolicyTMPMemberAppointeeRepository.saveAll(getMemberAppointeeEntity);
		Set<RenewalPolicyTMPMemberAppointeeEntity> setMemberAppointee = new HashSet<>(tt);


		memberEntity.setAppointees(setMemberAppointee);
		return ApiResponseDto.created(RenewalPolicyTMPMemberHelper.entityToDto(renewalPolicyTMPMemberRepository.findById(memberEntity.getId()).get()));
	}


	@Override
	public ApiResponseDto<RenewalPolicyTMPMemberDto> uploadMemberAddress(
			RenewalPolicyTMPMemberAddressDto renewalPolicyTMPMemberAddressDto, Long id) {

		RenewalPolicyTMPMemberAddressEntity renewalPolicyTMPMemberAddressEntity = renewalPolicyTMPMemberAddressRepository
				.findById(id).get();

		RenewalPolicyTMPMemberAddressEntity renewalPolicyTMPMemberAddressEntity2 = new ModelMapper()
				.map(renewalPolicyTMPMemberAddressDto, RenewalPolicyTMPMemberAddressEntity.class);

		// RenewalPolicyTMPMemberAddressEntity addressDtotoEntity =
		// RenewalPolicyTMPMemberHelper.addressDtotoEntity(renewalPolicyTMPMemberAddressDto);

		renewalPolicyTMPMemberAddressEntity2.setId(id);

		renewalPolicyTMPMemberAddressEntity2.setCreatedBy(renewalPolicyTMPMemberAddressEntity.getCreatedBy());
		renewalPolicyTMPMemberAddressEntity2.setCreatedDate(renewalPolicyTMPMemberAddressEntity.getCreatedDate());		
		renewalPolicyTMPMemberAddressEntity2.setIsActive(true);
		renewalPolicyTMPMemberAddressEntity2.setMember(renewalPolicyTMPMemberAddressEntity.getMember());

		renewalPolicyTMPMemberAddressRepository.save(renewalPolicyTMPMemberAddressEntity2);
		return ApiResponseDto
				.success(RenewalPolicyTMPMemberHelper.entityToDto(renewalPolicyTMPMemberRepository.findById(renewalPolicyTMPMemberAddressEntity.getMember().getId()).get()));

	}

	@Override
	public ApiResponseDto<List<RenewalPolicyTMPMemberDto>> delete(Long id) {
		renewalPolicyTMPMemberRepository.deleteById(id);
		return ApiResponseDto.success(null);
	}

	@Transactional
	@Override
	public ApiResponseDto<List<RenewalPolicyTMPMemberAddressDto>> deleteMemberAddress(Long id) {
		renewalPolicyTMPMemberAddressRepository.deleteByAddress(id);
		return ApiResponseDto.success(null);
	}

	@Transactional
	@Override
	public ApiResponseDto<List<RenewalPolicyTMPMemberAppointeeDto>> deleteMemberAppointee(Long id) {
		renewalPolicyTMPMemberAppointeeRepository.deleteByAppointee(id);
		return ApiResponseDto.success(null);
	}

	@Transactional
	@Override
	public ApiResponseDto<List<RenewalPolicyTMPMemberBankAccountDto>> deleteMemberBankAccount(Long id) {
		renewalPolicyTMPMemberBankRepository.deleteByBankAccount(id);
		return ApiResponseDto.success(null);
	}

	@Transactional
	@Override
	public ApiResponseDto<List<RenewalPolicyTMPMemberNomineeDto>> deleteMemberNominee(Long id) {
		renewalPolicyTMPMemberNomineeRepository.deleteByNominee(id);
		return ApiResponseDto.success(null);
	}

	@Override
	public ApiResponseDto<RenewalPolicyTMPMemberDto> memberBankDetailsUpdate(Long id,
			RenewalPolicyTMPMemberBankAccountDto renewalPolicyTMPMemberBankAccountDto) {
		
			RenewalPolicyTMPMemberBankAccountEntity renewalPolicyTMPMemberBankAccountEntity = renewalPolicyTMPMemberBankRepository.findById(id).get();
			
			RenewalPolicyTMPMemberBankAccountEntity newRenewalPolicyTMPMemberBankAccountEntity=RenewalPolicyTMPMemberHelper.bankDtoToEntity(renewalPolicyTMPMemberBankAccountDto);
			
			RenewalPolicyTMPMemberEntity renewalPolicyTMPMemberEntity=renewalPolicyTMPMemberRepository.findById(renewalPolicyTMPMemberBankAccountEntity.getMember().getId()).get();	
		
			
			newRenewalPolicyTMPMemberBankAccountEntity.setId(renewalPolicyTMPMemberBankAccountEntity.getId());
			newRenewalPolicyTMPMemberBankAccountEntity.setMember(renewalPolicyTMPMemberBankAccountEntity.getMember());
			newRenewalPolicyTMPMemberBankAccountEntity.setModifiedBy(renewalPolicyTMPMemberBankAccountDto.getModifiedBy());
			newRenewalPolicyTMPMemberBankAccountEntity.setModifiedDate(new Date());		
			newRenewalPolicyTMPMemberBankAccountEntity.setCreatedBy(renewalPolicyTMPMemberBankAccountEntity.getCreatedBy());
			newRenewalPolicyTMPMemberBankAccountEntity.setCreatedDate(renewalPolicyTMPMemberBankAccountEntity.getCreatedDate());
			newRenewalPolicyTMPMemberBankAccountEntity.setIsActive(true);
			
			
			renewalPolicyTMPMemberBankRepository.save(newRenewalPolicyTMPMemberBankAccountEntity);
			
			return ApiResponseDto
					.success(RenewalPolicyTMPMemberHelper.entityToDto(renewalPolicyTMPMemberEntity));
					}

	@Override
	public ApiResponseDto<RenewalPolicyTMPMemberDto> updatememberdetailsnominee(Long id,
			RenewalPolicyTMPMemberNomineeDto renewalPolicyTMPMemberNomineeDto) {
	
		RenewalPolicyTMPMemberNomineeEntity renewalPolicyTMPMemberNomineeEntity = renewalPolicyTMPMemberNomineeRepository.findById(id).get();
		
		RenewalPolicyTMPMemberNomineeEntity newrenewalPolicyTMPMemberNomineeEntity = RenewalPolicyTMPMemberHelper.nomineeDtotoEntity(renewalPolicyTMPMemberNomineeDto);
		
		RenewalPolicyTMPMemberEntity renewalPolicyTMPMemberEntity = renewalPolicyTMPMemberRepository.findById(renewalPolicyTMPMemberNomineeEntity.getMember().getId()).get();
		
		newrenewalPolicyTMPMemberNomineeEntity.setId(id);

		newrenewalPolicyTMPMemberNomineeEntity.setMember(renewalPolicyTMPMemberNomineeEntity.getMember());	
		newrenewalPolicyTMPMemberNomineeEntity.setIsActive(true);
		newrenewalPolicyTMPMemberNomineeEntity.setModifiedBy(renewalPolicyTMPMemberNomineeDto.getModifiedBy());
		newrenewalPolicyTMPMemberNomineeEntity.setModifiedDate(new Date());
		newrenewalPolicyTMPMemberNomineeEntity.setCreatedBy(renewalPolicyTMPMemberNomineeEntity.getCreatedBy());
		newrenewalPolicyTMPMemberNomineeEntity.setCreatedDate(renewalPolicyTMPMemberNomineeEntity.getCreatedDate());
		
		renewalPolicyTMPMemberNomineeRepository.save(newrenewalPolicyTMPMemberNomineeEntity);
		
		return ApiResponseDto
				.success(RenewalPolicyTMPMemberHelper.entityToDto(renewalPolicyTMPMemberEntity));

	}
//		return ApiResponseDto.created(renewalPolicyTMPMemberEntity.getNominees().stream().map(RenewalPolicyTMPMemberHelper::nomineeEntityToDto).collect(Collectors.toList()));
//
//	}

	@Override
	public ApiResponseDto<RenewalPolicyTMPMemberDto> uploadtempmemberappointeeupdate(Long id,
			RenewalPolicyTMPMemberAppointeeDto renewalPolicyTMPMemberAppointeeDto) {
		
		RenewalPolicyTMPMemberAppointeeEntity renewalPolicyTMPMemberAppointeeEntity = renewalPolicyTMPMemberAppointeeRepository.findById(id).get();	

		RenewalPolicyTMPMemberAppointeeEntity newrenewalPolicyTMPMemberAppointeeEntity = RenewalPolicyTMPMemberHelper.appointeeDtotoEntity(renewalPolicyTMPMemberAppointeeDto);
		
		RenewalPolicyTMPMemberEntity renewalPolicyTMPMemberEntity = renewalPolicyTMPMemberRepository.findById(renewalPolicyTMPMemberAppointeeEntity.getMember().getId()).get();
		
		newrenewalPolicyTMPMemberAppointeeEntity.setId(id);
		newrenewalPolicyTMPMemberAppointeeEntity.setIsActive(true);
		newrenewalPolicyTMPMemberAppointeeEntity.setMember(renewalPolicyTMPMemberEntity);
		newrenewalPolicyTMPMemberAppointeeEntity.setNominee(renewalPolicyTMPMemberAppointeeEntity.getNominee());
		newrenewalPolicyTMPMemberAppointeeEntity.setModifiedBy(renewalPolicyTMPMemberAppointeeDto.getModifiedBy());
		newrenewalPolicyTMPMemberAppointeeEntity.setModifiedDate(new Date());
		newrenewalPolicyTMPMemberAppointeeEntity.setCreatedBy(renewalPolicyTMPMemberAppointeeEntity.getCreatedBy());
		newrenewalPolicyTMPMemberAppointeeEntity.setCreatedDate(renewalPolicyTMPMemberAppointeeEntity.getCreatedDate());
		
		renewalPolicyTMPMemberAppointeeRepository.save(newrenewalPolicyTMPMemberAppointeeEntity);
		
		return ApiResponseDto
				.success(RenewalPolicyTMPMemberHelper.entityToDto(renewalPolicyTMPMemberEntity));
	}
}
			

