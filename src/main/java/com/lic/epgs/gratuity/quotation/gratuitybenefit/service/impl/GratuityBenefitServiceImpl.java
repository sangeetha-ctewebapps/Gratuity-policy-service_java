package com.lic.epgs.gratuity.quotation.gratuitybenefit.service.impl;

import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import javax.persistence.EntityManager;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lic.epgs.gratuity.common.dto.ApiResponseDto;
import com.lic.epgs.gratuity.quotation.gratuitybenefit.dto.GratuityBenefitDto;
import com.lic.epgs.gratuity.quotation.gratuitybenefit.dto.GratuityBenefitPropsDto;
import com.lic.epgs.gratuity.quotation.gratuitybenefit.entity.GratuityBenefitEntity;
import com.lic.epgs.gratuity.quotation.gratuitybenefit.entity.GratuityBenefitPropsEntity;
import com.lic.epgs.gratuity.quotation.gratuitybenefit.entity.MasterGratuityBenefitEntity;
import com.lic.epgs.gratuity.quotation.gratuitybenefit.helper.GratuityBenefitHelper;
import com.lic.epgs.gratuity.quotation.gratuitybenefit.repository.GratuityBenefitPropsRepository;
import com.lic.epgs.gratuity.quotation.gratuitybenefit.repository.GratuityBenefitRepository;
import com.lic.epgs.gratuity.quotation.gratuitybenefit.repository.MasterGratuityBenefitRepository;
import com.lic.epgs.gratuity.quotation.gratuitybenefit.service.GratuityBenefitService;

/**
 * @author Ismail Khan A
 *
 */

@Service
public class GratuityBenefitServiceImpl implements GratuityBenefitService {
	
	@Autowired
	private GratuityBenefitRepository gratuityBenefitRepository;
	
	@Autowired
	private GratuityBenefitPropsRepository gratuityBenefitPropsRepository;
	
	@Autowired
	private MasterGratuityBenefitRepository masterGratuityBenefitRepository;
	
	@Autowired
	private EntityManager entityManager;

	@Override
	public ApiResponseDto<List<GratuityBenefitDto>> findByQuotationId(Long quotationId, String type) {
		if (type.equals("INPROGRESS")) {
			List<GratuityBenefitEntity> entities = gratuityBenefitRepository.findByQuotationId(quotationId);
			if(entities.isEmpty()) {
				return ApiResponseDto.notFound(Collections.emptyList());
			} else {
				return ApiResponseDto.success(GratuityBenefitHelper.entitesToDto(entities));
			}
		} else {
			List<MasterGratuityBenefitEntity> entities = masterGratuityBenefitRepository.findByQuotationId(quotationId);
			if(entities.isEmpty()) {
				return ApiResponseDto.notFound(Collections.emptyList());
			} else {
				return ApiResponseDto.success(GratuityBenefitHelper.masterEntitesToDto(entities));
			}
		}
	}

	@Override
	public ApiResponseDto<List<GratuityBenefitDto>> create(GratuityBenefitDto gratuityBenefitDto) {
		GratuityBenefitEntity gratuityBenefitEntity = GratuityBenefitHelper.dtoToEntity(gratuityBenefitDto);
		
		Set<GratuityBenefitPropsEntity> gratuityBenefitPropsEntities = new HashSet<GratuityBenefitPropsEntity>();
		for (GratuityBenefitPropsDto gratuityBenefitPropsDto : gratuityBenefitDto.getGratuityBenefitProps()) {
			GratuityBenefitPropsEntity gratuityBenefitPropsEntity = GratuityBenefitHelper.dtoToEntity(gratuityBenefitPropsDto);
			gratuityBenefitPropsEntity.setGratuityBenefit(gratuityBenefitEntity);
			gratuityBenefitPropsEntity.setIsActive(true);
			gratuityBenefitPropsEntity.setCreatedBy(gratuityBenefitPropsDto.getCreatedBy());
			gratuityBenefitPropsEntity.setCreatedDate(new Date());
			gratuityBenefitPropsEntities.add(gratuityBenefitPropsEntity);
		}

		gratuityBenefitEntity.setGratuityBenefits(gratuityBenefitPropsEntities);

		gratuityBenefitEntity.setIsActive(true);
		gratuityBenefitEntity.setCreatedBy(gratuityBenefitDto.getCreatedBy());
		gratuityBenefitEntity.setCreatedDate(new Date());

		gratuityBenefitRepository.save(gratuityBenefitEntity);
		
		return ApiResponseDto.success(GratuityBenefitHelper.entitesToDto(gratuityBenefitRepository.findByQuotationId(gratuityBenefitEntity.getQuotationId())));
	}
	
