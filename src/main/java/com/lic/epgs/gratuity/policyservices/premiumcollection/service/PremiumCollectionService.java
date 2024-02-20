package com.lic.epgs.gratuity.policyservices.premiumcollection.service;

import java.util.List;

import com.lic.epgs.gratuity.common.dto.ApiResponseDto;
import com.lic.epgs.gratuity.policy.dto.PolicyContributionDetailDto;
import com.lic.epgs.gratuity.policy.dto.PolicyDto;
import com.lic.epgs.gratuity.policyservices.premiumcollection.dto.LifeCoverPremiumCollectionPropsDto;
import com.lic.epgs.gratuity.policyservices.premiumcollection.dto.PremiumCollectionDuesDto;
import com.lic.epgs.gratuity.policyservices.premiumcollection.dto.PremiumCollectionSearchDto;

public interface PremiumCollectionService{

	ApiResponseDto<List<LifeCoverPremiumCollectionPropsDto>> contributionfiltersearch(
			PremiumCollectionSearchDto premiumCollectionSearchDto);

	ApiResponseDto<LifeCoverPremiumCollectionPropsDto> premiumcollectionforReject(Long premiumpropsId, String username,
			LifeCoverPremiumCollectionPropsDto lifeCoverPremiumCollectionPropsDto);

	ApiResponseDto<List<PremiumCollectionDuesDto>> duescalculation(Long masterpolicyId, String username);

	ApiResponseDto<LifeCoverPremiumCollectionPropsDto> createPremiumcollectionAdjustment(
			PolicyContributionDetailDto policyContributionDetailDto, Long masterPolicyId);

	ApiResponseDto<LifeCoverPremiumCollectionPropsDto> updatePremiumcollectionAdjustment(
			PolicyContributionDetailDto policyContributionDetailDto, Long tmpPolicyId);

	ApiResponseDto<LifeCoverPremiumCollectionPropsDto> premiumAdjustStatusChange(Long premiumpropsId, Long statusId,
			LifeCoverPremiumCollectionPropsDto lcPremiumCollectionPropsDto);

	ApiResponseDto<LifeCoverPremiumCollectionPropsDto> premiumcollectionprops(Long premiumpropsId);

	ApiResponseDto<List<PremiumCollectionDuesDto>> premiumcollectionduesonprops(Long premiumpropsId);

	ApiResponseDto<PolicyDto> approve(Long premcontriadjustpropsid, String username);

	ApiResponseDto<LifeCoverPremiumCollectionPropsDto> escalateToZo(Long premiumpropsId, String username);

	Boolean discardservice(Long propsId, String moduleType);

	ApiResponseDto<Boolean> checkNextDueDate(Long masterPolicyId);
	
}