package com.lic.epgs.gratuity.policyservice.freelookcancellation.service;

import java.util.List;

import com.lic.epgs.gratuity.common.dto.ApiResponseDto;
import com.lic.epgs.gratuity.policy.dto.MasterPolicyContributionDetailsDto;
import com.lic.epgs.gratuity.policyservice.freelookcancellation.dto.FreeLookCancellationDto;

public interface FreeLookCancellationService {

	ApiResponseDto<FreeLookCancellationDto> saveFreeLookCancellationDetails(FreeLookCancellationDto freeLookCancellationDto);

	ApiResponseDto<FreeLookCancellationDto> getFreeLookCancellationDetails(Long policyId);

	ApiResponseDto<FreeLookCancellationDto> updateFreeLookCancellationDetails(FreeLookCancellationDto freeLookCancellationDto);

	ApiResponseDto<MasterPolicyContributionDetailsDto> getFlcPremiumDetails(Long masterPolicyId);

	ApiResponseDto<List<FreeLookCancellationDto>> inProgressDetailsByPolicyNumber(String policyNumber);

}
