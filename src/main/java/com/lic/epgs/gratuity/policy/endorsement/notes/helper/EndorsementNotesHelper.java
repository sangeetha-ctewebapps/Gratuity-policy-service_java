package com.lic.epgs.gratuity.policy.endorsement.notes.helper;

import org.modelmapper.ModelMapper;

import com.lic.epgs.gratuity.policy.endorsement.notes.dto.EndorsementNotesDto;
import com.lic.epgs.gratuity.policy.endorsement.notes.entity.EndorsementNotesEntity;

public class EndorsementNotesHelper {

	public static EndorsementNotesEntity dtoToEntity(EndorsementNotesDto endorsementNotesDto) {
		return new ModelMapper().map(endorsementNotesDto, EndorsementNotesEntity.class);
	}
	public static EndorsementNotesDto entityToDto(EndorsementNotesEntity endorsementNotesEntity) {
		return new ModelMapper().map(endorsementNotesEntity, EndorsementNotesDto.class);
	}
}
