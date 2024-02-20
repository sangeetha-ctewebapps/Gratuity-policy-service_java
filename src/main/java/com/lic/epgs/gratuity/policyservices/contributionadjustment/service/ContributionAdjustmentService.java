package com.lic.epgs.gratuity.policyservices.contributionadjustment.service;

import java.util.List;

import com.lic.epgs.gratuity.common.dto.ApiResponseDto;
import com.lic.epgs.gratuity.policy.dto.PolicyContributionDetailDto;
import com.lic.epgs.gratuity.policy.dto.PolicyDto;
import com.lic.epgs.gratuity.policy.renewalpolicy.dto.RenewalPolicyTMPDto;
import com.lic.epgs.gratuity.policyservices.contributionadjustment.dto.ContributionAdjustmentPropsDto;
import com.lic.epgs.gratuity.policyservices.contributionadjustment.dto.ContributionAdjustmentSearchDto;

public interface ContributionAdjustmentService {

	ApiResponseDto<ContributionAdjustmentPropsDto> createcontributionadjustment(
			PolicyContributionDetailDto policyContributionDetailDto, Long pmstPolicyId);

	ApiResponseDto<RenewalPolicyTMPDto> getInprogressforService(Long masterPolicyId, Long tmpPolicyId,
			String servicetype);

	ApiResponseDto<ContributionAdjustmentPropsDto> updatecontribadjustment(
			PolicyContributionDetailDto policyContributionDetailDto, Long tmpPolicyId);

	ApiResponseDto<List<ContributionAdjustmentPropsDto>> contributionfiltersearch(
			ContributionAdjustmentSearchDto contributionAdjustmentPropsDto);

	ApiResponseDto<ContributionAdjustmentPropsDto> contriAdjustStatusChange(Long contridjustpropsId, Long statusId,
			ContributionAdjustmentPropsDto tempPolicyClaimPropsDto);

	ApiResponseDto<ContributionAdjustmentPropsDto> sentPolicyforReject(Long id, String username,
			ContributionAdjustmentPropsDto contributionAdjustmentPropsDto);

	ApiResponseDto<PolicyDto> approve(Long contadjustid, String username);



}
