package com.lic.epgs.gratuity.policy.valuationmatrix.service;

import com.lic.epgs.gratuity.common.dto.ApiResponseDto;
import com.lic.epgs.gratuity.policy.valuationmatrix.dto.PolicyValuationMatrixAllDto;
import com.lic.epgs.gratuity.policy.valuationmatrix.dto.PolicyValuationMatrixDto;

/**
 * @author Ismail Khan A
 *
 */

public interface PolicyValuationMatrixService {

	ApiResponseDto<PolicyValuationMatrixDto> findByPolicyId(Long id, String type);

	ApiResponseDto<PolicyValuationMatrixAllDto> findAllValuation(Long policyId, String type);
	
	
}
