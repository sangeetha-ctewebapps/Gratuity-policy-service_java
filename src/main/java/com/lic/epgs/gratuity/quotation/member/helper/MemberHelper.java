package com.lic.epgs.gratuity.quotation.member.helper;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.modelmapper.ModelMapper;

import com.lic.epgs.gratuity.policy.member.entity.HistoryMemberEntity;
import com.lic.epgs.gratuity.policy.member.entity.PolicyMemberEntity;
import com.lic.epgs.gratuity.quotation.member.dto.MemberAddressDto;
import com.lic.epgs.gratuity.quotation.member.dto.MemberAppointeeDto;
import com.lic.epgs.gratuity.quotation.member.dto.MemberBankAccountDto;
import com.lic.epgs.gratuity.quotation.member.dto.MemberDto;
import com.lic.epgs.gratuity.quotation.member.dto.MemberNomineeDto;
import com.lic.epgs.gratuity.quotation.member.entity.MasterMemberAddressEntity;
import com.lic.epgs.gratuity.quotation.member.entity.MasterMemberAppointeeEntity;
import com.lic.epgs.gratuity.quotation.member.entity.MasterMemberBankAccount;
import com.lic.epgs.gratuity.quotation.member.entity.MasterMemberEntity;
import com.lic.epgs.gratuity.quotation.member.entity.MasterMemberNomineeEntity;
import com.lic.epgs.gratuity.quotation.member.entity.MemberAddressEntity;
import com.lic.epgs.gratuity.quotation.member.entity.MemberAppointeeEntity;
import com.lic.epgs.gratuity.quotation.member.entity.MemberBankAccount;
import com.lic.epgs.gratuity.quotation.member.entity.MemberEntity;
import com.lic.epgs.gratuity.quotation.member.entity.MemberNomineeEntity;

public class MemberHelper {

	protected final Logger logger = LogManager.getLogger(getClass());

	public static List<MasterMemberEntity> entityToMasterEntity(List<MemberEntity> entities, Long quotationId) {

		List<MasterMemberEntity> members = new ArrayList<MasterMemberEntity>();
		for (MemberEntity me : entities) {
			MasterMemberEntity masterMemberEntity = new ModelMapper().map(me, MasterMemberEntity.class);
			masterMemberEntity.setId(null);
			masterMemberEntity.setQuotationId(quotationId);

			Set<MasterMemberAddressEntity> addMasterAddressEntities = new HashSet<MasterMemberAddressEntity>();
			for (MemberAddressEntity memberAddressEntity : me.getAddresses()) {
				MasterMemberAddressEntity masterMemberAddressEntity = new ModelMapper().map(memberAddressEntity,
						MasterMemberAddressEntity.class);
				masterMemberAddressEntity.setId(null);
				masterMemberAddressEntity.setMember(masterMemberEntity);
				addMasterAddressEntities.add(masterMemberAddressEntity);
			}
			masterMemberEntity.setAddresses(addMasterAddressEntities);

			Set<MasterMemberBankAccount> addMasterMemberBankAccount = new HashSet<MasterMemberBankAccount>();
			for (MemberBankAccount memberBankAccount : me.getBankAccounts()) {
				MasterMemberBankAccount masterMemberBankAccount = new ModelMapper().map(memberBankAccount,
						MasterMemberBankAccount.class);
				masterMemberBankAccount.setId(null);
				masterMemberBankAccount.setMember(masterMemberEntity);
				addMasterMemberBankAccount.add(masterMemberBankAccount);
			}
			masterMemberEntity.setBankAccounts(addMasterMemberBankAccount);

			Set<MasterMemberAppointeeEntity> addMasterMemberAppointeeEntity = new HashSet<MasterMemberAppointeeEntity>();
			for (MemberAppointeeEntity memberAppointeeEntity : me.getAppointees()) {
				MasterMemberAppointeeEntity masterMemberAppointeeEntity = new ModelMapper().map(memberAppointeeEntity,
						MasterMemberAppointeeEntity.class);
				masterMemberAppointeeEntity.setId(null);
				masterMemberAppointeeEntity.setMember(masterMemberEntity);
				addMasterMemberAppointeeEntity.add(masterMemberAppointeeEntity);

				MemberNomineeEntity memberNomineeEntity = memberAppointeeEntity.getNominee();
				MasterMemberNomineeEntity masterMemberNomineeEntity = new ModelMapper().map(memberNomineeEntity,
						MasterMemberNomineeEntity.class);
				masterMemberNomineeEntity.setId(null);
				masterMemberNomineeEntity.setMember(masterMemberEntity);
				masterMemberAppointeeEntity.setNominee(masterMemberNomineeEntity);

				for (Iterator<MemberNomineeEntity> iterator = me.getNominees().iterator(); iterator.hasNext();) {
					MemberNomineeEntity s = iterator.next();
					if (s.getName() == masterMemberAppointeeEntity.getNominee().getName()
							&& s.getAadharNumber() == masterMemberAppointeeEntity.getNominee().getAadharNumber()
							&& s.getPanNumber() == masterMemberAppointeeEntity.getNominee().getPanNumber()) {
						iterator.remove();
					}
				}
				masterMemberEntity.getAppointees().add(masterMemberAppointeeEntity);

			}
			masterMemberEntity.setAppointees(addMasterMemberAppointeeEntity);

			Set<MasterMemberNomineeEntity> addMasterMemberNomineeEntity = new HashSet<MasterMemberNomineeEntity>();
			for (MemberNomineeEntity memberNomineeEntity : me.getNominees()) {
				MasterMemberNomineeEntity masterMemberNomineeEntity = new ModelMapper().map(memberNomineeEntity,
						MasterMemberNomineeEntity.class);
				masterMemberNomineeEntity.setId(null);
				masterMemberNomineeEntity.setMember(masterMemberEntity);
				addMasterMemberNomineeEntity.add(masterMemberNomineeEntity);
			}
			masterMemberEntity.setNominees(addMasterMemberNomineeEntity);

			members.add(masterMemberEntity);
		}

		return members;
	}

