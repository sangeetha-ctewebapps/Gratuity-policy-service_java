package com.lic.epgs.gratuity.policy.valuation.service;

import com.lic.epgs.gratuity.common.dto.ApiResponseDto;
import com.lic.epgs.gratuity.policy.valuation.dto.PolicyValuationDto;

public interface PolicyValuationService {

	ApiResponseDto<PolicyValuationDto> findByPolicyId(Long policyid, String type);



}
