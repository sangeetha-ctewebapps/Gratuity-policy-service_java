package com.lic.epgs.gratuity.renewalpolicy.notes.service;

import java.util.List;

import com.lic.epgs.gratuity.common.dto.ApiResponseDto;
import com.lic.epgs.gratuity.renewalpolicy.notes.dto.RenewalPolicyNotesDto;

public interface RenewalPolicyNotesService {

	ApiResponseDto<List<RenewalPolicyNotesDto>> create(RenewalPolicyNotesDto renewalPolicyNotesDto);

	ApiResponseDto<List<RenewalPolicyNotesDto>> findByPolicyIdAndNotesType(Long tmppolicyId, String notesType,
			String type);

}
