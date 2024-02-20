package com.lic.epgs.gratuity.policy.renewalpolicy.member.helper;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import com.lic.epgs.gratuity.common.entity.MemberCategoryEntity;
import com.lic.epgs.gratuity.policy.member.dto.PolicyMemberAppointeeDto;
import com.lic.epgs.gratuity.policy.member.dto.PolicyMemberDto;
import com.lic.epgs.gratuity.policy.member.entity.HistoryMemberEntity;
import com.lic.epgs.gratuity.policy.member.entity.PolicyMemberAddressEntity;
import com.lic.epgs.gratuity.policy.member.entity.PolicyMemberAppointeeEntity;
import com.lic.epgs.gratuity.policy.member.entity.PolicyMemberBankAccount;
import com.lic.epgs.gratuity.policy.member.entity.PolicyMemberEntity;
import com.lic.epgs.gratuity.policy.member.entity.PolicyMemberNomineeEntity;
import com.lic.epgs.gratuity.policy.member.entity.StagingPolicyMemberEntity;
import com.lic.epgs.gratuity.policy.member.helper.PolicyMemberHelper;
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

@Component
public class RenewalPolicyTMPMemberHelper {
	
	public static RenewalPolicyTMPMemberDto entityToDto(RenewalPolicyTMPMemberEntity renewalPolicyTMPMemberEntity) {
	return new ModelMapper().map(renewalPolicyTMPMemberEntity, RenewalPolicyTMPMemberDto.class);
	}