	public static List<MemberDto> fromEntities(List<MemberEntity> entities) {
		List<MemberDto> members = new ArrayList<MemberDto>();

		for (MemberEntity me : entities) {
			MemberDto mdto = new ModelMapper().map(me, MemberDto.class);
			if (me.getAddresses() != null && !me.getAddresses().isEmpty()) {
				mdto.setAddresses(
						me.getAddresses().stream().map(MemberHelper::addressEntityToDto).collect(Collectors.toList()));
			}
			if (me.getBankAccounts() != null && !me.getBankAccounts().isEmpty()) {
				mdto.setBankAccounts(me.getBankAccounts().stream().map(MemberHelper::bankAccountEntityToDto)
						.collect(Collectors.toList()));
			}
			if (me.getNominees() != null && !me.getNominees().isEmpty()) {
				mdto.setNominees(
						me.getNominees().stream().map(MemberHelper::nomineeEntityToDto).collect(Collectors.toList()));
			}
			if (me.getAppointees() != null && !me.getAppointees().isEmpty()) {
				List<MemberAppointeeDto> memberApponteeDtos = new ArrayList<MemberAppointeeDto>();
				for (MemberAppointeeEntity mae : me.getAppointees()) {
					MemberAppointeeDto mad = appointeeEntityToDto(mae);
					mad.setNominee(nomineeEntityToDto(mae.getNominee()));
					memberApponteeDtos.add(mad);
				}
				mdto.setAppointees(memberApponteeDtos);
			}

			members.add(mdto);
		}

		return members;
	}

	public static MemberAddressDto addressEntityToDto(MemberAddressEntity entity) {
		return new ModelMapper().map(entity, MemberAddressDto.class);
	}

	public static MemberBankAccountDto bankAccountEntityToDto(MemberBankAccount entity) {
		return new ModelMapper().map(entity, MemberBankAccountDto.class);
	}

	public static MemberNomineeDto nomineeEntityToDto(MemberNomineeEntity entity) {
		return new ModelMapper().map(entity, MemberNomineeDto.class);
	}

	public static MemberAppointeeDto appointeeEntityToDto(MemberAppointeeEntity entity) {
		return new ModelMapper().map(entity, MemberAppointeeDto.class);
	}

	public static MasterMemberAddressEntity addressEntityToMasterAddressEntity(MemberAddressEntity entity) {
		return new ModelMapper().map(entity, MasterMemberAddressEntity.class);
	}

	public static MasterMemberBankAccount bankAccountEntityToMasterBankAccountEntity(MemberBankAccount entity) {
		return new ModelMapper().map(entity, MasterMemberBankAccount.class);
	}

	public static MasterMemberNomineeEntity nomineeEntityToMasterNomineeEntity(MemberNomineeEntity entity) {
		return new ModelMapper().map(entity, MasterMemberNomineeEntity.class);
	}

