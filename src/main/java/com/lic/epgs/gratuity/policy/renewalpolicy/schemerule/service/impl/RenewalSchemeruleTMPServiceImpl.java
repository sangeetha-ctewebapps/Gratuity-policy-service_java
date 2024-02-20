package com.lic.epgs.gratuity.policy.renewalpolicy.schemerule.service.impl;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lic.epgs.gratuity.common.dto.ApiResponseDto;
import com.lic.epgs.gratuity.common.dto.MemberCategoryDto;
import com.lic.epgs.gratuity.common.entity.MemberCategoryEntity;
import com.lic.epgs.gratuity.common.entity.MemberCategoryModuleEntity;
import com.lic.epgs.gratuity.common.repository.MemberCategoryModuleRepository;
import com.lic.epgs.gratuity.common.repository.MemberCategoryRepository;
import com.lic.epgs.gratuity.policy.renewalpolicy.gratuitybenefit.dto.RenewalGratuityBenefitPropsTMPDto;
import com.lic.epgs.gratuity.policy.renewalpolicy.gratuitybenefit.dto.RenewalGratuityBenefitTMPDto;
import com.lic.epgs.gratuity.policy.renewalpolicy.gratuitybenefit.entity.RenewalGratuityBenefitTMPEntity;
import com.lic.epgs.gratuity.policy.renewalpolicy.gratuitybenefit.entity.RenewalsGratuityBenefitPropsTMPEntity;
import com.lic.epgs.gratuity.policy.renewalpolicy.gratuitybenefit.helper.RenewalGratuityBenefitTMPHelper;
import com.lic.epgs.gratuity.policy.renewalpolicy.gratuitybenefit.repository.RenewalGratuityBenefitPropsTMPRepository;
import com.lic.epgs.gratuity.policy.renewalpolicy.gratuitybenefit.repository.RenewalGratuityBenefitTMPRepository;
import com.lic.epgs.gratuity.policy.renewalpolicy.lifecover.dto.RenewalLifeCoverTMPDto;
import com.lic.epgs.gratuity.policy.renewalpolicy.lifecover.entity.RenewalLifeCoverTMPEntity;
import com.lic.epgs.gratuity.policy.renewalpolicy.lifecover.helper.RenewalLifeCoveTMPHelper;
import com.lic.epgs.gratuity.policy.renewalpolicy.lifecover.repository.RenewalLifeCoverTMPRepository;
import com.lic.epgs.gratuity.policy.renewalpolicy.schemerule.dto.RenewalLifeCoverAndGratuityDto;
import com.lic.epgs.gratuity.policy.renewalpolicy.schemerule.helper.RenewalSchemeruleTMPHelper;
import com.lic.epgs.gratuity.policy.renewalpolicy.schemerule.repository.RenewalSchemeruleTMPRepository;
import com.lic.epgs.gratuity.policy.renewalpolicy.schemerule.service.RenewalSchemeruleTMPService;
import com.lic.epgs.gratuity.policy.renewalpolicy.valuationmatrix.dto.RenewalValuationDetailDto;
import com.lic.epgs.gratuity.quotation.gratuitybenefit.entity.GratuityBenefitEntity;
import com.lic.epgs.gratuity.quotation.gratuitybenefit.entity.GratuityBenefitPropsEntity;
import com.lic.epgs.gratuity.quotation.lifecover.dto.LifeCoverAndGratuityDto;
import com.lic.epgs.gratuity.quotation.lifecover.entity.LifeCoverEntity;
import com.lic.epgs.gratuity.quotation.lifecover.helper.LifeCoverHelper;
import com.lic.epgs.gratuity.quotation.schemerule.helper.SchemeRuleHelper;


@Service
public class RenewalSchemeruleTMPServiceImpl implements  RenewalSchemeruleTMPService {
	
	@Autowired
	private EntityManager entityManager;
	
	@Autowired
	private MemberCategoryRepository memberCategoryRepository;
	
	@Autowired
	private RenewalLifeCoverTMPRepository  renewalLifeCoverTMPRepository;
		
	@Autowired
	 private RenewalGratuityBenefitTMPRepository renewalGratuityBenefitTMPRepository ;
	
	@Autowired
	 private RenewalGratuityBenefitPropsTMPRepository renewalGratuityBenefitPropsTMPRepository;
	
	@Autowired
	private RenewalLifeCoverTMPRepository renewalLifeCoverTMPEntityRepository;
	