	public static PolicyMemberEntity updateTemptoPolicyMemberMaster(RenewalPolicyTMPMemberEntity newTempMemberEntity,
			String username) {
		
			PolicyMemberEntity policyMemberEntity =new ModelMapper().map(newTempMemberEntity, PolicyMemberEntity.class);
			policyMemberEntity.setId(newTempMemberEntity.getPmstMemebrId());
			
		
			// Member Address
			Set<PolicyMemberAddressEntity> addRenewalMemberAddressEntity = new HashSet<PolicyMemberAddressEntity>();
			for (RenewalPolicyTMPMemberAddressEntity renewalMemberAddressEntity : newTempMemberEntity.getAddresses()) {

				PolicyMemberAddressEntity policyMemberAddressEntities = new ModelMapper().map(renewalMemberAddressEntity,
						PolicyMemberAddressEntity.class);
				policyMemberAddressEntities.setId(renewalMemberAddressEntity.getPmstMemberAddressId());
				policyMemberAddressEntities.setMember(policyMemberEntity);
				addRenewalMemberAddressEntity.add(policyMemberAddressEntities);
			}
			policyMemberEntity.setAddresses(addRenewalMemberAddressEntity);
			// end

			// Bank Account
			Set<PolicyMemberBankAccount> addPolicyMemberBankAccountEntity = new HashSet<PolicyMemberBankAccount>();
			for (RenewalPolicyTMPMemberBankAccountEntity renewalMemberBankAccount : newTempMemberEntity.getBankAccounts()) {

				PolicyMemberBankAccount policyMemberBankAccountEntities = new ModelMapper()
						.map(renewalMemberBankAccount, PolicyMemberBankAccount.class);
				policyMemberBankAccountEntities.setId(renewalMemberBankAccount.getPmstMemberBankAccId());
				policyMemberBankAccountEntities.setMember(policyMemberEntity);
				addPolicyMemberBankAccountEntity.add(policyMemberBankAccountEntities);
			}
			policyMemberEntity.setBankAccounts(addPolicyMemberBankAccountEntity);
			// end

			// Member Appointee
			Set<PolicyMemberAppointeeEntity> addPolicyMemberAppointeeEntity = new HashSet<PolicyMemberAppointeeEntity>();
			for (RenewalPolicyTMPMemberAppointeeEntity renewalMemberAppointeeEntity : newTempMemberEntity.getAppointees()) {

				PolicyMemberAppointeeEntity policyMemberAppointeeEntities = new ModelMapper()
						.map(renewalMemberAppointeeEntity, PolicyMemberAppointeeEntity.class);
				policyMemberAppointeeEntities.setId(renewalMemberAppointeeEntity.getPmstMemberAppointeeId());
				policyMemberAppointeeEntities.setMember(policyMemberEntity);
				addPolicyMemberAppointeeEntity.add(policyMemberAppointeeEntities);

				RenewalPolicyTMPMemberNomineeEntity newRenewalMemberNomineeEntity = renewalMemberAppointeeEntity.getNominee();
				PolicyMemberNomineeEntity policyMemberNomineeEntity = new ModelMapper().map(newRenewalMemberNomineeEntity,
						PolicyMemberNomineeEntity.class);
				policyMemberNomineeEntity.setId(newRenewalMemberNomineeEntity.getPmstMemberNomineeId());
				policyMemberNomineeEntity.setMember(policyMemberEntity);
				policyMemberAppointeeEntities.setNominee(policyMemberNomineeEntity);
				policyMemberEntity.getAppointees().add(policyMemberAppointeeEntities);

				for (Iterator<RenewalPolicyTMPMemberNomineeEntity> iterator = newTempMemberEntity.getNominees().iterator(); iterator
						.hasNext();) {
					RenewalPolicyTMPMemberNomineeEntity s = iterator.next();
					if (s.getNomineeName() == policyMemberAppointeeEntities.getNominee().getNomineeName()
							&& s.getNomineeAadharNumber() == policyMemberAppointeeEntities.getNominee()
									.getNomineeAadharNumber()
							&& s.getDateOfBirth() == policyMemberAppointeeEntities.getNominee().getDateOfBirth()) {
						iterator.remove();
					}
				}

			}
			policyMemberEntity.setAppointees(addPolicyMemberAppointeeEntity);
			// end

			// Member Nominee
			Set<PolicyMemberNomineeEntity> addPolicyMemberNomineeEntity = new HashSet<PolicyMemberNomineeEntity>();
			for (RenewalPolicyTMPMemberNomineeEntity renewalMemberNomineeEntity : newTempMemberEntity.getNominees()) {

				PolicyMemberNomineeEntity policyMemberNomineeEntities = new ModelMapper().map(renewalMemberNomineeEntity,
						PolicyMemberNomineeEntity.class);
				policyMemberNomineeEntities.setId(renewalMemberNomineeEntity.getPmstMemberNomineeId());
				policyMemberNomineeEntities.setMember(policyMemberEntity);
				addPolicyMemberNomineeEntity.add(policyMemberNomineeEntities);
			}
			policyMemberEntity.setNominees(addPolicyMemberNomineeEntity);
			
			
			policyMemberEntity.setId(newTempMemberEntity.getPmstMemebrId());
			policyMemberEntity.setModifiedBy(username);
			policyMemberEntity.setModifiedDate(new Date());
			policyMemberEntity.setIsActive(true);
			
			return policyMemberEntity;
		
	}
	
