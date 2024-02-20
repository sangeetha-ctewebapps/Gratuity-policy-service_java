package com.lic.epgs.gratuity.policy.claim.service;

import java.io.IOException;
import java.util.List;

import com.lic.epgs.gratuity.common.dto.ApiResponseDto;
import com.lic.epgs.gratuity.common.dto.ApiValidationResponse;
import com.lic.epgs.gratuity.fund.dto.CalculateResDto;
import com.lic.epgs.gratuity.policy.claim.dto.GratuityCalculationsDto;
import com.lic.epgs.gratuity.policy.claim.dto.TempPolicyClaimBeneficiaryDto;
import com.lic.epgs.gratuity.policy.claim.dto.TempPolicyClaimPropsDto;
import com.lic.epgs.gratuity.policy.claim.dto.TempPolicyClaimPropsSearchDto;
import com.lic.epgs.gratuity.policy.renewalpolicy.member.dto.RenewalPolicyTMPMemberDto;

public interface PolicyClaimService {

	ApiResponseDto<TempPolicyClaimPropsDto> createClaimforindividual(TempPolicyClaimPropsDto tempPolicyClaimPropsDto);

	ApiResponseDto<TempPolicyClaimPropsDto> getIndividualOnboardingno(String onboardNumber);

	ApiResponseDto<TempPolicyClaimPropsDto> onboardingClaimCancel(String onboardNumber);

	ApiResponseDto<List<TempPolicyClaimPropsDto>> filter(TempPolicyClaimPropsSearchDto policyClaimPropsSearchDto);

	ApiResponseDto<List<TempPolicyClaimPropsDto>> getClaimBasedonStatusIds(Long[] claimstatusids);

	ApiResponseDto<TempPolicyClaimPropsDto> updateIntimationDocumentsDetails(Long id,
			TempPolicyClaimPropsDto tempPolicyClaimPropsDto);

	ApiResponseDto<TempPolicyClaimPropsDto> intimationClaimUpdate(String onboardNumber,
			TempPolicyClaimPropsDto tempPolicyClaimPropsDto);



	ApiResponseDto<TempPolicyClaimBeneficiaryDto> beneficiaryDetailsUpdate(
			TempPolicyClaimBeneficiaryDto tempPolicyClaimBeneficiaryDto, Long id);

	ApiResponseDto<List<TempPolicyClaimBeneficiaryDto>> beneficiaryDetailsSave(
			TempPolicyClaimBeneficiaryDto tempPolicyClaimBeneficiaryDto);

	ApiResponseDto<List<TempPolicyClaimBeneficiaryDto>> beneficiaryDetailsget(Long id);

	ApiResponseDto<GratuityCalculationsDto> calculategratuity(GratuityCalculationsDto gratitutyCalculationDto);
	
	ApiResponseDto<CalculateResDto> GetFundSize(Long policyid);
	
	ApiResponseDto<GratuityCalculationsDto> RefundCalculation(GratuityCalculationsDto gratitutyCalculationDto);

	ApiResponseDto<TempPolicyClaimPropsDto> payoutapprove(Long id, String username);

	ApiResponseDto<List<TempPolicyClaimPropsDto>> importClaimData(Long pmstPolicyId, Long batchId, String username);

	ApiResponseDto<ApiValidationResponse> ValidationforOnboardingCondition(
			TempPolicyClaimPropsDto tempPolicyClaimPropsDto);

	ApiResponseDto<?> ValidateForOnboarding(Long memberid, Long policyid);

	ApiResponseDto<?> validationforclaimOnboarding(Long memberid, Long policyid);

	ApiResponseDto<RenewalPolicyTMPMemberDto> deleteBeneficiary(Long propsId, Long memberBankId,
			Long nomineeId, Long appointeeId);

	ApiResponseDto<TempPolicyClaimPropsDto> getIndividualIntimationNo(String intimationon);

	ApiResponseDto<TempPolicyClaimPropsDto> claimstatuschange(Long id, Long statusId,
			TempPolicyClaimPropsDto tempPolicyClaimPropsDto);

	ApiResponseDto<List<TempPolicyClaimPropsDto>> claimFilterSearch(
			TempPolicyClaimPropsSearchDto policyClaimPropsSearchDto);

	Long getClaimSubStatus(Long claimStatusId);
	
}
