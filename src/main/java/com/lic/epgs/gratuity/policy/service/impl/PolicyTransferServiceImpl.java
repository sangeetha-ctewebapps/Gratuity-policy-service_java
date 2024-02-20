package com.lic.epgs.gratuity.policy.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lic.epgs.gratuity.common.dto.ApiResponseDto;
import com.lic.epgs.gratuity.policy.dto.PmstTmpPolTrfPropsDto;
import com.lic.epgs.gratuity.policy.entity.PmstTmpPolTrfPropsEntity;
import com.lic.epgs.gratuity.policy.helper.CommonPolicyServiceHelper;
import com.lic.epgs.gratuity.policy.renewalpolicy.entitiy.PolicyServiceEntitiy;
import com.lic.epgs.gratuity.policy.renewalpolicy.repository.PolicyServiceRepository;
import com.lic.epgs.gratuity.policy.repository.PmstTmpPolTrfPropsRepository;
import com.lic.epgs.gratuity.policy.service.PolicyService;
import com.lic.epgs.gratuity.policy.service.PolicyTransferService;

@Service
public class PolicyTransferServiceImpl implements PolicyTransferService {
	
//	@Autowired
//	private PolicyServiceRepository policyServiceRepository;
//	
//	@Autowired
//	private PmstTmpPolTrfPropsRepository pmstTmpPolTrfPropsRepository;
//	
//	
//	@Override
//	public ApiResponseDto<List<PmstTmpPolTrfPropsDto>> Inprogresssearchfilter(String policyNumber) {
//PolicyServiceEntitiy policyServiceEntity=policyServiceRepository.findcheckserviceActive(policyNumber);
//if(policyServiceEntity!=null) {
//	List<PmstTmpPolTrfPropsEntity> pmstTmpPolTrfPropsEntity=pmstTmpPolTrfPropsRepository.findByTmpPolicyIDinprogress(policyNumber);
//	return ApiResponseDto.success(pmstTmpPolTrfPropsEntity.stream().map(CommonPolicyServiceHelper::entitytoDto)
//			.collect(Collectors.toList()));
//	}else {
//		return ApiResponseDto.success(null);
//
//}
//}
//
//	@Override
//	public ApiResponseDto<List<PmstTmpPolTrfPropsDto>> GetExistingSearchFilter(String policyNumber) {
//			PolicyServiceEntitiy policyServiceEntitiy = policyServiceRepository.findCheckserviceInActive(policyNumber);
//			if(policyServiceEntitiy!=null) {
//				List<PmstTmpPolTrfPropsEntity> pmstTmpPolTrfPropsEntity=pmstTmpPolTrfPropsRepository.findByExistingSearch(policyNumber);
//				return ApiResponseDto.success(pmstTmpPolTrfPropsEntity.stream().map(CommonPolicyServiceHelper::entitytoDto)
//						.collect(Collectors.toList()));
//				}else {
//					return ApiResponseDto.success(null);
//			
//		}
//			}
//	
	
	
}
