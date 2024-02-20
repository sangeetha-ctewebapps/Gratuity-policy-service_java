package com.lic.epgs.gratuity.policy.endorsement.document.service;

import java.util.List;

import com.lic.epgs.gratuity.common.dto.ApiResponseDto;
import com.lic.epgs.gratuity.policy.endorsement.document.dto.EndorsementDocumentDto;

public interface EndorsementDocumentService {

	ApiResponseDto<List<EndorsementDocumentDto>> create(EndorsementDocumentDto endorsementDocumentDto);

	ApiResponseDto<List<EndorsementDocumentDto>> update(Long id, EndorsementDocumentDto endorsementDocumentDto);

//	ApiResponseDto<List<EndorsementDocumentDto>> findByEndorsementId(Long endorsementId);

	ApiResponseDto<List<EndorsementDocumentDto>> delete(Long id);

	ApiResponseDto<List<EndorsementDocumentDto>> findByTmpPolicyId(Long endorsementId);

}
