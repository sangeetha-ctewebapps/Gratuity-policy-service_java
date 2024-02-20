package com.lic.epgs.gratuity.policy.member.helper;

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
import org.springframework.beans.factory.annotation.Autowired;

import com.lic.epgs.gratuity.mph.entity.MPHAddressEntity;
import com.lic.epgs.gratuity.policy.entity.MasterPolicyEntity;
import com.lic.epgs.gratuity.policy.entity.StagingPolicyEntity;
import com.lic.epgs.gratuity.policy.member.dto.PolicyMemberAddressDto;
import com.lic.epgs.gratuity.policy.member.dto.PolicyMemberAppointeeDto;
import com.lic.epgs.gratuity.policy.member.dto.PolicyMemberBankAccountDto;
import com.lic.epgs.gratuity.policy.member.dto.PolicyMemberDto;
import com.lic.epgs.gratuity.policy.member.dto.PolicyMemberNomineeDto;
import com.lic.epgs.gratuity.policy.member.dto.TempMemberDto;
import com.lic.epgs.gratuity.policy.member.entity.StagingPolicyMemberAddressEntity;
import com.lic.epgs.gratuity.policy.member.entity.StagingPolicyMemberAppointeeEntity;
import com.lic.epgs.gratuity.policy.member.entity.StagingPolicyMemberBankAccount;
import com.lic.epgs.gratuity.policy.member.entity.StagingPolicyMemberEntity;
import com.lic.epgs.gratuity.policy.member.entity.StagingPolicyMemberNomineeEntity;
import com.lic.epgs.gratuity.policy.member.entity.TempMemberEntity;
import com.lic.epgs.gratuity.policy.member.repository.PolicyMemberRepository;
import com.lic.epgs.gratuity.policy.renewalpolicy.member.entity.RenewalPolicyTMPMemberAddressEntity;
import com.lic.epgs.gratuity.policy.renewalpolicy.member.entity.RenewalPolicyTMPMemberAppointeeEntity;
import com.lic.epgs.gratuity.policy.renewalpolicy.member.entity.RenewalPolicyTMPMemberBankAccountEntity;
import com.lic.epgs.gratuity.policy.renewalpolicy.member.entity.RenewalPolicyTMPMemberEntity;
import com.lic.epgs.gratuity.policy.renewalpolicy.member.entity.RenewalPolicyTMPMemberNomineeEntity;
import com.lic.epgs.gratuity.policyservices.dom.dto.MemberLeaverDto;
import com.lic.epgs.gratuity.policyservices.dom.entity.PmstMidleaverMemberPropsEntity;
import com.lic.epgs.gratuity.quotation.member.entity.MasterMemberAddressEntity;
import com.lic.epgs.gratuity.quotation.member.entity.MasterMemberAppointeeEntity;
import com.lic.epgs.gratuity.quotation.member.entity.MasterMemberBankAccount;
import com.lic.epgs.gratuity.quotation.member.entity.MasterMemberEntity;
import com.lic.epgs.gratuity.quotation.member.entity.MasterMemberNomineeEntity;
import com.lic.epgs.gratuity.policy.member.entity.HistoryMemberAddressEntity;
import com.lic.epgs.gratuity.policy.member.entity.HistoryMemberAppointeeEntity;
import com.lic.epgs.gratuity.policy.member.entity.HistoryMemberBankAccountEntity;
import com.lic.epgs.gratuity.policy.member.entity.HistoryMemberEntity;
import com.lic.epgs.gratuity.policy.member.entity.HistoryMemberNomineeEntity;
import com.lic.epgs.gratuity.policy.member.entity.PolicyMemberAddressEntity;
import com.lic.epgs.gratuity.policy.member.entity.PolicyMemberAppointeeEntity;
import com.lic.epgs.gratuity.policy.member.entity.PolicyMemberBankAccount;
import com.lic.epgs.gratuity.policy.member.entity.PolicyMemberEntity;
import com.lic.epgs.gratuity.policy.member.entity.PolicyMemberNomineeEntity;

