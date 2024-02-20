package com.lic.epgs.gratuity.policy.valuationmatrix.service.impl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lic.epgs.gratuity.common.dto.ApiResponseDto;
import com.lic.epgs.gratuity.policy.valuationmatrix.dto.PolicyValuationMatrixAllDto;
import com.lic.epgs.gratuity.policy.valuationmatrix.dto.PolicyValuationMatrixDto;
import com.lic.epgs.gratuity.policy.valuationmatrix.entity.PolicyValuationMatrixEntity;
import com.lic.epgs.gratuity.policy.valuationmatrix.entity.PolicyValuationWithdrawalRateEntity;
import com.lic.epgs.gratuity.policy.valuationmatrix.entity.PolicyValutationBasicEntity;
import com.lic.epgs.gratuity.policy.valuationmatrix.entity.StagingPolicyValuationMatrixEntity;
import com.lic.epgs.gratuity.policy.valuationmatrix.entity.StagingPolicyValuationWithdrawalRateEntity;
import com.lic.epgs.gratuity.policy.valuationmatrix.entity.StagingPolicyValutationBasicEntity;
import com.lic.epgs.gratuity.policy.valuationmatrix.helper.PolicyValuationMatrixHelper;
import com.lic.epgs.gratuity.policy.valuationmatrix.repository.PolicyValuationBasicRepository;
import com.lic.epgs.gratuity.policy.valuationmatrix.repository.PolicyValuationMatrixRepository;
import com.lic.epgs.gratuity.policy.valuationmatrix.repository.PolicyValuationWithdrawalRateRepository;
import com.lic.epgs.gratuity.policy.valuationmatrix.repository.StagingPolicyValuationBasicRepository;
import com.lic.epgs.gratuity.policy.valuationmatrix.repository.StagingPolicyValuationMatrixRepository;
import com.lic.epgs.gratuity.policy.valuationmatrix.repository.StagingValuationWithdrawaleRateRepository;
import com.lic.epgs.gratuity.policy.valuationmatrix.service.PolicyValuationMatrixService;
import com.lic.epgs.gratuity.quotation.valuationmatrix.dto.ValuationDetailDto;
import com.lic.epgs.gratuity.quotation.valuationmatrix.entity.MasterValuationBasicEntity;
import com.lic.epgs.gratuity.quotation.valuationmatrix.entity.MasterValuationMatrixEntity;
import com.lic.epgs.gratuity.quotation.valuationmatrix.entity.MasterValuationWithdrawalRateEntity;
import com.lic.epgs.gratuity.quotation.valuationmatrix.entity.ValuationBasicEntity;
import com.lic.epgs.gratuity.quotation.valuationmatrix.entity.ValuationMatrixEntity;
import com.lic.epgs.gratuity.quotation.valuationmatrix.entity.ValuationWithdrawalRateEntity;
import com.lic.epgs.gratuity.quotation.valuationmatrix.helper.ValuationMatrixHelper;

/**
 * @author Ismail Khan A
 *
 */

@Service
public class PolicyValuationMatrixServiceImpl implements PolicyValuationMatrixService {

	@Autowired
	private StagingPolicyValuationMatrixRepository stagingPolicyValuationMatrixRepository;
	
	@Autowired
	private StagingPolicyValuationBasicRepository stagingPolicyValuationBasicRepository;
	
	@Autowired
	private StagingValuationWithdrawaleRateRepository stagingValuationWithdrawaleRateRepository;
	
	@Autowired
	private PolicyValuationMatrixRepository policyValuationMatrixRepository;

	@Autowired
	private PolicyValuationBasicRepository policyValuationBasicRepository;
	
	@Autowired
	private PolicyValuationWithdrawalRateRepository policyValuationWithdrawalRateRepository;
	
