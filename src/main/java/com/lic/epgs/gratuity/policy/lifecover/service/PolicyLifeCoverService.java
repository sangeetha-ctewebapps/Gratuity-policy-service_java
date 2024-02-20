package com.lic.epgs.gratuity.policy.lifecover.service;

import java.util.List;

import com.lic.epgs.gratuity.common.dto.ApiResponseDto;
import com.lic.epgs.gratuity.policy.lifecover.dto.PolicyLifeCoverDto;

/**
 * @author Ismail Khan A
 *
 */

public interface PolicyLifeCoverService {
	ApiResponseDto<List<PolicyLifeCoverDto>> findByPolicyId(Long id, String type);
}
