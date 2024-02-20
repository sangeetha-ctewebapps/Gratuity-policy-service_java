package com.lic.epgs.gratuity.policy.service;

import java.io.IOException;
import java.util.List;

import com.lic.epgs.gratuity.common.dto.ApiResponseDto;
import com.lic.epgs.gratuity.common.dto.MemberCategoryDto;
import com.lic.epgs.gratuity.common.entity.MemberCategoryEntity;
import com.lic.epgs.gratuity.policy.claim.dto.GratuityCalculationsDto;
import com.lic.epgs.gratuity.policy.dto.MasterPolicyMergerDto;
import com.lic.epgs.gratuity.policy.dto.MergerNewSearchFilterDto;
import com.lic.epgs.gratuity.policy.dto.PmstTmpMergerPropsDto;
import com.lic.epgs.gratuity.policy.dto.PolicyContributionDetailDto;
import com.lic.epgs.gratuity.policy.dto.PolicyContributionDetailMergerDto;
import com.lic.epgs.gratuity.policy.dto.PolicyDto;
import com.lic.epgs.gratuity.policy.entity.MasterPolicyEntity;
import com.lic.epgs.gratuity.policy.entity.PmstTmpMergerPropsEntity;
import com.lic.epgs.gratuity.policy.renewalpolicy.dto.RenewalPolicyTMPDto;
import com.lic.epgs.gratuity.policyservices.common.dto.PremiumGstRefundDto;
import com.lic.epgs.gratuity.quotation.valuationmatrix.dto.ValuationMatrixDto;

public interface MergerService {

	ApiResponseDto<PolicyDto> SearchFilterListPolicy(String policyNumber, String servicetype);

	ApiResponseDto<List<PmstTmpMergerPropsDto>> inprogresssearchfilter(PmstTmpMergerPropsDto pmstTmpMergerPropsDto);

	ApiResponseDto<PmstTmpMergerPropsDto> SaveMergerinService(PmstTmpMergerPropsDto pmstTmpMergerPropsDto);

	ApiResponseDto<List<PmstTmpMergerPropsDto>> getExistingSearchFilter(String policyNumber);

	ApiResponseDto<PmstTmpMergerPropsDto> updateMergerinService(PmstTmpMergerPropsDto pmstTmpMergerPropsDto);

	ApiResponseDto<PmstTmpMergerPropsDto> mergerStatusUpdate(PmstTmpMergerPropsDto pmstTmpMergerPropsDto);

	ApiResponseDto<PmstTmpMergerPropsDto> approvemerger(PmstTmpMergerPropsDto pmstTmpMergerPropsDto);

	ApiResponseDto<PmstTmpMergerPropsDto> mergemembesourcetodes(PmstTmpMergerPropsDto pmstTmpMergerPropsDto);

	String generateReport(Long policyId, String reportType)throws IOException;

	String premiumreceipt(Long policyId) throws IOException;

	ApiResponseDto<PmstTmpMergerPropsDto> gettmpMemberPropsDetails(Long mergerid);

	ApiResponseDto<PmstTmpMergerPropsDto> updateid(PmstTmpMergerPropsDto pmstTmpMergerPropsDto);

	ApiResponseDto<GratuityCalculationsDto> generalRefundCalculation(GratuityCalculationsDto gratitutyCalculationDto);

	ApiResponseDto<List<PolicyDto>> GetExitingPolicyNoinMaster(MergerNewSearchFilterDto mergerNewSearchFilterDto);

	ApiResponseDto<PmstTmpMergerPropsDto> mergerApprove(Long mergerPropsId, String username);

	ApiResponseDto<RenewalPolicyTMPDto> inprogressSearchByPolicynumber(String policyNumber);

	ApiResponseDto <List<PmstTmpMergerPropsDto>> mergerPropsDetailsByPolicyNumber(String policyNumber);

	ApiResponseDto<PmstTmpMergerPropsDto> forApprove(Long id, String username);

	ApiResponseDto<PmstTmpMergerPropsDto> forReject(Long id, String username);

	ApiResponseDto<PmstTmpMergerPropsDto> sendBackToMaker(Long id, String username);

	ApiResponseDto<PmstTmpMergerPropsDto> sendForApproval(Long id, String username, Long destinationTmpPolicyId);
	
	ApiResponseDto<RenewalPolicyTMPDto> makePaymentAdjustmentForMerger(
			PolicyContributionDetailMergerDto policyContributionDetailDto);

	ApiResponseDto<RenewalPolicyTMPDto> updatePaymentAdjustmentForMerger(
			PolicyContributionDetailMergerDto policyContributionDetailDto);

	ApiResponseDto<ValuationMatrixDto> generateValuationForMerger(PmstTmpMergerPropsDto pmstTmpMergerPropsDto);

	ApiResponseDto<PmstTmpMergerPropsDto> saveMerger(PmstTmpMergerPropsDto pmstTmpMergerPropsDto);

	ApiResponseDto<List<MemberCategoryDto>> memberCategoryDetailsByPolicynumber(String policyNumber);

	ApiResponseDto<ValuationMatrixDto> getValuationMatrixForMerger(Long tempPolicyId);

	ApiResponseDto<PremiumGstRefundDto> getOverViewOfGstAndPremium(String policyNumber);

	
}
