package com.lic.epgs.gratuity.policy.notes.helper;

import org.modelmapper.ModelMapper;

import com.lic.epgs.gratuity.policy.notes.dto.StagingPolicyNotesDto;
import com.lic.epgs.gratuity.policy.notes.entity.StagingPolicyNoteEntity;
import com.lic.epgs.gratuity.quotation.notes.dto.NotesDto;
import com.lic.epgs.gratuity.quotation.notes.entity.NotesEntity;

public class PolicyNotesHelper {

	public static StagingPolicyNoteEntity dtoToEntity(StagingPolicyNotesDto stagingPolicyNotesDto) {
		return new ModelMapper().map(stagingPolicyNotesDto, StagingPolicyNoteEntity.class);
	}
	public static StagingPolicyNotesDto entityToDto(StagingPolicyNoteEntity entity) {
		return new ModelMapper().map(entity, StagingPolicyNotesDto.class);
	}
	
	
	

}