	public static MasterMemberAppointeeEntity appointeeEntityToMasterAppointeeEntity(MemberAppointeeEntity entity) {
		return new ModelMapper().map(entity, MasterMemberAppointeeEntity.class);
	}

	public static MemberEntity clone(MemberEntity getMemberEntity, String username, Long quotationId) {

		MemberDto memberDto = new ModelMapper().map(getMemberEntity, MemberDto.class);
		memberDto.setId(null);
		MemberEntity newMemberEntity = new ModelMapper().map(memberDto, MemberEntity.class);

		Set<MemberAddressEntity> addMemberAddressEntity = new HashSet<MemberAddressEntity>();
		for (MemberAddressEntity getMemberAddressEntity : getMemberEntity.getAddresses()) {
			getMemberAddressEntity.setId(null);
			getMemberAddressEntity.setMember(newMemberEntity);
			getMemberAddressEntity.setCreatedBy(username);
			getMemberAddressEntity.setCreatedDate(new Date());
			getMemberAddressEntity.setIsActive(true);
			addMemberAddressEntity.add(getMemberAddressEntity);
		}
		newMemberEntity.setAddresses(addMemberAddressEntity);

		Set<MemberBankAccount> addMemberBankAccount = new HashSet<MemberBankAccount>();
		for (MemberBankAccount getMemberBankAccount : getMemberEntity.getBankAccounts()) {
			getMemberBankAccount.setId(null);
			getMemberBankAccount.setMember(newMemberEntity);
			getMemberBankAccount.setCreatedBy(username);
			getMemberBankAccount.setCreatedDate(new Date());
			getMemberBankAccount.setIsActive(true);
			addMemberBankAccount.add(getMemberBankAccount);
		}
		newMemberEntity.setBankAccounts(addMemberBankAccount);

		Set<MemberAppointeeEntity> addMemberAppointeeEntity = new HashSet<MemberAppointeeEntity>();
		for (MemberAppointeeEntity memberAppointeeEntity : getMemberEntity.getAppointees()) {
			memberAppointeeEntity.setId(null);
			memberAppointeeEntity.setMember(newMemberEntity);
			memberAppointeeEntity.setCreatedBy(username);
			memberAppointeeEntity.setCreatedDate(new Date());
			memberAppointeeEntity.setIsActive(true);

			memberAppointeeEntity.getNominee().setId(null);
			memberAppointeeEntity.getNominee().setMember(newMemberEntity);
			for (Iterator<MemberNomineeEntity> iterator = newMemberEntity.getNominees().iterator(); iterator
					.hasNext();) {
				MemberNomineeEntity s = iterator.next();
				if (s.getName() == memberAppointeeEntity.getNominee().getName()
						&& s.getAadharNumber() == memberAppointeeEntity.getNominee().getAadharNumber()
						&& s.getPanNumber() == memberAppointeeEntity.getNominee().getPanNumber()) {
					iterator.remove();
				}
			}
			addMemberAppointeeEntity.add(memberAppointeeEntity);
		}
		newMemberEntity.setAppointees(addMemberAppointeeEntity);

		Set<MemberNomineeEntity> addMemberNomineeEntity = new HashSet<MemberNomineeEntity>();
		for (MemberNomineeEntity memberNomineeEntity : getMemberEntity.getNominees()) {
			memberNomineeEntity.setId(null);
			memberNomineeEntity.setMember(newMemberEntity);
			memberNomineeEntity.setCreatedBy(username);
			memberNomineeEntity.setCreatedDate(new Date());
			memberNomineeEntity.setIsActive(true);
			addMemberNomineeEntity.add(memberNomineeEntity);
		}
		newMemberEntity.setNominees(addMemberNomineeEntity);
		newMemberEntity.setQuotationId(quotationId);
		newMemberEntity.setCreatedBy(username);
		newMemberEntity.setCreatedDate(new Date());
		newMemberEntity.setIsActive(true);
		return newMemberEntity;
	}

