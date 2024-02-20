package com.lic.epgs.gratuity.quotation.schemerule.service.impl;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.ModelMap;

import com.lic.epgs.gratuity.common.dto.ApiResponseDto;
import com.lic.epgs.gratuity.common.dto.MemberCategoryDto;
import com.lic.epgs.gratuity.common.entity.MemberCategoryEntity;
import com.lic.epgs.gratuity.common.repository.MemberCategoryRepository;
import com.lic.epgs.gratuity.quotation.gratuitybenefit.dto.GratuityBenefitDto;
import com.lic.epgs.gratuity.quotation.gratuitybenefit.dto.GratuityBenefitPropsDto;
import com.lic.epgs.gratuity.quotation.gratuitybenefit.entity.GratuityBenefitEntity;
import com.lic.epgs.gratuity.quotation.gratuitybenefit.entity.GratuityBenefitPropsEntity;
import com.lic.epgs.gratuity.quotation.gratuitybenefit.entity.MasterGratuityBenefitEntity;
import com.lic.epgs.gratuity.quotation.gratuitybenefit.helper.GratuityBenefitHelper;
import com.lic.epgs.gratuity.quotation.gratuitybenefit.repository.GratuityBenefitPropsRepository;
import com.lic.epgs.gratuity.quotation.gratuitybenefit.repository.GratuityBenefitRepository;
import com.lic.epgs.gratuity.quotation.gratuitybenefit.repository.MasterGratuityBenefitRepository;
import com.lic.epgs.gratuity.quotation.lifecover.dto.LifeCoverAndGratuityDto;
import com.lic.epgs.gratuity.quotation.lifecover.dto.LifeCoverDto;
import com.lic.epgs.gratuity.quotation.lifecover.entity.LifeCoverEntity;
import com.lic.epgs.gratuity.quotation.lifecover.entity.MasterLifeCoverEntity;
import com.lic.epgs.gratuity.quotation.lifecover.helper.LifeCoverHelper;
import com.lic.epgs.gratuity.quotation.lifecover.repository.LifeCoverEntityRepository;
import com.lic.epgs.gratuity.quotation.lifecover.repository.MasterLifeCoverEntityRepository;
import com.lic.epgs.gratuity.quotation.schemerule.dto.NewSchemeRuleDto;
import com.lic.epgs.gratuity.quotation.schemerule.dto.SchemeRuleDto;
import com.lic.epgs.gratuity.quotation.schemerule.entity.MasterSchemeRuleEntity;
import com.lic.epgs.gratuity.quotation.schemerule.entity.SchemeRuleEntity;
import com.lic.epgs.gratuity.quotation.schemerule.helper.SchemeRuleHelper;
import com.lic.epgs.gratuity.quotation.schemerule.repository.MasterSchemeRuleRepository;
import com.lic.epgs.gratuity.quotation.schemerule.repository.SchemeRuleRepository;
import com.lic.epgs.gratuity.quotation.schemerule.service.SchemeRuleService;

/**
 * @author Gopi
 *
 */

@Service
public class SchemeRuleServiceImpl implements SchemeRuleService {


	@Autowired
	private EntityManager entityManager;
	
	@Autowired
	private SchemeRuleRepository schemeRuleRepository;
	
	@Autowired
	private GratuityBenefitRepository gratuityBenefitRepository;
	
	@Autowired
	private LifeCoverEntityRepository lifeCoverEntityRepository;
	
	@Autowired
	private MasterSchemeRuleRepository masterSchemeRuleRepository;
	

	@Autowired
	private MemberCategoryRepository memberCategoryRepository;
	
	@Autowired
	private GratuityBenefitPropsRepository gratuityBenefitPropsRepository;
	
	@Autowired
	private MasterLifeCoverEntityRepository masterLifeCoverEntityRepository;
	
	@Autowired
	private MasterGratuityBenefitRepository masterGratuityBenefitRepository;

