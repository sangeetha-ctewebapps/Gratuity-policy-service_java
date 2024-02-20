package com.lic.epgs.gratuity.policy.notes.service.impl;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lic.epgs.gratuity.common.dto.ApiResponseDto;
import com.lic.epgs.gratuity.policy.notes.dto.StagingPolicyNotesDto;
import com.lic.epgs.gratuity.policy.notes.entity.StagingPolicyNoteEntity;
import com.lic.epgs.gratuity.policy.notes.helper.PolicyNotesHelper;
import com.lic.epgs.gratuity.policy.notes.repository.StagingPolicyNoteRepository;
import com.lic.epgs.gratuity.policy.notes.service.PolicyNotesService;
import com.lic.epgs.gratuity.quotation.notes.entity.MasterNotesEntity;
import com.lic.epgs.gratuity.quotation.notes.entity.NotesEntity;
import com.lic.epgs.gratuity.quotation.notes.helper.NotesHelper;


@Service
public class PolicyNotesServiceImpl implements PolicyNotesService {

	@Autowired
	private StagingPolicyNoteRepository stagingPolicyNoteRepository;
	
	@Override
	public ApiResponseDto<List<StagingPolicyNotesDto>> create(StagingPolicyNotesDto stagingPolicyNotesDto) {
	StagingPolicyNoteEntity stagingPolicyNoteEntity = PolicyNotesHelper.dtoToEntity(stagingPolicyNotesDto);
		
	stagingPolicyNoteEntity.setIsActive(true);
	stagingPolicyNoteEntity.setCreatedBy(stagingPolicyNotesDto.getCreatedBy());
	stagingPolicyNoteEntity.setCreatedDate(new Date());
		
	stagingPolicyNoteRepository.save(stagingPolicyNoteEntity);
	
	return ApiResponseDto.created(stagingPolicyNoteRepository
			.findByPolicyIdAndNotesType(stagingPolicyNotesDto.getPolicyId(), stagingPolicyNotesDto.getNotesType()).
			stream().map(PolicyNotesHelper::entityToDto).collect(Collectors.toList()));
	}

	@Override
	public ApiResponseDto<List<StagingPolicyNotesDto>> findByPolicyIdAndNotesType(Long policyId, String notesType,
			String type) {
		if (type.equals("INPROGRESS")) {
			List<StagingPolicyNoteEntity> entities = stagingPolicyNoteRepository.findByPolicyIdAndNotesType(policyId, notesType);
			if(entities.isEmpty()) {
				return ApiResponseDto.notFound(Collections.emptyList());
			} else {
				return ApiResponseDto.success(entities.stream().map(PolicyNotesHelper::entityToDto)
						.collect(Collectors.toList()));
			}
		} else {
			return ApiResponseDto.notFound(null);
		}
	}

}
