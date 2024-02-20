package com.lic.epgs.gratuity.quotation.lifecover.service.impl;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lic.epgs.gratuity.common.dto.ApiResponseDto;

import com.lic.epgs.gratuity.quotation.lifecover.dto.LifeCoverDto;
import com.lic.epgs.gratuity.quotation.lifecover.entity.LifeCoverEntity;
import com.lic.epgs.gratuity.quotation.lifecover.entity.MasterLifeCoverEntity;
import com.lic.epgs.gratuity.quotation.lifecover.helper.LifeCoverHelper;
import com.lic.epgs.gratuity.quotation.lifecover.repository.LifeCoverEntityRepository;
import com.lic.epgs.gratuity.quotation.lifecover.repository.MasterLifeCoverEntityRepository;
import com.lic.epgs.gratuity.quotation.lifecover.service.LifeCoverService;

/**
 * @author Vigneshwaran
 *
 */

@Service
public class LifeCoverServiceImpl implements LifeCoverService {


	@Autowired
	private LifeCoverEntityRepository lifeCoverEntityRepository;
	
	@Autowired
	private MasterLifeCoverEntityRepository masterLifeCoverEntityRepository;
	
	@Override
	public ApiResponseDto<List<LifeCoverDto>> findByQuotationId(Long quotationId, String type) {
		if (type.equals("INPROGRESS")) {
			List<LifeCoverEntity> entities =lifeCoverEntityRepository.findByQuotationId(quotationId);
			if(entities.isEmpty()) {
				return ApiResponseDto.notFound(Collections.emptyList());
			}
			return ApiResponseDto.success(entities.stream().map(LifeCoverHelper::entityToDto)
					.collect(Collectors.toList()));
		} else {
			List<MasterLifeCoverEntity> entities =masterLifeCoverEntityRepository.findByQuotationId(quotationId);
			if(entities.isEmpty()) {
				return ApiResponseDto.notFound(Collections.emptyList());
			}
			return ApiResponseDto.success(entities.stream().map(LifeCoverHelper::entityToDto)
					.collect(Collectors.toList()));
		}
	}

	@Override
	public ApiResponseDto<List<LifeCoverDto>> create(LifeCoverDto lifeCoverDtos) {
		LifeCoverEntity lifeCoverEntity = LifeCoverHelper.dtoToEntity(lifeCoverDtos);
		
		
		
		lifeCoverEntity.setIsActive(true);
		lifeCoverEntity.setCreatedBy(lifeCoverDtos.getCreatedBy());
		lifeCoverEntity.setCreatedDate(new Date());
		
		lifeCoverEntityRepository.save(lifeCoverEntity);
		
		return ApiResponseDto.created(lifeCoverEntityRepository
				.findByQuotationId(lifeCoverDtos.getQuotationId())
				.stream().map(LifeCoverHelper::entityToDto).collect(Collectors.toList()));
	}

	@Override
	public ApiResponseDto<List<LifeCoverDto>> update(Long id, LifeCoverDto lifeCoverDtos) {
		LifeCoverEntity lifeCoverEntity = (LifeCoverEntity) lifeCoverEntityRepository.findById(id).get();
		LifeCoverEntity newLifeCoverEntity =LifeCoverHelper.dtoToEntity(lifeCoverDtos);
		
		newLifeCoverEntity.setId(id);
		newLifeCoverEntity.setIsActive(lifeCoverEntity.getIsActive());
		newLifeCoverEntity.setCreatedBy(lifeCoverEntity.getCreatedBy());
		newLifeCoverEntity.setCreatedDate(lifeCoverEntity.getCreatedDate());
		newLifeCoverEntity.setModifiedBy(lifeCoverEntity.getModifiedBy());
		newLifeCoverEntity.setModifiedDate(new Date());
		
		lifeCoverEntityRepository.save( newLifeCoverEntity);
		return ApiResponseDto.success(lifeCoverEntityRepository
				.findByQuotationId(newLifeCoverEntity.getQuotationId())
				.stream().map(LifeCoverHelper::entityToDto).collect(Collectors.toList()));
	}

	@Override
	public ApiResponseDto<List<LifeCoverDto>> delete(Long id) {		
		LifeCoverEntity lifeCoverEntity = (LifeCoverEntity) lifeCoverEntityRepository.findById(id).get();
		lifeCoverEntityRepository.deleteById(id);
		return ApiResponseDto.success(lifeCoverEntityRepository
				.findByQuotationId(lifeCoverEntity.getQuotationId())
				.stream().map(LifeCoverHelper::entityToDto).collect(Collectors.toList()));
	}
}