public class PolicyMemberHelper {

	protected final Logger logger = LogManager.getLogger(getClass());
	
	public static List<PolicyMemberDto> fromEntities(List<PolicyMemberEntity> entities) {
		List<PolicyMemberDto> members = new ArrayList<PolicyMemberDto>();

		for (PolicyMemberEntity me : entities) {
			PolicyMemberDto mdto = new ModelMapper().map(me, PolicyMemberDto.class);
			if (me.getAddresses() != null && !me.getAddresses().isEmpty()) {
				mdto.setAddresses(
						me.getAddresses().stream().map(PolicyMemberHelper::addressEntityToDto).collect(Collectors.toList()));
			}
			if (me.getBankAccounts() != null && !me.getBankAccounts().isEmpty()) {
				mdto.setBankAccounts(me.getBankAccounts().stream().map(PolicyMemberHelper::bankAccountEntityToDto)
						.collect(Collectors.toList()));
			}
			if (me.getNominees() != null && !me.getNominees().isEmpty()) {
				mdto.setNominees(
						me.getNominees().stream().map(PolicyMemberHelper::nomineeEntityToDto).collect(Collectors.toList()));
			}
			if (me.getAppointees() != null && !me.getAppointees().isEmpty()) {
				List<PolicyMemberAppointeeDto> memberApponteeDtos = new ArrayList<PolicyMemberAppointeeDto>();
				for (PolicyMemberAppointeeEntity mae : me.getAppointees()) {
					PolicyMemberAppointeeDto mad = appointeeEntityToDto(mae);
					mad.setNominee(nomineeEntityToDto(mae.getNominee()));
					memberApponteeDtos.add(mad);
				}
				mdto.setAppointees(memberApponteeDtos);
			}

			members.add(mdto);
		}

		return members;
	}
	