	@Override
	public ApiResponseDto<SchemeRuleDto> findByQuotationId(Long quotationId, String type) {
		if (type.equals("INPROGRESS")) {
			Optional<SchemeRuleEntity> entity = schemeRuleRepository.findByQuotationId(quotationId);
			if (entity.isPresent()) {
				return ApiResponseDto.success(SchemeRuleHelper.entityToDto(entity.get()));
			} else {
				return ApiResponseDto.notFound(new SchemeRuleDto());
			}
		} else {
			MasterSchemeRuleEntity entity = masterSchemeRuleRepository.findByQuotationId(quotationId);
			if (entity == null) {
				return ApiResponseDto.notFound(new SchemeRuleDto());
			} else {
				return ApiResponseDto.success(SchemeRuleHelper.entityToDto(entity));
			}
		}
		
	}

	@Override
	public ApiResponseDto<SchemeRuleDto> create(NewSchemeRuleDto newSchemeRuleDto) {
		SchemeRuleEntity schemeRuleEntity = new ModelMapper().map(newSchemeRuleDto, SchemeRuleEntity.class);
		
		schemeRuleEntity.setIsActive(true);
		schemeRuleEntity.setCreatedBy(newSchemeRuleDto.getCreatedBy());
		schemeRuleEntity.setCreatedDate(new Date());
		
		return ApiResponseDto.created(SchemeRuleHelper.entityToDto(schemeRuleRepository.save(schemeRuleEntity)));
	}

	@Override
	public ApiResponseDto<SchemeRuleDto> update(Long id, SchemeRuleDto schemeRuleDto) {
		SchemeRuleEntity schemeRuleEntity = schemeRuleRepository.findById(id).get();
		SchemeRuleEntity newSchemeRuleEntity = SchemeRuleHelper.dtoToEntity(schemeRuleDto);
		
		newSchemeRuleEntity.setId(id);
		newSchemeRuleEntity.setIsActive(schemeRuleEntity.getIsActive());
		newSchemeRuleEntity.setCreatedBy(schemeRuleEntity.getCreatedBy());
		newSchemeRuleEntity.setCreatedDate(schemeRuleEntity.getCreatedDate());
		newSchemeRuleEntity.setModifiedBy(schemeRuleDto.getModifiedBy());
		newSchemeRuleEntity.setModifiedDate(new Date());
		
		return ApiResponseDto.success(SchemeRuleHelper.entityToDto(schemeRuleRepository.save(newSchemeRuleEntity)));
	}
	@Transactional
	@Override
	public ApiResponseDto<List<LifeCoverAndGratuityDto>> createLifeCoverandGratuity(
			LifeCoverAndGratuityDto lifeCoverAndGratuityDto) {
		MemberCategoryEntity memberCategoryEntity =new ModelMapper().map(lifeCoverAndGratuityDto, MemberCategoryEntity.class);
		memberCategoryEntity.setId(null);
		memberCategoryEntity.setQstgQuoationId(lifeCoverAndGratuityDto.getQuotationId());
		memberCategoryEntity.setName(lifeCoverAndGratuityDto.getCategoryName());
		memberCategoryEntity.setIsActive(true);
		memberCategoryEntity.setCreatedBy(lifeCoverAndGratuityDto.getCreatedBy());
		memberCategoryEntity.setCreatedDate(new Date());
		memberCategoryEntity=memberCategoryRepository.save(memberCategoryEntity);
		
		LifeCoverEntity lifeCoverEntity=new ModelMapper().map(lifeCoverAndGratuityDto, LifeCoverEntity.class);
		lifeCoverEntity.setId(null);
		lifeCoverEntity.setCategoryId(memberCategoryEntity.getId());
		lifeCoverEntity.setCreatedDate(new Date());
		lifeCoverEntity.setCreatedBy(lifeCoverAndGratuityDto.getCreatedBy());
		lifeCoverEntity.setIsActive(true);
		lifeCoverEntity=lifeCoverEntityRepository.save(lifeCoverEntity);
			
		GratuityBenefitEntity gratuityBenefitEntity = SchemeRuleHelper.dtoLifeandGratToEntity(lifeCoverAndGratuityDto);
		gratuityBenefitEntity.setCategoryId(memberCategoryEntity.getId());
		Set<GratuityBenefitPropsEntity> gratuityBenefitPropsEntities = new HashSet<GratuityBenefitPropsEntity>();
		for (GratuityBenefitPropsDto gratuityBenefitPropsDto : lifeCoverAndGratuityDto.getGratuityBenefitProps()) {
			
			GratuityBenefitPropsEntity gratuityBenefitPropsEntity = GratuityBenefitHelper.dtoToEntity(gratuityBenefitPropsDto);
			gratuityBenefitPropsEntity.setGratuityBenefit(gratuityBenefitEntity);
			gratuityBenefitPropsEntity.setIsActive(true);
			gratuityBenefitPropsEntity.setCreatedBy(gratuityBenefitPropsDto.getCreatedBy());
			gratuityBenefitPropsEntity.setCreatedDate(new Date());
			gratuityBenefitPropsEntities.add(gratuityBenefitPropsEntity);
		}

		gratuityBenefitEntity.setGratuityBenefits(gratuityBenefitPropsEntities);

		gratuityBenefitEntity.setIsActive(true);
		gratuityBenefitEntity.setCreatedBy(lifeCoverAndGratuityDto.getCreatedBy());
		gratuityBenefitEntity.setCreatedDate(new Date());

		gratuityBenefitEntity=gratuityBenefitRepository.save(gratuityBenefitEntity);	
		List<LifeCoverDto> lifeCoverDto =(lifeCoverEntityRepository.findByQuotationId(lifeCoverAndGratuityDto.getQuotationId()).stream().map(LifeCoverHelper::entityToDto)
				.collect(Collectors.toList()));
		List<GratuityBenefitDto> gratuityBenefitDto=GratuityBenefitHelper.entitesToDto(gratuityBenefitRepository.findByQuotationId(lifeCoverAndGratuityDto.getQuotationId()));	
		
		List<MemberCategoryDto> getMemberCategoryDto=(memberCategoryRepository.findByqstgQuoationId(lifeCoverAndGratuityDto.getQuotationId()).stream().map(LifeCoverHelper::memberEntitestoDto).
				collect(Collectors.toList()));
		return ApiResponseDto.success(SchemeRuleHelper.lifeandGratToDto(lifeCoverDto,getMemberCategoryDto,gratuityBenefitDto));

	}

