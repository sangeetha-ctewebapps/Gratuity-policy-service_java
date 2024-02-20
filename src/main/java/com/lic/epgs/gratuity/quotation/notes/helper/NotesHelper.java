package com.lic.epgs.gratuity.quotation.notes.helper;

import org.modelmapper.ModelMapper;

import com.lic.epgs.gratuity.quotation.notes.dto.NotesDto;
import com.lic.epgs.gratuity.quotation.notes.entity.MasterNotesEntity;
import com.lic.epgs.gratuity.quotation.notes.entity.NotesEntity;

public class NotesHelper {
	public static MasterNotesEntity entityToMasterEntity(NotesEntity entity) {
		return new ModelMapper().map(entity, MasterNotesEntity.class);
	}
	
	public static NotesDto entityToDto(NotesEntity entity) {
		return new ModelMapper().map(entity, NotesDto.class);
	}
	
	public static NotesDto entityToDto(MasterNotesEntity entity) {
		return new ModelMapper().map(entity, NotesDto.class);
	}
	
	public static NotesEntity dtoToEntity(NotesDto dto) {
		return new ModelMapper().map(dto, NotesEntity.class);
	}
}