	public static List<StagingPolicyMemberEntity> entityToMasterEntity(List<MasterMemberEntity> entities, 
			StagingPolicyEntity savedStagingPolicyEntity) {
		System.out.println("1");;
		List<StagingPolicyMemberEntity> members = new ArrayList<StagingPolicyMemberEntity>();
		for (MasterMemberEntity me : entities) {
			StagingPolicyMemberEntity stagingPolicyMemberEntity = new ModelMapper().map(me, StagingPolicyMemberEntity.class);
			stagingPolicyMemberEntity.setId(null);
			stagingPolicyMemberEntity.setPolicyId(savedStagingPolicyEntity.getId());
			stagingPolicyMemberEntity.setDojToScheme(savedStagingPolicyEntity.getPolicyStartDate());	
			stagingPolicyMemberEntity.setFirstName(me.getFirstName());
			stagingPolicyMemberEntity.setMiddleName(me.getMiddleName());
			stagingPolicyMemberEntity.setLastName(me.getLastName());
			Set<StagingPolicyMemberAddressEntity>  addStagingPolicyMemberAddressEntity =new HashSet<StagingPolicyMemberAddressEntity>();
			for (MasterMemberAddressEntity masterMemberAddressEntity : me.getAddresses()) {
				StagingPolicyMemberAddressEntity stagingPolicyMemberAddressEntity =new ModelMapper().map(masterMemberAddressEntity, StagingPolicyMemberAddressEntity.class);
				
				stagingPolicyMemberAddressEntity.setId(null);		
				stagingPolicyMemberAddressEntity.setMember(stagingPolicyMemberEntity);
				addStagingPolicyMemberAddressEntity.add(stagingPolicyMemberAddressEntity);
			}
			stagingPolicyMemberEntity.setAddresses(addStagingPolicyMemberAddressEntity);
			
			Set<StagingPolicyMemberBankAccount>  addStagingPolicyMemberBankAccount =new HashSet<StagingPolicyMemberBankAccount>();
			for (MasterMemberBankAccount masterMemberBankAccount : me.getBankAccounts()) {
				StagingPolicyMemberBankAccount stagingPolicyMemberBankAccount =new ModelMapper().map(masterMemberBankAccount, StagingPolicyMemberBankAccount.class);
				
				stagingPolicyMemberBankAccount.setId(null);
				stagingPolicyMemberBankAccount.setMember(stagingPolicyMemberEntity);
				addStagingPolicyMemberBankAccount.add(stagingPolicyMemberBankAccount);
				}
			stagingPolicyMemberEntity.setBankAccounts(addStagingPolicyMemberBankAccount);
			
			Set<StagingPolicyMemberAppointeeEntity>  addStagingPolicyMemberAppointeeEntity =new HashSet<StagingPolicyMemberAppointeeEntity>();	
			for (MasterMemberAppointeeEntity masterMemberAppointeeEntity : me.getAppointees()) {
				StagingPolicyMemberAppointeeEntity stagingPolicyMemberAppointeeEntity = new ModelMapper().map(masterMemberAppointeeEntity, StagingPolicyMemberAppointeeEntity.class);
				stagingPolicyMemberAppointeeEntity.setId(null);
				stagingPolicyMemberAppointeeEntity.setMember(stagingPolicyMemberEntity);
				
				MasterMemberNomineeEntity masterMemberNomineeEntity = masterMemberAppointeeEntity.getNominee();
				StagingPolicyMemberNomineeEntity stagingPolicyMemberNomineeEntity =new ModelMapper().map(masterMemberNomineeEntity, StagingPolicyMemberNomineeEntity.class);
				stagingPolicyMemberNomineeEntity.setId(null);
				stagingPolicyMemberNomineeEntity.setMember(stagingPolicyMemberEntity);
				stagingPolicyMemberAppointeeEntity.setNominee(stagingPolicyMemberNomineeEntity);				
				stagingPolicyMemberEntity.getAppointees().add(stagingPolicyMemberAppointeeEntity);
				
				for (Iterator<MasterMemberNomineeEntity> iterator = me.getNominees().iterator(); iterator.hasNext();) {
					MasterMemberNomineeEntity s =  iterator.next();
					if (s.getName() == stagingPolicyMemberAppointeeEntity.getNominee().getNomineeName() 
				    		&& s.getAadharNumber() == stagingPolicyMemberAppointeeEntity.getNominee().getNomineeAadharNumber()
				    		&& s.getDateOfBirth() == stagingPolicyMemberAppointeeEntity.getNominee().getDateOfBirth()){
				        iterator.remove();
				    }    
				}
				addStagingPolicyMemberAppointeeEntity.add(stagingPolicyMemberAppointeeEntity);
				}
			stagingPolicyMemberEntity.setAppointees(addStagingPolicyMemberAppointeeEntity);
			
			Set<StagingPolicyMemberNomineeEntity>  addStagingPolicyMemberNomineeEntity =new HashSet<StagingPolicyMemberNomineeEntity>();
			for (MasterMemberNomineeEntity masterMemberNomineeEntity : me.getNominees()) {
				StagingPolicyMemberNomineeEntity stagingPolicyMemberNomineeEntity = new ModelMapper().map(masterMemberNomineeEntity, StagingPolicyMemberNomineeEntity.class);
				stagingPolicyMemberNomineeEntity.setId(null);
				stagingPolicyMemberNomineeEntity.setMember(stagingPolicyMemberEntity);	
				addStagingPolicyMemberNomineeEntity.add(stagingPolicyMemberNomineeEntity);
			}
			stagingPolicyMemberEntity.setNominees(addStagingPolicyMemberNomineeEntity);
			
			members.add(stagingPolicyMemberEntity);
		}
		return members;
		
	}

	public static PolicyMemberAddressDto addressEntityToDto(StagingPolicyMemberAddressEntity entity) {
		return new ModelMapper().map(entity, PolicyMemberAddressDto.class);
	}

	public static PolicyMemberBankAccountDto bankAccountEntityToDto(StagingPolicyMemberBankAccount entity) {
		return new ModelMapper().map(entity, PolicyMemberBankAccountDto.class);
	}

