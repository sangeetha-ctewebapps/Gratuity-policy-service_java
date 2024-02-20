package com.lic.epgs.gratuity.policy.endorsement.notes.service;

import java.util.List;

import com.lic.epgs.gratuity.common.dto.ApiResponseDto;
import com.lic.epgs.gratuity.policy.endorsement.notes.dto.EndorsementNotesDto;

public interface EndorsementNotesService {

	ApiResponseDto<List<EndorsementNotesDto>> create(EndorsementNotesDto endorsementNotesDto);

	ApiResponseDto<List<EndorsementNotesDto>> findByEndorsementIdAndNotesType(Long endorsementId, String noteType,
			String type);

}
