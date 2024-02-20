package com.lic.epgs.gratuity.policy.endorsement.notes.service.impl;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lic.epgs.gratuity.common.dto.ApiResponseDto;
import com.lic.epgs.gratuity.policy.endorsement.notes.dto.EndorsementNotesDto;
import com.lic.epgs.gratuity.policy.endorsement.notes.entity.EndorsementNotesEntity;
import com.lic.epgs.gratuity.policy.endorsement.notes.helper.EndorsementNotesHelper;
import com.lic.epgs.gratuity.policy.endorsement.notes.repository.EndorsementNotesRepository;
import com.lic.epgs.gratuity.policy.endorsement.notes.service.EndorsementNotesService;
import com.lic.epgs.gratuity.policy.notes.entity.StagingPolicyNoteEntity;
import com.lic.epgs.gratuity.policy.notes.helper.PolicyNotesHelper;

import io.swagger.v3.oas.annotations.servers.Server;

@Service
public class EndorsementNotesServiceImpl implements EndorsementNotesService {

	@Autowired
	private EndorsementNotesRepository endorsementNotesRepository;
	 
	@Override
	public ApiResponseDto<List<EndorsementNotesDto>> create(EndorsementNotesDto endorsementNotesDto) {
		
		EndorsementNotesEntity endorsementNotesEntity=EndorsementNotesHelper.dtoToEntity(endorsementNotesDto);
		
		endorsementNotesEntity.setIsActive(true);
		endorsementNotesEntity.setCreatedBy(endorsementNotesDto.getCreatedBy());
		endorsementNotesEntity.setCreatedDate(new Date());
			
		endorsementNotesRepository.save(endorsementNotesEntity);
		
		return ApiResponseDto.created(endorsementNotesRepository
				.findByEndorsementIdAndNotesType(endorsementNotesDto.getEndorsementId(), endorsementNotesDto.getNotesType()).
				stream().map(EndorsementNotesHelper::entityToDto).collect(Collectors.toList()));
	
	}

	@Override
	public ApiResponseDto<List<EndorsementNotesDto>> findByEndorsementIdAndNotesType(Long endorsementId,
			String notesType, String type) {
		if (type.equals("INPROGRESS")) {

			List<EndorsementNotesEntity> entities = endorsementNotesRepository
					.findByEndorsementIdAndNotesType(endorsementId, notesType);
			if (entities.isEmpty()) {
				return ApiResponseDto.notFound(Collections.emptyList());
			} else {
				return ApiResponseDto.success(
						entities.stream().map(EndorsementNotesHelper::entityToDto).collect(Collectors.toList()));
			}
		} else {
			return ApiResponseDto.notFound(null);
		}
	}

}
