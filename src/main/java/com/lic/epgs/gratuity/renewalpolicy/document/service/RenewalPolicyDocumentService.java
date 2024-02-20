package com.lic.epgs.gratuity.renewalpolicy.document.service;

import java.util.List;

import com.lic.epgs.gratuity.common.dto.ApiResponseDto;
import com.lic.epgs.gratuity.renewalpolicy.document.dto.RenewalPolicyDocumentDto;

public interface RenewalPolicyDocumentService {

	ApiResponseDto<List<RenewalPolicyDocumentDto>> create(RenewalPolicyDocumentDto renewalPolicyDocumentDto);

	ApiResponseDto<List<RenewalPolicyDocumentDto>> findByPolicyIdandModuleType(Long tmpPolicyId, String moduleType);

	ApiResponseDto<List<RenewalPolicyDocumentDto>> delete(Long id);

}