	@Autowired
	private MemberCategoryModuleRepository memberCategoryModuleRepository;
	
//	@Autowired
//	private RenewalLifeCoverTMPRepository renewalLifeCoverTMPRepository;
//	
	@Override
	public ApiResponseDto<List<RenewalLifeCoverAndGratuityDto>> createLifeCoverandGratuity(
			RenewalLifeCoverAndGratuityDto renewalLifeCoverAndGratuityDto) {
		
		MemberCategoryEntity memberCategoryEntity =new ModelMapper().map(renewalLifeCoverAndGratuityDto, MemberCategoryEntity.class);
		memberCategoryEntity.setId(null);
		memberCategoryEntity.setPmstTmpPolicy(renewalLifeCoverAndGratuityDto.getTmpPolicyId());
		memberCategoryEntity.setName(renewalLifeCoverAndGratuityDto.getCategoryName());
		memberCategoryEntity.setIsActive(true);
		memberCategoryEntity.setCreatedBy(renewalLifeCoverAndGratuityDto.getCreatedBy());
		memberCategoryEntity.setCreatedDate(new Date());
		memberCategoryEntity=memberCategoryRepository.save(memberCategoryEntity);
		MemberCategoryModuleEntity memberCategoryModuleEntity=new MemberCategoryModuleEntity();
		memberCategoryModuleEntity.setTmpPolicyId(renewalLifeCoverAndGratuityDto.getTmpPolicyId());
		memberCategoryModuleEntity.setMemberCategoryId(memberCategoryEntity.getId());
		memberCategoryModuleEntity.setCreatedBy(renewalLifeCoverAndGratuityDto.getCreatedBy());
		memberCategoryModuleEntity.setCreatedDate(new Date());
		memberCategoryModuleEntity.setIsActive(true);
		memberCategoryModuleRepository.save(memberCategoryModuleEntity);
		RenewalLifeCoverTMPEntity renewalLifeCoverTMPEntity=new ModelMapper().map(renewalLifeCoverAndGratuityDto, RenewalLifeCoverTMPEntity.class);
		renewalLifeCoverTMPEntity .setId(null);
		renewalLifeCoverTMPEntity .setCategoryId(memberCategoryEntity.getId());
		renewalLifeCoverTMPEntity.setCreatedDate(new Date());
		renewalLifeCoverTMPEntity.setCreatedBy(renewalLifeCoverAndGratuityDto.getCreatedBy());
		renewalLifeCoverTMPEntity.setIsActive(true);
		renewalLifeCoverTMPEntity=renewalLifeCoverTMPRepository.save(renewalLifeCoverTMPEntity);
			
		RenewalGratuityBenefitTMPEntity renewalGratuityBenefitTMPEntity = RenewalSchemeruleTMPHelper.dtoLifeandGratToEntity(renewalLifeCoverAndGratuityDto);
		renewalGratuityBenefitTMPEntity.setCategoryId(memberCategoryEntity.getId());
		Set<RenewalsGratuityBenefitPropsTMPEntity> gratuityBenefitPropsEntities = new HashSet<RenewalsGratuityBenefitPropsTMPEntity>();
		for (RenewalGratuityBenefitPropsTMPDto  gratuityBenefitPropsDto : renewalLifeCoverAndGratuityDto.getRenewalGratuityBenefitProps()) {
				
			 RenewalsGratuityBenefitPropsTMPEntity renewalsGratuityBenefitPropsTMPEntity = RenewalGratuityBenefitTMPHelper.dtoToEntity(gratuityBenefitPropsDto);
			
			renewalsGratuityBenefitPropsTMPEntity.setRenewalGratuityBenefitTMPEntity(renewalGratuityBenefitTMPEntity);
			renewalsGratuityBenefitPropsTMPEntity.setIsActive(true);
			renewalsGratuityBenefitPropsTMPEntity.setCreatedBy(gratuityBenefitPropsDto.getCreatedBy());
			renewalsGratuityBenefitPropsTMPEntity.setCreatedDate(new Date());
			gratuityBenefitPropsEntities.add(renewalsGratuityBenefitPropsTMPEntity);
		}

		renewalGratuityBenefitTMPEntity.setRenewalgratuityPropsBenefit(gratuityBenefitPropsEntities);

		renewalGratuityBenefitTMPEntity.setIsActive(true);
		renewalGratuityBenefitTMPEntity.setCreatedBy(renewalLifeCoverAndGratuityDto.getCreatedBy());
		renewalGratuityBenefitTMPEntity.setCreatedDate(new Date());
		renewalGratuityBenefitTMPEntity = renewalGratuityBenefitTMPRepository.save(renewalGratuityBenefitTMPEntity);	
		
		List<RenewalLifeCoverTMPDto> renewalLifeCoverTMPDto =(renewalLifeCoverTMPRepository.findBytmpPolicyId(renewalLifeCoverAndGratuityDto.getTmpPolicyId()).stream().map(RenewalLifeCoveTMPHelper::entityToDto).
				collect(Collectors.toList()));
		List<RenewalGratuityBenefitTMPDto> renewalGratuityBenefitTMPDto= RenewalGratuityBenefitTMPHelper.entitesToDto(renewalGratuityBenefitTMPRepository.findBytmpPolicyId(renewalLifeCoverAndGratuityDto.getTmpPolicyId()));	
//		List<MemberCategoryEntity> getMemberCategoryEntity=memberCategoryRepository.findByTmpPolicyId(renewalLifeCoverAndGratuityDto.getTmpPolicyId());
		List<MemberCategoryDto> getMemberCategoryDto=(memberCategoryRepository.findByTmpPolicyId(renewalLifeCoverAndGratuityDto.getTmpPolicyId()).stream().map(RenewalLifeCoveTMPHelper::memberEntitestoDto).
				collect(Collectors.toList()));
		return ApiResponseDto.success(RenewalSchemeruleTMPHelper.lifeandGratToDto(renewalLifeCoverTMPDto,getMemberCategoryDto,renewalGratuityBenefitTMPDto));

			
	}

