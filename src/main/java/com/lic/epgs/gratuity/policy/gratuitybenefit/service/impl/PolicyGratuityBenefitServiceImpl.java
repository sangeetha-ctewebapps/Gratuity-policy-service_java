package com.lic.epgs.gratuity.policy.gratuitybenefit.service.impl;

import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lic.epgs.gratuity.common.dto.ApiResponseDto;
import com.lic.epgs.gratuity.policy.gratuitybenefit.dto.PolicyGratuityBenefitDto;
import com.lic.epgs.gratuity.policy.gratuitybenefit.entity.StagingPolicyGratuityBenefitEntity;
import com.lic.epgs.gratuity.policy.gratuitybenefit.entity.PolicyGratuityBenefitEntity;
import com.lic.epgs.gratuity.policy.gratuitybenefit.helper.PolicyGratuityBenefitHelper;
import com.lic.epgs.gratuity.policy.gratuitybenefit.repository.StagingPolicyGratuityBenefitRepository;
import com.lic.epgs.gratuity.policy.gratuitybenefit.repository.PolicyGratuityBenefitRepository;
import com.lic.epgs.gratuity.policy.gratuitybenefit.service.PolicyGratuityBenefitService;
import com.lic.epgs.gratuity.quotation.gratuitybenefit.dto.GratuityBenefitDto;
import com.lic.epgs.gratuity.quotation.gratuitybenefit.entity.GratuityBenefitEntity;
import com.lic.epgs.gratuity.quotation.gratuitybenefit.entity.MasterGratuityBenefitEntity;
import com.lic.epgs.gratuity.quotation.gratuitybenefit.helper.GratuityBenefitHelper;

@Service
public class PolicyGratuityBenefitServiceImpl implements PolicyGratuityBenefitService {

	@Autowired
	private PolicyGratuityBenefitRepository policyGratuityBenefitRepository;
	
	@Autowired
	private StagingPolicyGratuityBenefitRepository plcTMPGratuityBenefitRepository;
	@Override
	public ApiResponseDto<List<PolicyGratuityBenefitDto>> findBypolicyId(Long policyId, String type) {
		// TODO Auto-generated method stub
		if (type.equals("INPROGRESS")) {
			List<StagingPolicyGratuityBenefitEntity> entities = plcTMPGratuityBenefitRepository.findByPolicyId(policyId);
			if(entities.isEmpty()) {
				return ApiResponseDto.notFound(Collections.emptyList());
			} else {
				return ApiResponseDto.success(PolicyGratuityBenefitHelper.EntitesToDto(entities));
			}
		} else {
		
			List<PolicyGratuityBenefitEntity> entities = policyGratuityBenefitRepository.findBypolicyId(policyId);
			if(entities.isEmpty()) {
				return ApiResponseDto.notFound(Collections.emptyList());
			} else {
				return ApiResponseDto.success(PolicyGratuityBenefitHelper.entitesToDto(entities));
			}
		}
	}

}