	public static PolicyMemberNomineeDto nomineeEntityToDto(StagingPolicyMemberNomineeEntity entity) {
		return new ModelMapper().map(entity, PolicyMemberNomineeDto.class);
	}

	public static PolicyMemberAppointeeDto appointeeEntityToDto(StagingPolicyMemberAppointeeEntity entity) {
		return new ModelMapper().map(entity, PolicyMemberAppointeeDto.class);
	}

	public static PolicyMemberAddressDto addressEntityToDto(PolicyMemberAddressEntity entity) {
		return new ModelMapper().map(entity, PolicyMemberAddressDto.class);
	}
	
	public static PolicyMemberBankAccountDto bankAccountEntityToDto(PolicyMemberBankAccount entity) {
		return new ModelMapper().map(entity, PolicyMemberBankAccountDto.class);
	}
	
	public static PolicyMemberNomineeDto nomineeEntityToDto(PolicyMemberNomineeEntity entity) {
		return new ModelMapper().map(entity, PolicyMemberNomineeDto.class);
	}

	public static PolicyMemberAppointeeDto appointeeEntityToDto(PolicyMemberAppointeeEntity entity) {
		return new ModelMapper().map(entity, PolicyMemberAppointeeDto.class);
	}

	public static TempMemberDto tempentitytoDto(TempMemberEntity entity) {
		return new ModelMapper().map(entity, TempMemberDto.class);
	}
	
	public static MemberLeaverDto tmpMemberLeaver(TempMemberEntity entity) {
		return new ModelMapper().map(entity, MemberLeaverDto.class);
	}
	
	public static MemberLeaverDto addMemberLeaverProps(MemberLeaverDto memberLeaverDto, PmstMidleaverMemberPropsEntity pmstMidleaverMemberPropsEntity) {
		memberLeaverDto.setDateOfLeaving(pmstMidleaverMemberPropsEntity.getLeavingDate());
		return memberLeaverDto;
	}
	
