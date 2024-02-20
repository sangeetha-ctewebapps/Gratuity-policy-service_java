package com.lic.epgs.gratuity.policy.valuation.service.impl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lic.epgs.gratuity.common.dto.ApiResponseDto;
import com.lic.epgs.gratuity.policy.valuation.dto.PolicyValuationDto;
import com.lic.epgs.gratuity.policy.valuation.entity.PolicyMasterValuationEntity;
import com.lic.epgs.gratuity.policy.valuation.entity.PolicyValuationEntity;
import com.lic.epgs.gratuity.policy.valuation.helper.PolicyValuationHelper;
import com.lic.epgs.gratuity.policy.valuation.repository.PolicyMasterValuationRepository;
import com.lic.epgs.gratuity.policy.valuation.repository.PolicyValuationRepository;
import com.lic.epgs.gratuity.policy.valuation.service.PolicyValuationService;
import com.lic.epgs.gratuity.quotation.valuation.dto.ValuationDto;
import com.lic.epgs.gratuity.quotation.valuation.entity.MasterValuationEntity;
import com.lic.epgs.gratuity.quotation.valuation.entity.ValuationEntity;
import com.lic.epgs.gratuity.quotation.valuation.helper.ValuationHelper;

@Service
public class PolicyValuationServiceImpl implements PolicyValuationService {

	@Autowired
	private PolicyValuationRepository policyValuationRepository;

	@Autowired
	private PolicyMasterValuationRepository policyMasterValuationRepository;

	@Override
	public ApiResponseDto<PolicyValuationDto> findByPolicyId(Long policyid, String type) {

		if (type.equals("INPROGRESS")) {
			Optional<PolicyValuationEntity> entity = policyValuationRepository.findByPolicyId(policyid);
			if (entity.isPresent()) {
				return ApiResponseDto.success(PolicyValuationHelper.entityToDto(entity.get()));
			} else {
				return ApiResponseDto.notFound(new PolicyValuationDto());
			}
		} else {
			Optional<PolicyMasterValuationEntity> entity = policyMasterValuationRepository.findByPolicyId(policyid);
			if (entity.isPresent()) {
				return ApiResponseDto.success(PolicyValuationHelper.entityToDto(entity.get()));
			} else {
				return ApiResponseDto.notFound(new PolicyValuationDto());
			}
		}
	}

}