	public static PolicyMemberEntity createTemptoPolicyMemberMaster(RenewalPolicyTMPMemberEntity newTempMemberEntity,
			String username, List<MemberCategoryEntity> addMemberCategoryEntity, List<MemberCategoryEntity> oldmemberCategoryEntity) {
		
			PolicyMemberEntity policyMemberEntity =new ModelMapper().map(newTempMemberEntity, PolicyMemberEntity.class);
			policyMemberEntity.setId(null);
			
		
			// Member Address
			Set<PolicyMemberAddressEntity> addRenewalMemberAddressEntity = new HashSet<PolicyMemberAddressEntity>();
			for (RenewalPolicyTMPMemberAddressEntity renewalMemberAddressEntity : newTempMemberEntity.getAddresses()) {

				PolicyMemberAddressEntity policyMemberAddressEntities = new ModelMapper().map(renewalMemberAddressEntity,
						PolicyMemberAddressEntity.class);
				policyMemberAddressEntities.setId(null);
				policyMemberAddressEntities.setMember(policyMemberEntity);
				addRenewalMemberAddressEntity.add(policyMemberAddressEntities);
			}
			policyMemberEntity.setAddresses(addRenewalMemberAddressEntity);
			// end

			// Bank Account
			Set<PolicyMemberBankAccount> addPolicyMemberBankAccountEntity = new HashSet<PolicyMemberBankAccount>();
			for (RenewalPolicyTMPMemberBankAccountEntity renewalMemberBankAccount : newTempMemberEntity.getBankAccounts()) {

				PolicyMemberBankAccount policyMemberBankAccountEntities = new ModelMapper()
						.map(renewalMemberBankAccount, PolicyMemberBankAccount.class);
				policyMemberBankAccountEntities.setId(null);
				policyMemberBankAccountEntities.setMember(policyMemberEntity);
				addPolicyMemberBankAccountEntity.add(policyMemberBankAccountEntities);
			}
			policyMemberEntity.setBankAccounts(addPolicyMemberBankAccountEntity);
			// end

			// Member Appointee
			Set<PolicyMemberAppointeeEntity> addPolicyMemberAppointeeEntity = new HashSet<PolicyMemberAppointeeEntity>();
			for (RenewalPolicyTMPMemberAppointeeEntity renewalMemberAppointeeEntity : newTempMemberEntity.getAppointees()) {

				PolicyMemberAppointeeEntity policyMemberAppointeeEntities = new ModelMapper()
						.map(renewalMemberAppointeeEntity, PolicyMemberAppointeeEntity.class);
				policyMemberAppointeeEntities.setId(null);
				policyMemberAppointeeEntities.setMember(policyMemberEntity);
				addPolicyMemberAppointeeEntity.add(policyMemberAppointeeEntities);

				RenewalPolicyTMPMemberNomineeEntity newRenewalMemberNomineeEntity = renewalMemberAppointeeEntity.getNominee();
				PolicyMemberNomineeEntity policyMemberNomineeEntity = new ModelMapper().map(newRenewalMemberNomineeEntity,
						PolicyMemberNomineeEntity.class);
				policyMemberNomineeEntity.setId(null);
				policyMemberNomineeEntity.setMember(policyMemberEntity);
				policyMemberAppointeeEntities.setNominee(policyMemberNomineeEntity);
				policyMemberEntity.getAppointees().add(policyMemberAppointeeEntities);

				for (Iterator<RenewalPolicyTMPMemberNomineeEntity> iterator = newTempMemberEntity.getNominees().iterator(); iterator
						.hasNext();) {
					RenewalPolicyTMPMemberNomineeEntity s = iterator.next();
					if (s.getNomineeName() == policyMemberAppointeeEntities.getNominee().getNomineeName()
							&& s.getNomineeAadharNumber() == policyMemberAppointeeEntities.getNominee()
									.getNomineeAadharNumber()
							&& s.getDateOfBirth() == policyMemberAppointeeEntities.getNominee().getDateOfBirth()) {
						iterator.remove();
					}
				}

			}
			policyMemberEntity.setAppointees(addPolicyMemberAppointeeEntity);
			// end

			// Member Nominee
			Set<PolicyMemberNomineeEntity> addPolicyMemberNomineeEntity = new HashSet<PolicyMemberNomineeEntity>();
			for (RenewalPolicyTMPMemberNomineeEntity renewalMemberNomineeEntity : newTempMemberEntity.getNominees()) {

				PolicyMemberNomineeEntity policyMemberNomineeEntities = new ModelMapper().map(renewalMemberNomineeEntity,
						PolicyMemberNomineeEntity.class);
				policyMemberNomineeEntities.setId(null);
				policyMemberNomineeEntities.setMember(policyMemberEntity);
				addPolicyMemberNomineeEntity.add(policyMemberNomineeEntities);
			}
			policyMemberEntity.setNominees(addPolicyMemberNomineeEntity);
			
			
			policyMemberEntity.setId(null);
			policyMemberEntity.setCreatedBy(username);
			policyMemberEntity.setCreatedDate(new Date());
			policyMemberEntity.setIsActive(true);
			
			for (MemberCategoryEntity getMemberCategoryEntity : oldmemberCategoryEntity) {
				for (MemberCategoryEntity getnewMemberCategoryEntity : addMemberCategoryEntity) {
					if (getMemberCategoryEntity.getId().equals(newTempMemberEntity.getCategoryId())) {
					if (getMemberCategoryEntity.getName().equals(getnewMemberCategoryEntity.getName())) {
						policyMemberEntity.setCategoryId(getnewMemberCategoryEntity.getId());
					}
					}
				}
			}
			
			return policyMemberEntity;
		
	}

