package com.lic.epgs.gratuity.policy.document.service;

import java.util.List;

import com.lic.epgs.gratuity.common.dto.ApiResponseDto;
import com.lic.epgs.gratuity.policy.document.dto.PolicyDocumentDto;
import com.lic.epgs.gratuity.quotation.document.dto.DocumentDto;

public interface PolicyDocumentService {

	ApiResponseDto<List<PolicyDocumentDto>> create(PolicyDocumentDto policyDocumentDto);

	ApiResponseDto<List<PolicyDocumentDto>> update(Long id, PolicyDocumentDto policyDocumentDto);

	ApiResponseDto<List<PolicyDocumentDto>> findByPolicyId(Long policyId, String type);

	ApiResponseDto<List<PolicyDocumentDto>> delete(Long id);

}