	@Override
	public ApiResponseDto<List<LifeCoverAndGratuityDto>> findAll(Long quotationId, String type) {
		if (type.equals("INPROGRESS")) {
			List<LifeCoverEntity> entity = lifeCoverEntityRepository.findByQuotationId(quotationId);
			List<GratuityBenefitEntity> gratuityBenefitEntity = gratuityBenefitRepository.findByQuotationId(quotationId);
			if (entity.size()!=0 && gratuityBenefitEntity.size()!=0) {	
				List<LifeCoverDto> lifeCoverDto =(lifeCoverEntityRepository.findByQuotationId(quotationId).stream().map(LifeCoverHelper::entityToDto)
						.collect(Collectors.toList()));
				List<GratuityBenefitDto> gratuityBenefitDto=GratuityBenefitHelper.entitesToDto(gratuityBenefitRepository.findByQuotationId(quotationId));	
				
				List<MemberCategoryDto> getMemberCategoryDto=(memberCategoryRepository.findByqstgQuoationId(quotationId).stream().map(LifeCoverHelper::memberEntitestoDto).
						collect(Collectors.toList()));
				return ApiResponseDto.success(SchemeRuleHelper.lifeandGratToDto(lifeCoverDto,getMemberCategoryDto,gratuityBenefitDto));
			
			} else {
				return ApiResponseDto.notFound(null);
			}
		} else {
			List<MasterLifeCoverEntity> entity = masterLifeCoverEntityRepository.findByQuotationId(quotationId);
			List<MasterGratuityBenefitEntity> gratuityBenefitEntity = masterGratuityBenefitRepository.findByQuotationId(quotationId);
			if (entity.size()!=0 && gratuityBenefitEntity.size()!=0) {	
				List<LifeCoverDto> lifeCoverDto =(masterLifeCoverEntityRepository.findByQuotationId(quotationId).stream().map(LifeCoverHelper::entityToDto)
						.collect(Collectors.toList()));
				List<GratuityBenefitDto> gratuityBenefitDto=GratuityBenefitHelper.masterEntitesToDto(masterGratuityBenefitRepository.findByQuotationId(quotationId));	
				
				List<MemberCategoryDto> getMemberCategoryDto=(memberCategoryRepository.findByqmstQuotationId(quotationId).stream().map(LifeCoverHelper::memberEntitestoDto).
						collect(Collectors.toList()));
				return ApiResponseDto.success(SchemeRuleHelper.lifeandGratToDto(lifeCoverDto,getMemberCategoryDto,gratuityBenefitDto));
			} else {
				return ApiResponseDto.notFound(null);
			}
		}
	}