	public static List<PolicyMemberEntity> tmpMemberToMasterEntity(
			List<RenewalPolicyTMPMemberEntity> entities, 
			Long masterPolicyId,
			Integer maxLICID) {
		
		List<PolicyMemberEntity> members = new ArrayList<PolicyMemberEntity>();
		for (RenewalPolicyTMPMemberEntity tpmMemberEntity : entities) {
			PolicyMemberEntity policyMemberEntity = new ModelMapper().map(tpmMemberEntity, PolicyMemberEntity.class);
			policyMemberEntity.setId(null);
			policyMemberEntity.setPolicyId(masterPolicyId);
			policyMemberEntity.setLicId(maxLICID.toString());
			maxLICID = maxLICID+1;
			policyMemberEntity.setFirstName(tpmMemberEntity.getFirstName());
			policyMemberEntity.setMiddleName(tpmMemberEntity.getMiddleName());
			policyMemberEntity.setLastName(tpmMemberEntity.getLastName());
			policyMemberEntity.setLastPremiumCollectedDate(new Date());;
			
			Set<PolicyMemberAddressEntity> addPolicyMemberAddressEntity = new HashSet<PolicyMemberAddressEntity>();
			for (RenewalPolicyTMPMemberAddressEntity tmpMemberAddressEntity : tpmMemberEntity.getAddresses()) {
				PolicyMemberAddressEntity policyMemberAddressEntity=new ModelMapper().map(tmpMemberAddressEntity, PolicyMemberAddressEntity.class);
				policyMemberAddressEntity.setId(null);		
				policyMemberAddressEntity.setMember(policyMemberEntity);
				addPolicyMemberAddressEntity.add(policyMemberAddressEntity);
			}
			policyMemberEntity.setAddresses(addPolicyMemberAddressEntity);
			
			Set<PolicyMemberBankAccount> addPolicyMemberBankAccount = new HashSet<PolicyMemberBankAccount>();
			for (RenewalPolicyTMPMemberBankAccountEntity tmpMemberBankAccount : tpmMemberEntity.getBankAccounts()) {
				PolicyMemberBankAccount policyMemberBankAccount =new ModelMapper().map(tmpMemberBankAccount, PolicyMemberBankAccount.class);
				policyMemberBankAccount.setId(null);
				policyMemberBankAccount.setMember(policyMemberEntity);
				addPolicyMemberBankAccount.add(policyMemberBankAccount);
			}
			policyMemberEntity.setBankAccounts(addPolicyMemberBankAccount);
			
			Set<PolicyMemberAppointeeEntity> addPolicyMemberAppointeeEntity = new HashSet<PolicyMemberAppointeeEntity>();
			for (RenewalPolicyTMPMemberAppointeeEntity tmpMemberAppointeeEntity : tpmMemberEntity.getAppointees()) {
				PolicyMemberAppointeeEntity policyMemberAppointeeEntity=new ModelMapper().map(tmpMemberAppointeeEntity, PolicyMemberAppointeeEntity.class);
				policyMemberAppointeeEntity.setId(null);
				policyMemberAppointeeEntity.setMember(policyMemberEntity);
				addPolicyMemberAppointeeEntity.add(policyMemberAppointeeEntity);
			
				RenewalPolicyTMPMemberNomineeEntity tmpMemberNomineeEntity = tmpMemberAppointeeEntity.getNominee();
				PolicyMemberNomineeEntity policyMemberNomineeEntity =new ModelMapper().map(tmpMemberNomineeEntity, PolicyMemberNomineeEntity.class);
				policyMemberNomineeEntity.setId(null);
				policyMemberNomineeEntity.setMember(policyMemberEntity);
				policyMemberAppointeeEntity.setNominee(policyMemberNomineeEntity);				
				policyMemberEntity.getAppointees().add(policyMemberAppointeeEntity);
			
				for (Iterator<RenewalPolicyTMPMemberNomineeEntity> iterator = tpmMemberEntity.getNominees().iterator(); iterator.hasNext();) {
					RenewalPolicyTMPMemberNomineeEntity s =  iterator.next();
					if (s.getNomineeName() == policyMemberAppointeeEntity.getNominee().getNomineeName() 
				    		&& s.getNomineeAadharNumber() == policyMemberAppointeeEntity.getNominee().getNomineeAadharNumber()
				    		&& s.getDateOfBirth() == policyMemberAppointeeEntity.getNominee().getDateOfBirth()){
				        iterator.remove();
				    }    
				}
			}
			policyMemberEntity.setAppointees(addPolicyMemberAppointeeEntity);
		
			Set<PolicyMemberNomineeEntity> addPolicyMemberNomineeEntity = new HashSet<PolicyMemberNomineeEntity>();	
			for (RenewalPolicyTMPMemberNomineeEntity tmpMemberNomineeEntity : tpmMemberEntity.getNominees()) {
				PolicyMemberNomineeEntity policyMemberNomineeEntity =new ModelMapper().map(tmpMemberNomineeEntity, PolicyMemberNomineeEntity.class);
				policyMemberNomineeEntity.setId(null);
				policyMemberNomineeEntity.setMember(policyMemberEntity);
				addPolicyMemberNomineeEntity.add(policyMemberNomineeEntity);
				
			}
			policyMemberEntity.setNominees(addPolicyMemberNomineeEntity);
			
			members.add(policyMemberEntity);
		}
		return members;
		
	}