	public static RenewalPolicyTMPMemberNomineeEntity nomineeDtotoEntity(
			RenewalPolicyTMPMemberNomineeDto renewalPolicyTMPMemberNomineeDto) {
		return new ModelMapper().map(renewalPolicyTMPMemberNomineeDto, RenewalPolicyTMPMemberNomineeEntity.class);
	}

	public static RenewalPolicyTMPMemberNomineeDto nomineeEntityToDto(RenewalPolicyTMPMemberNomineeEntity entity) {
		return new ModelMapper().map(entity, RenewalPolicyTMPMemberNomineeDto.class);
	}

	public static RenewalPolicyTMPMemberAddressDto addressEntitytoDto(RenewalPolicyTMPMemberAddressEntity renewalPolicyTMPMemberAddressEntity) {
		
		return new ModelMapper().map(renewalPolicyTMPMemberAddressEntity, RenewalPolicyTMPMemberAddressDto.class);
		
	}

	public static RenewalPolicyTMPMemberBankAccountEntity bankDtoToEntity(
			RenewalPolicyTMPMemberBankAccountDto renewalPolicyTMPMemberBankAccountDto) {
		
		 	return new ModelMapper().map(renewalPolicyTMPMemberBankAccountDto, RenewalPolicyTMPMemberBankAccountEntity.class);
	}
	
	
	public static RenewalPolicyTMPMemberBankAccountDto bankAccountEntityToDto(
			RenewalPolicyTMPMemberBankAccountEntity renewalPolicyTMPMemberBankAccountEntity) {
		
		 	return new ModelMapper().map(renewalPolicyTMPMemberBankAccountEntity, RenewalPolicyTMPMemberBankAccountDto.class);
	}

	public static RenewalPolicyTMPMemberAppointeeEntity appointeeDtotoEntity(
			RenewalPolicyTMPMemberAppointeeDto renewalPolicyTMPMemberAppointeeDto) {
		
		return new ModelMapper().map(renewalPolicyTMPMemberAppointeeDto, RenewalPolicyTMPMemberAppointeeEntity.class);
	}
	
	public static RenewalPolicyTMPMemberAppointeeDto appointeeEntityToDto(
			RenewalPolicyTMPMemberAppointeeEntity renewalPolicyTMPMemberAppointeeEntity) {
		
		return new ModelMapper().map(renewalPolicyTMPMemberAppointeeEntity, RenewalPolicyTMPMemberAppointeeDto.class);
	}
	
	public static RenewalPolicyTMPMemberEntity histtoTmp(HistoryMemberEntity historyMemberEntity) {
		
		return new ModelMapper().map(historyMemberEntity, RenewalPolicyTMPMemberEntity.class);
	}

