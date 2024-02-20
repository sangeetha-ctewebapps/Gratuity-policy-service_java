package com.lic.epgs.gratuity.policy.endorsement.service;

import java.util.List;

import com.lic.epgs.gratuity.common.dto.ApiResponseDto;
import com.lic.epgs.gratuity.policy.endorsement.dto.EndorsementDto;
import com.lic.epgs.gratuity.quotation.dto.QuotationDto;

public interface EndorsementService {

	ApiResponseDto<List<EndorsementDto>>  findBypolicyId(Long policyId);

	ApiResponseDto<EndorsementDto> create(long policyId, EndorsementDto endorsementDto);

	ApiResponseDto<EndorsementDto> update(long id, EndorsementDto endorsementDto);

	ApiResponseDto<EndorsementDto> submitForApproval(Long id, String username);

	ApiResponseDto<EndorsementDto> sendBackToMaker(Long id, String username);

	ApiResponseDto<EndorsementDto> reject(Long id, String username);

	ApiResponseDto<EndorsementDto> approve(Long id, String username);



}