	public static List<PolicyMemberEntity> entityTopolicyMasterEntity(List<StagingPolicyMemberEntity> entities,
			MasterPolicyEntity masterPolicyEntity) {
		// TODO Auto-generated method stub
		Long maxLICID=1L;
		List<PolicyMemberEntity> members = new ArrayList<PolicyMemberEntity>();
		for (StagingPolicyMemberEntity me : entities) {
			PolicyMemberEntity policyMemberEntity = new ModelMapper().map(me, PolicyMemberEntity.class);
			policyMemberEntity.setId(null);
			policyMemberEntity.setPolicyId(masterPolicyEntity.getId());
			
			policyMemberEntity.setDojToScheme(masterPolicyEntity.getPolicyStartDate());
			policyMemberEntity.setLicId(maxLICID.toString());
			maxLICID =maxLICID+1;
			policyMemberEntity.setFirstName(me.getFirstName());
			policyMemberEntity.setMiddleName(me.getMiddleName());
			policyMemberEntity.setLastName(me.getLastName());
			Set<PolicyMemberAddressEntity> addPolicyMemberAddressEntity = new HashSet<PolicyMemberAddressEntity>();
			
			for (StagingPolicyMemberAddressEntity stagingPolicyMemberAddressEntity : me.getAddresses()) {
				PolicyMemberAddressEntity policyMemberAddressEntity=new ModelMapper().map(stagingPolicyMemberAddressEntity, PolicyMemberAddressEntity.class);
				policyMemberAddressEntity.setId(null);		
				policyMemberAddressEntity.setMember(policyMemberEntity);
				addPolicyMemberAddressEntity.add(policyMemberAddressEntity);
			}
			policyMemberEntity.setAddresses(addPolicyMemberAddressEntity);
			
			Set<PolicyMemberBankAccount> addPolicyMemberBankAccount = new HashSet<PolicyMemberBankAccount>();
			
			for (StagingPolicyMemberBankAccount stagingPolicyMemberBankAccount : me.getBankAccounts()) {
				PolicyMemberBankAccount policyMemberBankAccount =new ModelMapper().map(stagingPolicyMemberBankAccount, PolicyMemberBankAccount.class);
				policyMemberBankAccount.setId(null);
				policyMemberBankAccount.setMember(policyMemberEntity);
				addPolicyMemberBankAccount.add(policyMemberBankAccount);
			}
			policyMemberEntity.setBankAccounts(addPolicyMemberBankAccount);
			
			Set<PolicyMemberAppointeeEntity> addPolicyMemberAppointeeEntity = new HashSet<PolicyMemberAppointeeEntity>();
			
			for (StagingPolicyMemberAppointeeEntity stagingPolicyMemberAppointeeEntity : me.getAppointees()) {
				PolicyMemberAppointeeEntity policyMemberAppointeeEntity=new ModelMapper().map(stagingPolicyMemberAppointeeEntity,PolicyMemberAppointeeEntity.class);
				policyMemberAppointeeEntity.setId(null);
				policyMemberAppointeeEntity.setMember(policyMemberEntity);
				addPolicyMemberAppointeeEntity.add(policyMemberAppointeeEntity);
			
				StagingPolicyMemberNomineeEntity stagingPolicyMemberNomineeEntity = stagingPolicyMemberAppointeeEntity.getNominee();
				PolicyMemberNomineeEntity policyMemberNomineeEntity =new ModelMapper().map(stagingPolicyMemberNomineeEntity, PolicyMemberNomineeEntity.class);
				policyMemberNomineeEntity.setId(null);
				policyMemberNomineeEntity.setMember(policyMemberEntity);
				policyMemberAppointeeEntity.setNominee(policyMemberNomineeEntity);				
				policyMemberEntity.getAppointees().add(policyMemberAppointeeEntity);
			
				for (Iterator<StagingPolicyMemberNomineeEntity> iterator = me.getNominees().iterator(); iterator.hasNext();) {
					StagingPolicyMemberNomineeEntity s =  iterator.next();
					if (s.getNomineeName() == policyMemberAppointeeEntity.getNominee().getNomineeName() 
				    		&& s.getNomineeAadharNumber() == policyMemberAppointeeEntity.getNominee().getNomineeAadharNumber()
				    		&& s.getDateOfBirth() == policyMemberAppointeeEntity.getNominee().getDateOfBirth()){
				        iterator.remove();
				    }    
				}
			}
			policyMemberEntity.setAppointees(addPolicyMemberAppointeeEntity);
		
			Set<PolicyMemberNomineeEntity> addPolicyMemberNomineeEntity = new HashSet<PolicyMemberNomineeEntity>();	
			for (StagingPolicyMemberNomineeEntity stagingPolicyMemberNomineeEntity : me.getNominees()) {
				PolicyMemberNomineeEntity policyMemberNomineeEntity =new ModelMapper().map(stagingPolicyMemberNomineeEntity, PolicyMemberNomineeEntity.class);
				policyMemberNomineeEntity.setId(null);
				policyMemberNomineeEntity.setMember(policyMemberEntity);
				addPolicyMemberNomineeEntity.add(policyMemberNomineeEntity);
				
			}
			policyMemberEntity.setNominees(addPolicyMemberNomineeEntity);
			
			members.add(policyMemberEntity);
		}
		return members;
	}

