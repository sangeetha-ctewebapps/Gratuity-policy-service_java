package com.lic.epgs.gratuity.common.service.impl;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lic.epgs.gratuity.common.dto.ApiResponseDto;
import com.lic.epgs.gratuity.common.dto.GratutityCeilingDto;
import com.lic.epgs.gratuity.common.dto.StandardCodeDto;
import com.lic.epgs.gratuity.common.entity.GratutityCeilingEntity;
import com.lic.epgs.gratuity.common.entity.StandardCodeEntity;
import com.lic.epgs.gratuity.common.repository.GratutityCeilingRepository;
import com.lic.epgs.gratuity.common.repository.StandardCodeRepository;
import com.lic.epgs.gratuity.common.service.StandardCodeService;

/**
 * @author Gopi
 *
 */

@Service
public class StandardCodeServiceImpl implements StandardCodeService {

	@Autowired
	private StandardCodeRepository standardCodeRepository;
	
	@Autowired
	private GratutityCeilingRepository  gratutityCeilingRepository;

	
	@Override
	public ApiResponseDto<List<StandardCodeDto>> findAll() {
		List<StandardCodeEntity> entities = standardCodeRepository.findAll();
		if (entities.isEmpty()) {
			return ApiResponseDto.notFound(Collections.emptyList());
		} else {
			return ApiResponseDto.success(entities.stream().map(this::entityToDto)
					.collect(Collectors.toList()));
		}
	}
	
	private StandardCodeDto entityToDto(StandardCodeEntity entity) {
		return new ModelMapper().map(entity, StandardCodeDto.class);
	}

	@Override
	public ApiResponseDto<List<GratutityCeilingDto>> findGetGratutity() {
		List<GratutityCeilingEntity> entities = gratutityCeilingRepository.findAll();
		if (entities.isEmpty()) {
			return ApiResponseDto.notFound(Collections.emptyList());
		} else {
			return ApiResponseDto.success(entities.stream().map(this::entityToDto)
					.collect(Collectors.toList()));
		}
	}
	
	private GratutityCeilingDto entityToDto(GratutityCeilingEntity entity) {
		return new ModelMapper().map(entity, GratutityCeilingDto.class);
	}

}