	@Override
	public ApiResponseDto<List<LifeCoverAndGratuityDto>> updateLifeCoverandGratuity(
			LifeCoverAndGratuityDto lifeCoverAndGratuityDto) {
		
		
		LifeCoverEntity lifeCoverEntity = lifeCoverEntityRepository.findById(lifeCoverAndGratuityDto.getId()).get();
		lifeCoverEntity.setMaximumSumAssured(lifeCoverAndGratuityDto.getMaximumSumAssured());
		lifeCoverEntity.setMinimumSumAssured(lifeCoverAndGratuityDto.getMinimumSumAssured());
		lifeCoverEntity.setNoOfMonths(lifeCoverAndGratuityDto.getNoOfMonths());
		lifeCoverEntity.setRetentionLimit(lifeCoverAndGratuityDto.getRetentionLimit());
		lifeCoverEntity.setRetirementAge(lifeCoverAndGratuityDto.getRetirementAge());
		lifeCoverEntity.setSumAssured(lifeCoverAndGratuityDto.getSumAssured());
		lifeCoverEntity.setCategoryId(lifeCoverAndGratuityDto.getCategoryId());
		lifeCoverEntity.setModifiedBy(lifeCoverAndGratuityDto.getModifiedBy());
		lifeCoverEntity.setModifiedDate(new Date());
		lifeCoverEntity.setIsActive(true);
		lifeCoverEntity=lifeCoverEntityRepository.save(lifeCoverEntity);
		
		GratuityBenefitEntity gratuityBenefitEntity = gratuityBenefitRepository.findById(lifeCoverAndGratuityDto.getGratuityBenefitId()).get();
		gratuityBenefitEntity.setGratutiyBenefitTypeId(lifeCoverAndGratuityDto.getGratutiyBenefitTypeId());
		gratuityBenefitEntity.setGratutiySubBenefitId(lifeCoverAndGratuityDto.getGratutiySubBenefitId());
		Set<GratuityBenefitPropsEntity> gratuityBenefitPropsEntities = new HashSet<GratuityBenefitPropsEntity>();
		for (GratuityBenefitPropsDto gratuityBenefitPropsDto : lifeCoverAndGratuityDto.getGratuityBenefitProps()) {
			GratuityBenefitPropsEntity gratuityBenefitPropsEntity = gratuityBenefitPropsRepository.findById(gratuityBenefitPropsDto.getId()).get();
			gratuityBenefitPropsEntity.setRetirementAge(lifeCoverAndGratuityDto.getRetirementAge());
			gratuityBenefitPropsEntity.setMonthlyCelling(gratuityBenefitPropsDto.getMonthlyCelling());
			gratuityBenefitPropsEntity.setNumberOfDaysWage(gratuityBenefitPropsDto.getNumberOfDaysWage());
			gratuityBenefitPropsEntity.setNumberOfWorkingDaysPerMonth(gratuityBenefitPropsDto.getNumberOfWorkingDaysPerMonth());
			gratuityBenefitPropsEntity.setNumberOfYearsOfService(gratuityBenefitPropsDto.getNumberOfYearsOfService());
			gratuityBenefitPropsEntity.setGratuityCellingAmount(gratuityBenefitPropsDto.getGratuityCellingAmount());
			gratuityBenefitPropsEntity.setGratuityBenefit(gratuityBenefitEntity);
			gratuityBenefitPropsEntity.setSalaryCelling(gratuityBenefitPropsDto.getSalaryCelling());
//			gratuityBenefitPropsEntity.setMonthlyCelling(gratuityBenefitPropsDto.getMonthlyCelling());
			gratuityBenefitPropsEntity.setModifiedBy(gratuityBenefitPropsDto.getModifiedBy());
			gratuityBenefitPropsEntity.setModifiedDate(new Date());
			gratuityBenefitPropsEntity.setIsActive(true);
			gratuityBenefitPropsEntities.add(gratuityBenefitPropsEntity);
		}

		gratuityBenefitEntity.setGratuityBenefits(gratuityBenefitPropsEntities);
		gratuityBenefitEntity.setCategoryId(lifeCoverAndGratuityDto.getCategoryId());
		gratuityBenefitEntity.setModifiedBy(lifeCoverAndGratuityDto.getModifiedBy());
		gratuityBenefitEntity.setModifiedDate(new Date());
		gratuityBenefitEntity.setIsActive(true);
		

		gratuityBenefitEntity=gratuityBenefitRepository.save(gratuityBenefitEntity);
		
		List<LifeCoverDto> lifeCoverDto =(lifeCoverEntityRepository.findByQuotationId(lifeCoverAndGratuityDto.getQuotationId()).stream().map(LifeCoverHelper::entityToDto)
				.collect(Collectors.toList()));
		List<GratuityBenefitDto> gratuityBenefitDto=GratuityBenefitHelper.entitesToDto(gratuityBenefitRepository.findByQuotationId(lifeCoverAndGratuityDto.getQuotationId()));	
		
		List<MemberCategoryDto> getMemberCategoryDto=(memberCategoryRepository.findByqstgQuoationId(lifeCoverAndGratuityDto.getQuotationId()).stream().map(LifeCoverHelper::memberEntitestoDto).
				collect(Collectors.toList()));
		return ApiResponseDto.success(SchemeRuleHelper.lifeandGratToDto(lifeCoverDto,getMemberCategoryDto,gratuityBenefitDto));

	}
	
