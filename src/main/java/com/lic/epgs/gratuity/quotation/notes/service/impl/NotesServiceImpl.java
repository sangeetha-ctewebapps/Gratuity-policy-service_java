package com.lic.epgs.gratuity.quotation.notes.service.impl;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lic.epgs.gratuity.common.dto.ApiResponseDto;
import com.lic.epgs.gratuity.quotation.notes.dto.NotesDto;
import com.lic.epgs.gratuity.quotation.notes.entity.MasterNotesEntity;
import com.lic.epgs.gratuity.quotation.notes.entity.NotesEntity;
import com.lic.epgs.gratuity.quotation.notes.helper.NotesHelper;
import com.lic.epgs.gratuity.quotation.notes.repository.MasterNotesRepository;
import com.lic.epgs.gratuity.quotation.notes.repository.NotesRepository;
import com.lic.epgs.gratuity.quotation.notes.service.NotesService;

@Service
public class NotesServiceImpl implements NotesService {

	@Autowired
	private NotesRepository notesRepository;
	
	@Autowired
	private MasterNotesRepository masterNotesRepository;
	
	@Override
	public ApiResponseDto<List<NotesDto>> findByQuotationIdAndNotesType(Long quotationId, 
			String notesType, String type) {
		if (type.equals("INPROGRESS")) {
			List<NotesEntity> entities = notesRepository.findByQuotationIdAndNotesType(quotationId, notesType);
			if(entities.isEmpty()) {
				return ApiResponseDto.notFound(Collections.emptyList());
			} else {
				return ApiResponseDto.success(entities.stream().map(NotesHelper::entityToDto)
						.collect(Collectors.toList()));
			}
		} else {
			List<MasterNotesEntity> entities = masterNotesRepository.findByQuotationIdAndNotesType(quotationId, notesType);
			if(entities.isEmpty()) {
				return ApiResponseDto.notFound(Collections.emptyList());
			} else {
				return ApiResponseDto.success(entities.stream().map(NotesHelper::entityToDto)
						.collect(Collectors.toList()));
			}
		}
	}
	
	@Override
	public ApiResponseDto<List<NotesDto>> create(NotesDto notesDto) {
		NotesEntity notesEntity = NotesHelper.dtoToEntity(notesDto);
		
		notesEntity.setIsActive(true);
		notesEntity.setCreatedBy(notesDto.getCreatedBy());
		notesEntity.setCreatedDate(new Date());
		
		notesRepository.save(notesEntity);
		
		return ApiResponseDto.created(notesRepository
				.findByQuotationIdAndNotesType(notesDto.getQuotationId(), notesDto.getNotesType())
				.stream().map(NotesHelper::entityToDto).collect(Collectors.toList()));
	}
}
