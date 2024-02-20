package com.lic.epgs.gratuity.policy.gratuitybenefit.service;

import java.util.List;

import com.lic.epgs.gratuity.common.dto.ApiResponseDto;
import com.lic.epgs.gratuity.policy.gratuitybenefit.dto.PolicyGratuityBenefitDto;
import com.lic.epgs.gratuity.quotation.gratuitybenefit.dto.GratuityBenefitDto;

public interface PolicyGratuityBenefitService {

	ApiResponseDto<List<PolicyGratuityBenefitDto>> findBypolicyId(Long policyId, String type);

}