	@Override
	public ApiResponseDto<List<RenewalLifeCoverAndGratuityDto>> updateLifeCoverandGratuity(
			RenewalLifeCoverAndGratuityDto renewallifeCoverAndGratuityDto) {
			
		RenewalLifeCoverTMPEntity renewalLifeCoverTMPEntity = renewalLifeCoverTMPEntityRepository.findById(renewallifeCoverAndGratuityDto.getId()).get();
		renewalLifeCoverTMPEntity.setMaximumSumAssured(renewallifeCoverAndGratuityDto.getMaximumSumAssured());
		renewalLifeCoverTMPEntity.setMinimumSumAssured(renewallifeCoverAndGratuityDto.getMinimumSumAssured());
		renewalLifeCoverTMPEntity.setNoofMonth(renewallifeCoverAndGratuityDto.getNoOfMonths());
		renewalLifeCoverTMPEntity.setRetentionLimit(renewallifeCoverAndGratuityDto.getRetentionLimit());
		renewalLifeCoverTMPEntity.setRetirementAge(renewallifeCoverAndGratuityDto.getRetirementAge());
		renewalLifeCoverTMPEntity.setSumAssured(renewallifeCoverAndGratuityDto.getSumAssured());
		renewalLifeCoverTMPEntity.setCategoryId(renewallifeCoverAndGratuityDto.getCategoryId());
		renewalLifeCoverTMPEntity.setModifiedBy(renewallifeCoverAndGratuityDto.getModifiedBy());
		renewalLifeCoverTMPEntity.setModifiedDate(new Date());
		renewalLifeCoverTMPEntity.setIsActive(true);
		renewalLifeCoverTMPEntity = renewalLifeCoverTMPEntityRepository.save(renewalLifeCoverTMPEntity);
		
		RenewalGratuityBenefitTMPEntity renewalGratuityBenefitTMPEntity = renewalGratuityBenefitTMPRepository.findById(renewallifeCoverAndGratuityDto.getGratuityBenefitId()).get();
		renewalGratuityBenefitTMPEntity.setGratutiyBenefitTypeId(renewallifeCoverAndGratuityDto.getGratutiyBenefitTypeId());
		renewalGratuityBenefitTMPEntity.setGratutiySubBenefitId(renewallifeCoverAndGratuityDto.getGratutiySubBenefitId());
		Set<RenewalsGratuityBenefitPropsTMPEntity> renewalsGratuityBenefitProp = new HashSet<RenewalsGratuityBenefitPropsTMPEntity>();
		for (RenewalGratuityBenefitPropsTMPDto renewalGratuityBenefitPropsTMPDto : renewallifeCoverAndGratuityDto.getRenewalGratuityBenefitProps()) {
			RenewalsGratuityBenefitPropsTMPEntity renewalsGratuityBenefitPropsTMPEntity = renewalGratuityBenefitPropsTMPRepository.findById(renewalGratuityBenefitPropsTMPDto.getId()).get();
			renewalsGratuityBenefitPropsTMPEntity.setRetirementAge(renewalGratuityBenefitPropsTMPDto.getRetirementAge());
			renewalsGratuityBenefitPropsTMPEntity.setMonthlyCelling(renewalGratuityBenefitPropsTMPDto.getMonthlyCelling());
			renewalsGratuityBenefitPropsTMPEntity.setNumberOfDaysWage(renewalGratuityBenefitPropsTMPDto.getNumberOfDaysWage());
			renewalsGratuityBenefitPropsTMPEntity.setNumberOfWorkingDaysPerMonth(renewalGratuityBenefitPropsTMPDto.getNumberOfWorkingDaysPerMonth());
			renewalsGratuityBenefitPropsTMPEntity.setNumberOfYearsOfService(renewalGratuityBenefitPropsTMPDto.getNumberOfYearsOfService());
			renewalsGratuityBenefitPropsTMPEntity.setGratuityCellingAmount(renewalGratuityBenefitPropsTMPDto.getGratuityCellingAmount());
			renewalsGratuityBenefitPropsTMPEntity.setRenewalGratuityBenefitTMPEntity(renewalGratuityBenefitTMPEntity);
			renewalsGratuityBenefitPropsTMPEntity.setSalaryCelling(renewalGratuityBenefitPropsTMPDto.getSalaryCelling());
			renewalsGratuityBenefitPropsTMPEntity.setModifiedBy(renewalGratuityBenefitPropsTMPDto.getModifiedBy());
			renewalsGratuityBenefitPropsTMPEntity.setModifiedDate(new Date());
			renewalsGratuityBenefitPropsTMPEntity.setIsActive(true);
			renewalsGratuityBenefitProp.add(renewalsGratuityBenefitPropsTMPEntity);
		}

		renewalGratuityBenefitTMPEntity.setRenewalgratuityPropsBenefit(renewalsGratuityBenefitProp);
		renewalGratuityBenefitTMPEntity.setCategoryId(renewallifeCoverAndGratuityDto.getCategoryId());
		renewalGratuityBenefitTMPEntity.setModifiedBy(renewallifeCoverAndGratuityDto.getModifiedBy());
		renewalGratuityBenefitTMPEntity.setModifiedDate(new Date());
		renewalGratuityBenefitTMPEntity.setIsActive(true);
		
		renewalGratuityBenefitTMPEntity=renewalGratuityBenefitTMPRepository.save(renewalGratuityBenefitTMPEntity);
		
		List<RenewalLifeCoverTMPDto> renewalLifeCoverTMPDto =(renewalLifeCoverTMPRepository.findBytmpPolicyId(renewallifeCoverAndGratuityDto.getTmpPolicyId()).stream().map(RenewalLifeCoveTMPHelper::entityToDto).
				collect(Collectors.toList()));
		List<RenewalGratuityBenefitTMPDto> renewalGratuityBenefitTMPDto= RenewalGratuityBenefitTMPHelper.entitesToDto(renewalGratuityBenefitTMPRepository.findBytmpPolicyId(renewallifeCoverAndGratuityDto.getTmpPolicyId()));	
		
		List<MemberCategoryDto> getMemberCategoryDto=(memberCategoryRepository.findByTmpPolicyId(renewallifeCoverAndGratuityDto.getTmpPolicyId()).stream().map(LifeCoverHelper::memberEntitestoDto).
				collect(Collectors.toList()));
		return ApiResponseDto.success(RenewalSchemeruleTMPHelper.lifeandGratToDto(renewalLifeCoverTMPDto,getMemberCategoryDto,renewalGratuityBenefitTMPDto));
	
	}