	@Transactional
	@Override
	public ApiResponseDto<List<LifeCoverAndGratuityDto>> delete(Long quotationId, Long id) {
		Optional<GratuityBenefitPropsEntity> gratuityBenefitPropsEntity = gratuityBenefitPropsRepository.findById(id);
		if (gratuityBenefitPropsEntity.isPresent()) {
			entityManager.createQuery("delete from GratuityBenefitPropsEntity where id = :id")
			  .setParameter("id", id)
			  .executeUpdate();
			return ApiResponseDto.success(SchemeRuleHelper.entiteslifeandGratToDto(lifeCoverEntityRepository.findByQuotationId(quotationId),
					memberCategoryRepository.findByqstgQuoationId(quotationId),gratuityBenefitRepository.findByQuotationId(quotationId)));
		}
		else {
			return ApiResponseDto.notFound(null);
		}
		 	
	}

	@Override
	public ApiResponseDto<List<LifeCoverAndGratuityDto>> deleteLifeandGratuity(Long quotationId, Long lifecoverID,
			Long gratuityId) {
		Optional<GratuityBenefitEntity> gratuityBenefitEntity = gratuityBenefitRepository.findById(gratuityId);
		Optional<LifeCoverEntity> lifeCoverEntity = lifeCoverEntityRepository.findById(lifecoverID);
		
		if (gratuityBenefitEntity.isPresent() && lifeCoverEntity.isPresent()) {
			lifeCoverEntityRepository.deleteById(lifecoverID);
			gratuityBenefitRepository.deleteById(gratuityId);
			memberCategoryRepository.deleteById(gratuityBenefitEntity.get().getCategoryId());
		}
		return ApiResponseDto.success(SchemeRuleHelper.entiteslifeandGratToDto(lifeCoverEntityRepository.findByQuotationId(quotationId),
				memberCategoryRepository.findByqstgQuoationId(quotationId),gratuityBenefitRepository.findByQuotationId(quotationId)));
	}
//	



	
}
