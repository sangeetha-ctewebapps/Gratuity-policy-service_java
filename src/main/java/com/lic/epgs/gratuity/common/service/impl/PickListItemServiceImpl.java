package com.lic.epgs.gratuity.common.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lic.epgs.gratuity.common.dto.ApiResponseDto;
import com.lic.epgs.gratuity.common.dto.PickListDto;
import com.lic.epgs.gratuity.common.dto.PickListItemDto;
import com.lic.epgs.gratuity.common.entity.PickListEntity;
import com.lic.epgs.gratuity.common.entity.PickListItemEntity;
import com.lic.epgs.gratuity.common.repository.PickListItemRepository;
import com.lic.epgs.gratuity.common.repository.PickListRepository;
import com.lic.epgs.gratuity.common.service.PickListItemService;

/**
 * @author Gopi
 *
 */

@Service
public class PickListItemServiceImpl implements PickListItemService {

	@Autowired
	private PickListRepository pickListRepository;
	
	@Autowired
	private PickListItemRepository pickListItemRepository;

	@Override
	public ApiResponseDto<List<PickListItemDto>> findByPickListId(Long pickListId) {
		List<PickListItemEntity> entities = pickListItemRepository.findAllByOrderByDisplayOrderByPickListId(pickListId);
		if (entities.isEmpty()) {
			return ApiResponseDto.notFound(Collections.emptyList());
		} else {
			return ApiResponseDto.success(entities.stream().map(this::entityToDto)
					.collect(Collectors.toList()));
		}
	}
	
	private PickListItemDto entityToDto(PickListItemEntity entity) {
		return new ModelMapper().map(entity, PickListItemDto.class);
	}

	@Override
	public ApiResponseDto<List<PickListDto>> findAllByOrderByPickListIdOrderByDisplayOrder() {
		List<PickListDto> pickListDtos = new ArrayList<PickListDto>();
		List<PickListEntity> entities = pickListRepository.findAllByOrderById();
		entities.forEach(pickListEntity -> {
			PickListDto p = new PickListDto();
			p.setId(pickListEntity.getId());
			p.setName(pickListEntity.getName());
			List<PickListItemEntity> pickListItemEntities = pickListItemRepository.findAllByOrderByDisplayOrderByPickListId(pickListEntity.getId());
			p.setPickListItems(pickListItemEntities.stream().map(this::entityToDto)
					.collect(Collectors.toList()));
			pickListDtos.add(p);
		});

		return ApiResponseDto.success(pickListDtos);
	}
}