	@Override
	public ApiResponseDto<PolicyValuationMatrixDto> findByPolicyId(Long policyId, String type) {
		if (type.equals("INPROGRESS")) {
			Optional<StagingPolicyValuationMatrixEntity> stagingPolicyValuationMatrixEntity = stagingPolicyValuationMatrixRepository.findByPolicyId(policyId);
			if(stagingPolicyValuationMatrixEntity.isPresent()) {
				return ApiResponseDto.success(PolicyValuationMatrixHelper.valuationEntityToDto(stagingPolicyValuationMatrixEntity.get()));
			} else {
				return ApiResponseDto.notFound(new PolicyValuationMatrixDto());
			}
		} else {
			Optional<PolicyValuationMatrixEntity> policyValuationMatrixEntity = policyValuationMatrixRepository.findByPolicyId(policyId);
			if(policyValuationMatrixEntity.isPresent()) {
				return ApiResponseDto.success(PolicyValuationMatrixHelper.valuationEntityToDto(policyValuationMatrixEntity.get()));
			} else {
				return ApiResponseDto.notFound(new PolicyValuationMatrixDto());
			}
		}
	}


	
	@Override
	public ApiResponseDto<PolicyValuationMatrixAllDto> findAllValuation(Long policyId, String type) {
		if (type.equals("INPROGRESS")) {
			Optional<StagingPolicyValutationBasicEntity> stagingPolicyValutationBasicEntity = stagingPolicyValuationBasicRepository.findByPolicyId(policyId);
			Optional<StagingPolicyValuationMatrixEntity> stagingPolicyValuationMatrixEntity = stagingPolicyValuationMatrixRepository.findByPolicyId(policyId);
			List<StagingPolicyValuationWithdrawalRateEntity> stagingPolicyValuationWithdrawalRateEntity = stagingValuationWithdrawaleRateRepository.findByPolicyId(policyId);
			if (stagingPolicyValutationBasicEntity.isPresent()) {
				PolicyValuationMatrixAllDto valuationDetailDto = new PolicyValuationMatrixAllDto();
				valuationDetailDto.setPolicyValuationBasicDto(PolicyValuationMatrixHelper.valuationBasicEntityToDto(stagingPolicyValutationBasicEntity.get()));
				
				valuationDetailDto.setPolicyValuationMatrixDto(PolicyValuationMatrixHelper.valuationMatrixEntityToDto(stagingPolicyValuationMatrixEntity.get()));			
				valuationDetailDto.setPolicyValuationWithdrawalRateDto(stagingPolicyValuationWithdrawalRateEntity.stream().map(PolicyValuationMatrixHelper::valuationWithdrawalRateEntityToDto)
						.collect(Collectors.toList()));			
				return ApiResponseDto.success(valuationDetailDto);
			}
		} else {
			Optional<PolicyValutationBasicEntity> policyValuationBasicEntity = policyValuationBasicRepository.findByPolicyId(policyId);
			Optional<PolicyValuationMatrixEntity> policyValuationMatrixEntity = policyValuationMatrixRepository.findByPolicyId(policyId);
			List<PolicyValuationWithdrawalRateEntity> policyValuationWithdrawalRateEntities = policyValuationWithdrawalRateRepository.findByPolicyId(policyId);
			if (policyValuationBasicEntity.isPresent()) {
				PolicyValuationMatrixAllDto valuationDetailDto = new PolicyValuationMatrixAllDto();
				valuationDetailDto.setPolicyValuationBasicDto(PolicyValuationMatrixHelper.valuationBasicEntityToDto(policyValuationBasicEntity.get()));
				valuationDetailDto.setPolicyValuationMatrixDto(PolicyValuationMatrixHelper.valuationMatrixEntityToDto(policyValuationMatrixEntity.get()));			
				valuationDetailDto.setPolicyValuationWithdrawalRateDto(policyValuationWithdrawalRateEntities.stream().map(PolicyValuationMatrixHelper::valuationWithdrawalRateEntityToDto)
						.collect(Collectors.toList()));				
				return ApiResponseDto.success(valuationDetailDto);
			}
		}
		return ApiResponseDto.notFound(new PolicyValuationMatrixAllDto());
	}
}