	public static Object entityToDto1(Optional<RenewalPolicyTMPMemberEntity> findById) {
		
		return new ModelMapper().map(findById, RenewalPolicyTMPMemberDto.class);
	}
	
	
	public static List<RenewalPolicyTMPMemberDto> fromEntities(List<RenewalPolicyTMPMemberEntity> entities) {
		List<RenewalPolicyTMPMemberDto> members = new ArrayList<RenewalPolicyTMPMemberDto>();

		for (RenewalPolicyTMPMemberEntity me : entities) {
			RenewalPolicyTMPMemberDto mdto = new ModelMapper().map(me, RenewalPolicyTMPMemberDto.class);
			if (me.getAddresses() != null && !me.getAddresses().isEmpty()) {
				mdto.setAddresses(
						me.getAddresses().stream().map(RenewalPolicyTMPMemberHelper::addressEntitytoDto).collect(Collectors.toList()));
			}
			if (me.getBankAccounts() != null && !me.getBankAccounts().isEmpty()) {
				mdto.setBankAccounts(me.getBankAccounts().stream().map(RenewalPolicyTMPMemberHelper::bankAccountEntityToDto)
						.collect(Collectors.toList()));
			}
			if (me.getNominees() != null && !me.getNominees().isEmpty()) {
				mdto.setNominees(
						me.getNominees().stream().map(RenewalPolicyTMPMemberHelper::nomineeEntityToDto).collect(Collectors.toList()));
			}
			if (me.getAppointees() != null && !me.getAppointees().isEmpty()) {
				List<RenewalPolicyTMPMemberAppointeeDto> memberApponteeDtos = new ArrayList<RenewalPolicyTMPMemberAppointeeDto>();
				for (RenewalPolicyTMPMemberAppointeeEntity mae : me.getAppointees()) {
					RenewalPolicyTMPMemberAppointeeDto mad = appointeeEntityToDto(mae);
					mad.setNominee(nomineeEntityToDto(mae.getNominee()));
					memberApponteeDtos.add(mad);
				}
				mdto.setAppointees(memberApponteeDtos);
			}

			members.add(mdto);
		}

		return members;
	}



		public static List<RenewalPolicyTMPMemberDto> fromStagingEntities(List<RenewalPolicyTMPMemberEntity> entities) {
			List<RenewalPolicyTMPMemberDto> members = new ArrayList<RenewalPolicyTMPMemberDto>();

			for (RenewalPolicyTMPMemberEntity me : entities) {
				RenewalPolicyTMPMemberDto mdto = new ModelMapper().map(me, RenewalPolicyTMPMemberDto.class);
				if (me.getAddresses() != null && !me.getAddresses().isEmpty()) {
					mdto.setAddresses(
							me.getAddresses().stream().map(RenewalPolicyTMPMemberHelper::addressEntitytoDto).collect(Collectors.toList()));
				}
				if (me.getBankAccounts() != null && !me.getBankAccounts().isEmpty()) {
					mdto.setBankAccounts(me.getBankAccounts().stream().map(RenewalPolicyTMPMemberHelper::bankAccountEntityToDto)
							.collect(Collectors.toList()));
				}
				if (me.getNominees() != null && !me.getNominees().isEmpty()) {
					mdto.setNominees(
							me.getNominees().stream().map(RenewalPolicyTMPMemberHelper::nomineeEntityToDto).collect(Collectors.toList()));
				}
				if (me.getAppointees() != null && !me.getAppointees().isEmpty()) {
					List<RenewalPolicyTMPMemberAppointeeDto> memberApponteeDtos = new ArrayList<RenewalPolicyTMPMemberAppointeeDto>();
					for (RenewalPolicyTMPMemberAppointeeEntity mae : me.getAppointees()) {
						RenewalPolicyTMPMemberAppointeeDto mad = appointeeEntityToDto(mae);
						mad.setNominee(nomineeEntityToDto(mae.getNominee()));
						memberApponteeDtos.add(mad);
					}
					mdto.setAppointees(memberApponteeDtos);
				}

				members.add(mdto);
			}

			return members;
		}

		
	

}