	public static MasterMemberEntity masterClone(MasterMemberEntity masterMemberEntity, String username,
			Long quotationId) {
		MemberDto memberDto = new ModelMapper().map(masterMemberEntity, MemberDto.class);
		memberDto.setId(null);
		MasterMemberEntity newMasterMemberEntity = new ModelMapper().map(memberDto, MasterMemberEntity.class);

		Set<MasterMemberAddressEntity> addMasterMemberAddressEntity = new HashSet<MasterMemberAddressEntity>();
		for (MasterMemberAddressEntity masterMemberAddressEntity : masterMemberEntity.getAddresses()) {
			masterMemberAddressEntity.setId(null);
			masterMemberAddressEntity.setMember(newMasterMemberEntity);
			masterMemberAddressEntity.setCreatedBy(username);
			masterMemberAddressEntity.setCreatedDate(new Date());
			masterMemberAddressEntity.setIsActive(true);
			addMasterMemberAddressEntity.add(masterMemberAddressEntity);

		}
		newMasterMemberEntity.setAddresses(addMasterMemberAddressEntity);

		Set<MasterMemberBankAccount> addMasterMemberBankAccount = new HashSet<MasterMemberBankAccount>();
		for (MasterMemberBankAccount masterMemberAddressEntity : masterMemberEntity.getBankAccounts()) {
			masterMemberAddressEntity.setId(null);
			masterMemberAddressEntity.setMember(newMasterMemberEntity);
			masterMemberAddressEntity.setCreatedBy(username);
			masterMemberAddressEntity.setCreatedDate(new Date());
			masterMemberAddressEntity.setIsActive(true);
			addMasterMemberBankAccount.add(masterMemberAddressEntity);
		}
		newMasterMemberEntity.setBankAccounts(addMasterMemberBankAccount);

		Set<MasterMemberAppointeeEntity> addMasterMemberAppointeeEntity = new HashSet<MasterMemberAppointeeEntity>();
		for (MasterMemberAppointeeEntity masterMemberAppointeeEntity : masterMemberEntity.getAppointees()) {
			masterMemberAppointeeEntity.setId(null);
			masterMemberAppointeeEntity.setMember(newMasterMemberEntity);
			masterMemberAppointeeEntity.setCreatedBy(username);
			masterMemberAppointeeEntity.setCreatedDate(new Date());
			masterMemberAppointeeEntity.setIsActive(true);
			masterMemberAppointeeEntity.getNominee().setId(null);
			masterMemberAppointeeEntity.getNominee().setMember(newMasterMemberEntity);
			for (Iterator<MasterMemberNomineeEntity> iterator = newMasterMemberEntity.getNominees().iterator(); iterator
					.hasNext();) {
				MasterMemberNomineeEntity s = iterator.next();
				if (s.getName() == masterMemberAppointeeEntity.getNominee().getName()
						&& s.getAadharNumber() == masterMemberAppointeeEntity.getNominee().getAadharNumber()
						&& s.getPanNumber() == masterMemberAppointeeEntity.getNominee().getPanNumber()) {
					iterator.remove();
				}
			}
			;
			addMasterMemberAppointeeEntity.add(masterMemberAppointeeEntity);
		}
		newMasterMemberEntity.setAppointees(addMasterMemberAppointeeEntity);

		Set<MasterMemberNomineeEntity> addMasterMemberNomineeEntity = new HashSet<MasterMemberNomineeEntity>();
		for (MasterMemberNomineeEntity masterMemberNomineeEntity : masterMemberEntity.getNominees()) {
			masterMemberNomineeEntity.setId(null);
			masterMemberNomineeEntity.setMember(newMasterMemberEntity);
			masterMemberNomineeEntity.setCreatedBy(username);
			masterMemberNomineeEntity.setCreatedDate(new Date());
			masterMemberNomineeEntity.setIsActive(true);
			addMasterMemberNomineeEntity.add(masterMemberNomineeEntity);
		}
		newMasterMemberEntity.setNominees(addMasterMemberNomineeEntity);
		newMasterMemberEntity.setQuotationId(quotationId);
		newMasterMemberEntity.setCreatedBy(username);
		newMasterMemberEntity.setCreatedDate(new Date());
		newMasterMemberEntity.setIsActive(true);

		return newMasterMemberEntity;
	}

