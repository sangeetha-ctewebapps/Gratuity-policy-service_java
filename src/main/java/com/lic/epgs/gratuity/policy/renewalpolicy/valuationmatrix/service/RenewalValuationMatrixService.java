package com.lic.epgs.gratuity.policy.renewalpolicy.valuationmatrix.service;

import com.lic.epgs.gratuity.common.dto.ApiResponseDto;
import com.lic.epgs.gratuity.policy.renewalpolicy.valuationmatrix.dto.RenewalValuationDetailDto;
import com.lic.epgs.gratuity.policy.renewalpolicy.valuationmatrix.dto.RenewalValuationMatrixDto;
import com.lic.epgs.gratuity.quotation.valuationmatrix.dto.ValuationDetailDto;
import com.lic.epgs.gratuity.quotation.valuationmatrix.dto.ValuationMatrixDto;

public interface RenewalValuationMatrixService {

	ApiResponseDto<RenewalValuationDetailDto> RenewalValuationUpdate(
			Long id, RenewalValuationDetailDto renewalValuationDetailDto);

	ApiResponseDto<RenewalValuationMatrixDto> create(RenewalValuationMatrixDto renewalValuationMatrixDto);

	ApiResponseDto<RenewalValuationMatrixDto> findMatrixTemPolicyId(Long tmpPolicyId, String type);

	ApiResponseDto<RenewalValuationMatrixDto> updateRenewalvalutionMatrixTemPolicyId(Long tmpPolicyId,
			RenewalValuationMatrixDto renewalValuationMatrixDto);

	ApiResponseDto<RenewalValuationDetailDto> findBypolicyId(Long tmpPolicyId, String type);

	ApiResponseDto<RenewalValuationDetailDto> renewalvaluationcreate(RenewalValuationDetailDto renewalValuationDetailDto);

	
}