	@Transactional
	@Override
	public ApiResponseDto<List<RenewalLifeCoverAndGratuityDto>> delete(Long policyId, Long id) {

		Optional< RenewalsGratuityBenefitPropsTMPEntity>  renewalsGratuityBenefitPropsTMPEntity = renewalGratuityBenefitPropsTMPRepository.findById(id);
		if ( renewalsGratuityBenefitPropsTMPEntity.isPresent()) {
			entityManager.createQuery("delete from RenewalsGratuityBenefitPropsTMPEntity where id = :id")
			  .setParameter("id", id)
			  .executeUpdate();
			List<RenewalLifeCoverTMPDto> renewalLifeCoverTMPDto =(renewalLifeCoverTMPRepository.findBytmpPolicyId(policyId).stream().map(RenewalLifeCoveTMPHelper::entityToDto).
					collect(Collectors.toList()));
			List<RenewalGratuityBenefitTMPDto> renewalGratuityBenefitTMPDto= RenewalGratuityBenefitTMPHelper.entitesToDto(renewalGratuityBenefitTMPRepository.findBytmpPolicyId(policyId));	
			
			List<MemberCategoryDto> getMemberCategoryDto=(memberCategoryRepository.findByTmpPolicyId(policyId).stream().map(LifeCoverHelper::memberEntitestoDto).
					collect(Collectors.toList()));
			return ApiResponseDto.success(RenewalSchemeruleTMPHelper.lifeandGratToDto(renewalLifeCoverTMPDto,getMemberCategoryDto,renewalGratuityBenefitTMPDto));
		
		}
		else {
			return ApiResponseDto.notFound(null);
		}   		
	}
	