	public static Long nextLICNumber(Long maxLICNumber) {
		maxLICNumber = maxLICNumber == null ? 1 : maxLICNumber + 1;
		return maxLICNumber;
	}

	public static List<PolicyMemberDto> fromStagingEntities(List<StagingPolicyMemberEntity> entities) {
		List<PolicyMemberDto> members = new ArrayList<PolicyMemberDto>();

		for (StagingPolicyMemberEntity me : entities) {
			PolicyMemberDto mdto = new ModelMapper().map(me, PolicyMemberDto.class);
			if (me.getAddresses() != null && !me.getAddresses().isEmpty()) {
				mdto.setAddresses(
						me.getAddresses().stream().map(PolicyMemberHelper::addressEntityToDto).collect(Collectors.toList()));
			}
			if (me.getBankAccounts() != null && !me.getBankAccounts().isEmpty()) {
				mdto.setBankAccounts(me.getBankAccounts().stream().map(PolicyMemberHelper::bankAccountEntityToDto)
						.collect(Collectors.toList()));
			}
			if (me.getNominees() != null && !me.getNominees().isEmpty()) {
				mdto.setNominees(
						me.getNominees().stream().map(PolicyMemberHelper::nomineeEntityToDto).collect(Collectors.toList()));
			}
			if (me.getAppointees() != null && !me.getAppointees().isEmpty()) {
				List<PolicyMemberAppointeeDto> memberApponteeDtos = new ArrayList<PolicyMemberAppointeeDto>();
				for (StagingPolicyMemberAppointeeEntity mae : me.getAppointees()) {
					PolicyMemberAppointeeDto mad = appointeeEntityToDto(mae);
					mad.setNominee(nomineeEntityToDto(mae.getNominee()));
					memberApponteeDtos.add(mad);
				}
				mdto.setAppointees(memberApponteeDtos);
			}

			members.add(mdto);
		}

		return members;
	}

//member
	public static HistoryMemberEntity policymastertohistoryupdate(PolicyMemberEntity policyMemberEntity,Long hisPolicyId,
			String username) {
		HistoryMemberEntity historyMemberEntity = new ModelMapper().map(policyMemberEntity, HistoryMemberEntity.class);
		historyMemberEntity.setId(null);
		if (policyMemberEntity.getModifiedDate() == null) {
			historyMemberEntity.setPolicyId(hisPolicyId);
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
			historyMemberEntity.setPolicyId(hisPolicyId);
			historyMemberEntity.setEffectiveFromDate(effictiveDate);
			historyMemberEntity.setEffectiveToDate(new Date());
			historyMemberEntity.setCreatedBy(username);
			historyMemberEntity.setCreatedDate(new Date());
			historyMemberEntity.setModifiedBy(null);
			historyMemberEntity.setModifiedDate(new Date());
		}
		// end

		// Member Address
		Set<HistoryMemberAddressEntity> addhistoryMemberAddressEntity = new HashSet<HistoryMemberAddressEntity>();
		for (PolicyMemberAddressEntity policyMemberAddressEntity : policyMemberEntity.getAddresses()) {

			HistoryMemberAddressEntity historyMemberAddressEntities = new ModelMapper().map(policyMemberAddressEntity,
					HistoryMemberAddressEntity.class);
			historyMemberAddressEntities.setId(null);
			historyMemberAddressEntities.setMember(historyMemberEntity);
			addhistoryMemberAddressEntity.add(historyMemberAddressEntities);
		}
		historyMemberEntity.setAddresses(addhistoryMemberAddressEntity);
		// end

		// Bank Account
		Set<HistoryMemberBankAccountEntity> addHistoryMemberBankAccountEntity = new HashSet<HistoryMemberBankAccountEntity>();
		for (PolicyMemberBankAccount PolicyMemberBankAccount : policyMemberEntity.getBankAccounts()) {

			HistoryMemberBankAccountEntity historyMemberBankAccountEntities = new ModelMapper()
					.map(PolicyMemberBankAccount, HistoryMemberBankAccountEntity.class);
			historyMemberBankAccountEntities.setId(null);
			historyMemberBankAccountEntities.setMember(historyMemberEntity);
			addHistoryMemberBankAccountEntity.add(historyMemberBankAccountEntities);
		}
		historyMemberEntity.setBankAccounts(addHistoryMemberBankAccountEntity);
		// end

		// Member Appointee
		Set<HistoryMemberAppointeeEntity> addPolicyMemberAppointeeEntity = new HashSet<HistoryMemberAppointeeEntity>();
		for (PolicyMemberAppointeeEntity policyMemberAppointeeEntity : policyMemberEntity.getAppointees()) {

			HistoryMemberAppointeeEntity historyMemberAppointeeEntities = new ModelMapper()
					.map(policyMemberAppointeeEntity, HistoryMemberAppointeeEntity.class);
			historyMemberAppointeeEntities.setId(null);
			historyMemberAppointeeEntities.setMember(historyMemberEntity);
			addPolicyMemberAppointeeEntity.add(historyMemberAppointeeEntities);

			PolicyMemberNomineeEntity newPolicyMemberNomineeEntity = policyMemberAppointeeEntity.getNominee();
			HistoryMemberNomineeEntity historyMemberNomineeEntity = new ModelMapper().map(newPolicyMemberNomineeEntity,
					HistoryMemberNomineeEntity.class);
			historyMemberNomineeEntity.setId(null);
			historyMemberNomineeEntity.setMember(historyMemberEntity);
			historyMemberAppointeeEntities.setNominee(historyMemberNomineeEntity);
			historyMemberEntity.getAppointees().add(historyMemberAppointeeEntities);

			for (Iterator<PolicyMemberNomineeEntity> iterator = policyMemberEntity.getNominees().iterator(); iterator
					.hasNext();) {
				PolicyMemberNomineeEntity s = iterator.next();
				if (s.getNomineeName() == historyMemberAppointeeEntities.getNominee().getNomineeName()
						&& s.getNomineeAadharNumber() == historyMemberAppointeeEntities.getNominee()
								.getNomineeAadharNumber()
						&& s.getDateOfBirth() == historyMemberAppointeeEntities.getNominee().getDateOfBirth()) {
					iterator.remove();
				}
			}

		}
		historyMemberEntity.setAppointees(addPolicyMemberAppointeeEntity);
		// end

		// Member Nominee
		Set<HistoryMemberNomineeEntity> addHistoryMemberNomineeEntity = new HashSet<HistoryMemberNomineeEntity>();
		for (PolicyMemberNomineeEntity policyMemberNomineeEntity : policyMemberEntity.getNominees()) {

			HistoryMemberNomineeEntity historyMemberNomineeEntities = new ModelMapper().map(policyMemberNomineeEntity,
					HistoryMemberNomineeEntity.class);
			historyMemberNomineeEntities.setId(null);
			historyMemberNomineeEntities.setMember(historyMemberEntity);
			addHistoryMemberNomineeEntity.add(historyMemberNomineeEntities);
		}
		historyMemberEntity.setNominees(addHistoryMemberNomineeEntity);
		// end
		return historyMemberEntity;
	}
}



