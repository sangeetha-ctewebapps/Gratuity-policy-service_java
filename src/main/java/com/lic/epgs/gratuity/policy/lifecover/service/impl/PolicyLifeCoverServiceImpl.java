package com.lic.epgs.gratuity.policy.lifecover.service.impl;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lic.epgs.gratuity.common.dto.ApiResponseDto;
import com.lic.epgs.gratuity.policy.lifecover.dto.PolicyLifeCoverDto;
import com.lic.epgs.gratuity.policy.lifecover.entity.StagingPolicyLifeCoverEntity;
import com.lic.epgs.gratuity.policy.lifecover.entity.PolicyLifeCoverEntity;
import com.lic.epgs.gratuity.policy.lifecover.helper.PolicyLifeCoverHelper;
import com.lic.epgs.gratuity.policy.lifecover.repository.StagingPolicyLifeCoverRepository;
import com.lic.epgs.gratuity.policy.lifecover.repository.PolicyLifeCoverRepository;
import com.lic.epgs.gratuity.policy.lifecover.service.PolicyLifeCoverService;

/**
 * @author Ismail Khan A
 *
 */

@Service
public class PolicyLifeCoverServiceImpl implements PolicyLifeCoverService {


	@Autowired
	private StagingPolicyLifeCoverRepository stagingPolicyLifeCoverRepository;
	
	@Autowired
	private PolicyLifeCoverRepository policyLifeCoverRepository;
	
	@Override
	public ApiResponseDto<List<PolicyLifeCoverDto>> findByPolicyId(Long policyId, String type) {
		if (type.equals("INPROGRESS")) {
			List<StagingPolicyLifeCoverEntity> entities = stagingPolicyLifeCoverRepository.findByPolicyId(policyId);
			if(entities.isEmpty()) {
				return ApiResponseDto.notFound(Collections.emptyList());
			}
			return ApiResponseDto.success(entities.stream().map(PolicyLifeCoverHelper::entityToDto)
					.collect(Collectors.toList()));
		} else {
			List<PolicyLifeCoverEntity> entities = policyLifeCoverRepository.findByPolicyId(policyId);
			if(entities.isEmpty()) {
				return ApiResponseDto.notFound(Collections.emptyList());
			}
			return ApiResponseDto.success(entities.stream().map(PolicyLifeCoverHelper::entityToDto)
					.collect(Collectors.toList()));
		}
	}
}