	@Transactional
	@Override
	public ApiResponseDto<List<RenewalLifeCoverAndGratuityDto>> deleteLifeandGratuity(Long policyId, Long lifecoverID,
			Long gratuityId) {	

		Optional<RenewalGratuityBenefitTMPEntity> renewalGratuityBenefitTMPEntity = renewalGratuityBenefitTMPRepository.findById(gratuityId);
		Optional<RenewalLifeCoverTMPEntity> renewalLifeCoverTMPEntity = renewalLifeCoverTMPRepository.findById(lifecoverID);
		
		Long getMemberCategoryModule=memberCategoryModuleRepository.findByTempIdandMemberCategoryId(renewalGratuityBenefitTMPEntity.get().getCategoryId(),policyId);
		
		if (renewalGratuityBenefitTMPEntity.isPresent() && renewalLifeCoverTMPEntity.isPresent()) {
			
		
			memberCategoryModuleRepository.deleteById(getMemberCategoryModule);
			memberCategoryRepository.deleteById(renewalGratuityBenefitTMPEntity.get().getCategoryId());
			renewalGratuityBenefitTMPRepository.deleteById(gratuityId);
			renewalLifeCoverTMPRepository.deleteById(lifecoverID);
		}
		List<RenewalLifeCoverTMPDto> renewalLifeCoverTMPDto =(renewalLifeCoverTMPRepository.findBytmpPolicyId(policyId).stream().map(RenewalLifeCoveTMPHelper::entityToDto).
				collect(Collectors.toList()));
		List<RenewalGratuityBenefitTMPDto> renewalGratuityBenefitTMPDto= RenewalGratuityBenefitTMPHelper.entitesToDto(renewalGratuityBenefitTMPRepository.findBytmpPolicyId(policyId));	
		
		List<MemberCategoryDto> getMemberCategoryDto=(memberCategoryRepository.findByTmpPolicyId(policyId).stream().map(LifeCoverHelper::memberEntitestoDto).
				collect(Collectors.toList()));
		return ApiResponseDto.success(RenewalSchemeruleTMPHelper.lifeandGratToDto(renewalLifeCoverTMPDto,getMemberCategoryDto,renewalGratuityBenefitTMPDto));
	
	}

	@Override
	public ApiResponseDto<List<RenewalLifeCoverAndGratuityDto>> findAll(Long policyId, String type) {
		if(type.equals("INPROGRESS")) {
			List<RenewalLifeCoverTMPDto> renewalLifeCoverTMPDto =(renewalLifeCoverTMPRepository.findBytmpPolicyId(policyId).stream().map(RenewalLifeCoveTMPHelper::entityToDto).
					collect(Collectors.toList()));
			List<RenewalGratuityBenefitTMPDto> renewalGratuityBenefitTMPDto= RenewalGratuityBenefitTMPHelper.entitesToDto(renewalGratuityBenefitTMPRepository.findBytmpPolicyId(policyId));	
			List<Long> policy = renewalGratuityBenefitTMPRepository.findBytmpPolicy(policyId);
			List<MemberCategoryDto> getMemberCategoryDto=(memberCategoryRepository.findByTmpPolicyId(policyId).stream().map(LifeCoverHelper::memberEntitestoDto).
					collect(Collectors.toList()));
			return ApiResponseDto.success(RenewalSchemeruleTMPHelper.lifeandGratToDto(renewalLifeCoverTMPDto,getMemberCategoryDto,renewalGratuityBenefitTMPDto));
		
		}else {
			return ApiResponseDto.success(null);
		}
		
	}
}
