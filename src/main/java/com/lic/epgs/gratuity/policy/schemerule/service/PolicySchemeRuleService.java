package com.lic.epgs.gratuity.policy.schemerule.service;

import java.util.List;

import com.lic.epgs.gratuity.common.dto.ApiResponseDto;
import com.lic.epgs.gratuity.policy.schemerule.dto.PolicyLifeCoverAndGratuityDto;
import com.lic.epgs.gratuity.policy.schemerule.dto.PolicySchemeRuleDto;
import com.lic.epgs.gratuity.policy.schemerule.entity.PolicySchemeEntity;
import com.lic.epgs.gratuity.quotation.schemerule.dto.NewSchemeRuleDto;
import com.lic.epgs.gratuity.quotation.schemerule.dto.SchemeRuleDto;

public interface PolicySchemeRuleService {

	ApiResponseDto<PolicySchemeRuleDto> findBypolicyId(Long policyId, String type);

	ApiResponseDto<List<PolicyLifeCoverAndGratuityDto>> findAll(Long policyId, String type);

	




}
