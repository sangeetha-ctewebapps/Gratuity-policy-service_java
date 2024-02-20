package com.lic.epgs.gratuity.policy.renewalpolicy.valuation.service.impl;

import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lic.epgs.gratuity.common.dto.ApiResponseDto;
import com.lic.epgs.gratuity.policy.helper.PolicyHelper;
import com.lic.epgs.gratuity.policy.renewalpolicy.dto.RenewalPolicyTMPDto;
import com.lic.epgs.gratuity.policy.renewalpolicy.valuation.dto.RenewalValuationTMPDto;
import com.lic.epgs.gratuity.policy.renewalpolicy.valuation.entity.RenewalValuationTMPEntity;
import com.lic.epgs.gratuity.policy.renewalpolicy.valuation.helper.RenewalValuationTMPHelper;
import com.lic.epgs.gratuity.policy.renewalpolicy.valuation.repository.RenewalValuationTMPRepository;
import com.lic.epgs.gratuity.policy.renewalpolicy.valuation.service.RenewalPolicyValutionService;

@Service
public class RenewalPolicyValutionServiceImpl implements RenewalPolicyValutionService {
	@Autowired
	private RenewalValuationTMPRepository renewalValuationTMPRepository;

	@Override
	public ApiResponseDto<RenewalValuationTMPDto> fetchPolicyValuation(Long tmpPolicyId) {

		RenewalValuationTMPEntity renewalValuationTMPEntity = renewalValuationTMPRepository.findBytmpPolicyId(tmpPolicyId);

		 RenewalValuationTMPDto entityToDto = RenewalValuationTMPHelper.entityToDto(renewalValuationTMPEntity);

		return ApiResponseDto.success(entityToDto);
		// ApiResponseDto.success(fetchpolicyDetails.stream().map(PolicyHelper::entityToDto).collect(Collectors.toList()));

	}

	@Override
	public ApiResponseDto<RenewalValuationTMPDto> updatePolicyValuation(Long tmpPolicyId, RenewalValuationTMPDto renewalValuationTMPDto ) {
		
		 RenewalValuationTMPEntity renewalValuationTMPEntity = renewalValuationTMPRepository.findById(tmpPolicyId).get();
		
		
		// renewalValuationTMPEntity.setId(null);
		 renewalValuationTMPEntity.setSalaryEscalation(renewalValuationTMPDto.getSalaryEscalation());
		 
//		 renewalValuationTMPEntity.setDiscountRate(renewalValuationTMPDto.getDiscountRate());
		 
		 renewalValuationTMPRepository.save(renewalValuationTMPEntity);
		 
		 RenewalValuationTMPDto entityToDtoupdate = RenewalValuationTMPHelper.entityToDtoupdate(renewalValuationTMPEntity);
	
		return ApiResponseDto.success(entityToDtoupdate);
	}

}
