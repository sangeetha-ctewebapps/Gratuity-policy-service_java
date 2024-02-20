package com.lic.epgs.gratuity.policy.renewalpolicy.valuation.service;

import com.lic.epgs.gratuity.common.dto.ApiResponseDto;
import com.lic.epgs.gratuity.policy.renewalpolicy.dto.RenewalPolicyTMPDto;
import com.lic.epgs.gratuity.policy.renewalpolicy.valuation.dto.RenewalValuationTMPDto;

public interface RenewalPolicyValutionService {

	ApiResponseDto<RenewalValuationTMPDto> fetchPolicyValuation(Long tmpPolicyId);

	ApiResponseDto<RenewalValuationTMPDto> updatePolicyValuation(Long tmpPolicyId,RenewalValuationTMPDto renewalValuationTMPDto);

	
}