	@Override
	public ApiResponseDto<List<GratuityBenefitDto>> update(Long quotationId, GratuityBenefitPropsDto gratuityBenefitPropsDto) {
		GratuityBenefitPropsEntity gratuityBenefitPropsEntity = gratuityBenefitPropsRepository.findById(gratuityBenefitPropsDto.getId()).get();
		gratuityBenefitPropsEntity.setNumberOfYearsOfService(gratuityBenefitPropsDto.getNumberOfYearsOfService());
		gratuityBenefitPropsEntity.setNumberOfDaysWage(gratuityBenefitPropsDto.getNumberOfDaysWage());
		gratuityBenefitPropsEntity.setNumberOfWorkingDaysPerMonth(gratuityBenefitPropsDto.getNumberOfWorkingDaysPerMonth());
		gratuityBenefitPropsEntity.setGratuityCellingAmount(gratuityBenefitPropsDto.getGratuityCellingAmount());
		gratuityBenefitPropsEntity.setMonthlyCelling(gratuityBenefitPropsDto.getMonthlyCelling());
		gratuityBenefitPropsEntity.setSalaryCelling(gratuityBenefitPropsDto.getSalaryCelling());
		gratuityBenefitPropsEntity.setRetirementAge(gratuityBenefitPropsDto.getRetirementAge());
		gratuityBenefitPropsEntity.setModifiedBy(gratuityBenefitPropsDto.getModifiedBy());
		gratuityBenefitPropsEntity.setModifiedDate(new Date());
		
		gratuityBenefitPropsRepository.save(gratuityBenefitPropsEntity);
		return ApiResponseDto.success(GratuityBenefitHelper.entitesToDto(gratuityBenefitRepository.findByQuotationId(quotationId)));
	}

	@Override
	public ApiResponseDto<List<GratuityBenefitDto>> update(GratuityBenefitDto gratuityBenefitDto) {
		GratuityBenefitEntity gratuityBenefitEntity = GratuityBenefitHelper.dtoToEntity(gratuityBenefitDto);
		
		Set<GratuityBenefitPropsEntity> gratuityBenefitPropsEntities = new HashSet<GratuityBenefitPropsEntity>();
		for (GratuityBenefitPropsDto gratuityBenefitPropsDto : gratuityBenefitDto.getGratuityBenefitProps()) {
			GratuityBenefitPropsEntity gratuityBenefitPropsEntity = GratuityBenefitHelper.dtoToEntity(gratuityBenefitPropsDto);
			gratuityBenefitPropsEntity.setGratuityBenefit(gratuityBenefitEntity);
			gratuityBenefitPropsEntity.setIsActive(true);
			gratuityBenefitPropsEntity.setCreatedBy(gratuityBenefitPropsDto.getCreatedBy());
			gratuityBenefitPropsEntity.setCreatedDate(new Date());
			gratuityBenefitPropsEntities.add(gratuityBenefitPropsEntity);
		}

		gratuityBenefitEntity.setGratuityBenefits(gratuityBenefitPropsEntities);

		gratuityBenefitEntity.setIsActive(true);
		gratuityBenefitEntity.setModifiedBy(gratuityBenefitDto.getModifiedBy());
		gratuityBenefitEntity.setModifiedDate(new Date());

		gratuityBenefitRepository.save(gratuityBenefitEntity);
		
		return ApiResponseDto.success(GratuityBenefitHelper.entitesToDto(gratuityBenefitRepository.findByQuotationId(gratuityBenefitEntity.getQuotationId())));
	}

	@Transactional
	@Override
	public ApiResponseDto<List<GratuityBenefitDto>> delete(Long quotationId, Long id) {
		Optional<GratuityBenefitPropsEntity> gratuityBenefitPropsEntity = gratuityBenefitPropsRepository.findById(id);
		if (gratuityBenefitPropsEntity.isPresent()) {
			entityManager.createQuery("delete from GratuityBenefitPropsEntity where id = :id")
			  .setParameter("id", id)
			  .executeUpdate();
			return ApiResponseDto.success(GratuityBenefitHelper.entitesToDto(gratuityBenefitRepository.findByQuotationId(quotationId)));
		} else {
			return ApiResponseDto.notFound(null);
		}		
	}
	
	@Override
	public ApiResponseDto<List<GratuityBenefitDto>> delete(Long id) {
		Optional<GratuityBenefitEntity> gratuityBenefitEntity = gratuityBenefitRepository.findById(id);
		if (gratuityBenefitEntity.isPresent()) {
			gratuityBenefitRepository.deleteById(id);
			return ApiResponseDto.success(GratuityBenefitHelper.entitesToDto(gratuityBenefitRepository.findByQuotationId(gratuityBenefitEntity.get().getQuotationId())));
		} else {
			return ApiResponseDto.notFound(null);
		}
	}
}