	public static HistoryMemberEntity policymastertohistoryupdate(PolicyMemberEntity policyMemberEntity,
			String username) {
		HistoryMemberEntity historyMemberEntity = new ModelMapper().map(policyMemberEntity, HistoryMemberEntity.class);
		if (policyMemberEntity.getModifiedDate() == null) {
			historyMemberEntity.setPmstMemberId(policyMemberEntity.getId());
			historyMemberEntity.setEffectiveFromDate(policyMemberEntity.getCreatedDate());
			historyMemberEntity.setEffectiveToDate(new Date());
			
			historyMemberEntity.setCreatedBy(username);
			historyMemberEntity.setCreatedDate(new Date());
			historyMemberEntity.setModifiedBy(null);
			historyMemberEntity.setModifiedDate(new Date());
		} else {
			Timestamp effictiveDate = new Timestamp(policyMemberEntity.getModifiedDate().getTime() + (60 * 1000L));
			historyMemberEntity.setPmstMemberId(policyMemberEntity.getId());
			historyMemberEntity.setEffectiveFromDate(effictiveDate);
			historyMemberEntity.setEffectiveToDate(new Date());
			historyMemberEntity.setCreatedBy(username);
			historyMemberEntity.setCreatedDate(new Date());
			historyMemberEntity.setModifiedBy(null);
			historyMemberEntity.setModifiedDate(new Date());
		}
		return historyMemberEntity;
	}

	public static List<MemberDto> fromMasterEntities(List<MasterMemberEntity> entities) {
		List<MemberDto> members = new ArrayList<MemberDto>();

		for (MasterMemberEntity me : entities) {
			MemberDto mdto = new ModelMapper().map(me, MemberDto.class);
			if (me.getAddresses() != null && !me.getAddresses().isEmpty()) {
				mdto.setAddresses(me.getAddresses().stream().map(MemberHelper::masterAddressEntityToDto)
						.collect(Collectors.toList()));
			}
			if (me.getBankAccounts() != null && !me.getBankAccounts().isEmpty()) {
				mdto.setBankAccounts(me.getBankAccounts().stream().map(MemberHelper::masterBankAccountEntityToDto)
						.collect(Collectors.toList()));
			}
			if (me.getNominees() != null && !me.getNominees().isEmpty()) {
				mdto.setNominees(me.getNominees().stream().map(MemberHelper::masterNomineeEntityToDto)
						.collect(Collectors.toList()));
			}
			if (me.getAppointees() != null && !me.getAppointees().isEmpty()) {
				List<MemberAppointeeDto> memberApponteeDtos = new ArrayList<MemberAppointeeDto>();
				for (MasterMemberAppointeeEntity mae : me.getAppointees()) {
					MemberAppointeeDto mad = masterAppointeeEntityToDto(mae);
					mad.setNominee(masterNomineeEntityToDto(mae.getNominee()));
					memberApponteeDtos.add(mad);
				}
				mdto.setAppointees(memberApponteeDtos);
			}

			members.add(mdto);
		}
		return members;
	}

	public static MemberAddressDto masterAddressEntityToDto(MasterMemberAddressEntity mastermemberaddressentity) {
		return new ModelMapper().map(mastermemberaddressentity, MemberAddressDto.class);
	}

	public static MemberBankAccountDto masterBankAccountEntityToDto(MasterMemberBankAccount mastermemberbankaccount) {
		return new ModelMapper().map(mastermemberbankaccount, MemberBankAccountDto.class);
	}

	public static MemberNomineeDto masterNomineeEntityToDto(MasterMemberNomineeEntity masterMemberNomineeEntity) {
		return new ModelMapper().map(masterMemberNomineeEntity, MemberNomineeDto.class);
	}

	public static MemberAppointeeDto masterAppointeeEntityToDto(
			MasterMemberAppointeeEntity masterMemberAppointeeEntity) {
		return new ModelMapper().map(masterMemberAppointeeEntity, MemberAppointeeDto.class);
	}

	public static MemberDto entitytomemberdto(MemberEntity entity) {
		return new ModelMapper().map(entity, MemberDto.class);
	}

	public static MemberAddressEntity addressDtotoEntity(MemberAddressDto memberAddressDto) {
		return new ModelMapper().map(memberAddressDto, MemberAddressEntity.class);
	}

	public static MemberBankAccount bankDtoToEntity(MemberBankAccountDto memberBankAccountDto) {
		return new ModelMapper().map(memberBankAccountDto, MemberBankAccount.class);
	}

	public static MemberNomineeEntity nomineeDtotoEntity(MemberNomineeDto memberNomineeDto) {
		return new ModelMapper().map(memberNomineeDto, MemberNomineeEntity.class);
	}

	public static MemberAppointeeEntity appointeeDtotoEntity(MemberAppointeeDto memberAppointeeDto) {
		return new ModelMapper().map(memberAppointeeDto, MemberAppointeeEntity.class);
	}

}
