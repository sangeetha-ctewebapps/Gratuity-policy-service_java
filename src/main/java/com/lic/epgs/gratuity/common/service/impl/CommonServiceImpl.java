package com.lic.epgs.gratuity.common.service.impl;

import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.NonUniqueResultException;

import org.apache.logging.log4j.LogManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lic.epgs.gratuity.common.dto.ApiResponseDto;
import com.lic.epgs.gratuity.common.dto.MemberCategoryDto;
import com.lic.epgs.gratuity.common.dto.ProductFeatureDto;
import com.lic.epgs.gratuity.common.entity.MemberCategoryEntity;
import com.lic.epgs.gratuity.common.helper.CommonHelper;
import com.lic.epgs.gratuity.common.repository.MemberCategoryRepository;
import com.lic.epgs.gratuity.common.repository.ProductFeatureRepository;
import com.lic.epgs.gratuity.common.service.CommonService;
import com.lic.epgs.gratuity.policy.entity.MasterPolicyEntity;
import com.lic.epgs.gratuity.policy.renewalpolicy.entitiy.PolicyTmpSearchEntity;
import com.lic.epgs.gratuity.policy.repository.MasterPolicyRepository;
import com.lic.epgs.gratuity.policy.repository.PolicyTmpSearchRepository;
import com.lic.epgs.gratuity.quotation.repository.QuotationRepository;

@Service
public class CommonServiceImpl implements CommonService {

	protected final Logger logger = LogManager.getLogger(getClass());;

	@Autowired
	private QuotationRepository quotationRepository;

	@Autowired
	private ProductFeatureRepository productFeatureRepository;
	
	@Autowired
	private MasterPolicyRepository masterPolicyRepository;
	
	@Autowired
	private PolicyTmpSearchRepository policyTmpSearchRepository;

	@Override
	public synchronized String getSequence(String type) {
		logger.info("CommonSequenceService-getSequence-Start");
		try {
			return quotationRepository.getSequence(type);
		} catch (NonUniqueResultException e) {
			logger.error("CommonSequenceService-getSequence-Error:");
		}
		logger.info("CommonSequenceService-getSequence-End");
		return null;
	}



	@Override
	public ApiResponseDto<ProductFeatureDto> getProductFeature(Long masterPolicyId, Long tmpPolicId) {
		Long pid = 0L;
		Long vid = 0L;
		
		if (masterPolicyId == 0) {
			PolicyTmpSearchEntity policyTmpSearchEntity = policyTmpSearchRepository.findById(tmpPolicId).get();
			pid = policyTmpSearchEntity.getProductId();
			vid = policyTmpSearchEntity.getProductVariantId();
		} else {
			MasterPolicyEntity masterPolicyEntity = masterPolicyRepository.findById(masterPolicyId).get();
			pid = masterPolicyEntity.getProductId();
			vid = masterPolicyEntity.getProductVariantId();
		}
		
		return ApiResponseDto.success(CommonHelper.enititytoDto(
				productFeatureRepository.findByProductVariantId(
						pid, vid)));
	}



	@Override
	public ApiResponseDto<List<ProductFeatureDto>> getallproductfeature() {
	
		return ApiResponseDto.success(productFeatureRepository.findAll().stream().map(CommonHelper::enititytoDto).collect(Collectors.toList()));
	}
}

