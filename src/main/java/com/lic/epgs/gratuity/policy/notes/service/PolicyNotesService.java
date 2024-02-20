package com.lic.epgs.gratuity.policy.notes.service;

import java.util.List;

import com.lic.epgs.gratuity.common.dto.ApiResponseDto;
import com.lic.epgs.gratuity.policy.notes.dto.StagingPolicyNotesDto;

public interface PolicyNotesService {

	ApiResponseDto<List<StagingPolicyNotesDto>> create(StagingPolicyNotesDto stagingPolicyNotesDto);

	ApiResponseDto<List<StagingPolicyNotesDto>> findByPolicyIdAndNotesType(Long policyId, String notesType,
			String type);

	
	

}
