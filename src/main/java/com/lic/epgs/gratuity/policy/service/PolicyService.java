package com.lic.epgs.gratuity.policy.service;

import java.io.IOException;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.lic.epgs.gratuity.common.dto.ApiResponseDto;
import com.lic.epgs.gratuity.policy.dto.AgeValuationReportDto;
import com.lic.epgs.gratuity.policy.dto.ContributionRequestDto;
import com.lic.epgs.gratuity.policy.dto.ContributionResponseDto;
import com.lic.epgs.gratuity.policy.dto.DocumentStoragedto;
import com.lic.epgs.gratuity.policy.dto.GenerateCBSheetPdfDto;
import com.lic.epgs.gratuity.policy.dto.PolicyBondDetailDto;
import com.lic.epgs.gratuity.policy.dto.PolicyContributionDetailDto;
import com.lic.epgs.gratuity.policy.dto.PolicyDto;
import com.lic.epgs.gratuity.policy.dto.PolicySearchDto;
import com.lic.epgs.gratuity.policy.dto.PolicyStatementPeriod;
import com.lic.epgs.gratuity.policy.dto.QuotationChargeDto;
import com.lic.epgs.gratuity.policy.dto.ShowDepositDto;
import com.lic.epgs.gratuity.policy.premiumadjustment.dto.PolicyContrySummarydto;
import com.lic.epgs.gratuity.policy.premiumadjustment.dto.PolicyDepositdto;
import com.lic.epgs.gratuity.policy.renewalpolicy.dto.RenewalPolicyTMPDto;
import com.lic.epgs.gratuity.simulation.dto.DepositDto;

/**
 * @author Gopi
 *
 */

public interface PolicyService {
	
	ApiResponseDto<PolicyDto> findById(Long id);
	ApiResponseDto<List<PolicyDto>> filter(PolicySearchDto policySearchDto);
//	ApiResponseDto<PolicyDto> makePaynent(List<PaymentDto> paymentDtos);
	ApiResponseDto<PolicyDto> submitForApproval(Long id, String username);
	ApiResponseDto<PolicyDto> sendBackToMaker(Long id, String username);
	ApiResponseDto<PolicyDto> approve(Long id, String username) ;
	ApiResponseDto<PolicyDto> reject(Long id, String username, PolicyDto policyDto);
	ApiResponseDto<PolicyDto> escalateToCo(Long id, String username);
	ApiResponseDto<PolicyDto> escalateToZo(Long id, String username);
	ApiResponseDto<List<QuotationChargeDto>> findCharges(Long id);
	ApiResponseDto<PolicyDto> findById(Long Id, String type);
	byte[] findByPolicyId(Long policyId,Long taggedStatusId);
	
	ApiResponseDto<PolicyContributionDetailDto> createContributionid(Long masterQuotationId,PolicyContributionDetailDto contributionDto);
	ApiResponseDto<List<PolicyContributionDetailDto>> findContributionId(Long id);
	ApiResponseDto<PolicyContributionDetailDto> updateContributionid(Long id,PolicyContributionDetailDto contributionDto);
	ApiResponseDto<PolicyContributionDetailDto> makePayment(PolicyContributionDetailDto policyContributionDetailDto);
	ApiResponseDto<List<PolicyDepositdto>> getDepositData(String type, Long policyId);
	byte[] findByquotationId(Long quotationId,Long taggedStatusId);
	ApiResponseDto<DocumentStoragedto> uploadDocumentStorage( MultipartFile file)
			throws IllegalStateException, IOException;	
	

	ApiResponseDto<PolicyBondDetailDto> getPolicyBondDetails(Long policyId);
	byte[] policybondpdf(Long id);
	
	byte[] getpolicyIdactive(Long policyId);
	byte[] getPolicyMemberDtls(Long policyId);
	byte[] downloadSample();
	ApiResponseDto<List<AgeValuationReportDto>> getAgeReport(Long policyId);
	byte[] findBytmpPolicyId(Long quotationId);
	ApiResponseDto<RenewalPolicyTMPDto> makePaymentAdjustmentforRenewal(
			PolicyContributionDetailDto policyContributionDetailDto);
	ApiResponseDto<List<QuotationChargeDto>> renewalChargeDetail(Long tmpPolicyId);
	ApiResponseDto<PolicyContributionDetailDto> createContributionTmpId(Long tmpPolicyId,
			PolicyContributionDetailDto contributionDto);
	ApiResponseDto<List<PolicyDepositdto>> getDepositTmpPolicy(String type, Long tmpPolicyId);
	ApiResponseDto<List<PolicyContributionDetailDto>> findContributionTmpPolicyId(Long tempPolicyId);
	ApiResponseDto<PolicyContributionDetailDto> updatePaymentAdjustment(PolicyContributionDetailDto policyContributionDetailDto);
	ApiResponseDto<List<PolicyContrySummarydto>> getSummaryContriDetail(Long policyId);
//	String generateReport(Long policyId, String reportType) throws IOException;
	ApiResponseDto<PolicyContrySummarydto> getSummaryContriDetailfield(Long policyId, String taggedStatus);
	
//	String premiumreceipt(Long masterpolicyId) throws IOException;
//	ApiResponseDto<List<GenerateCBSheetPdfDto>> getcbsheetpdf(Long policyId,Long taggedStatusId);
//	String getcandbsheetpdf(Long policyId,Long taggedStatusId) throws IOException;
	
	ApiResponseDto<RenewalPolicyTMPDto> updatePaymentAdjustmentforRenewal(
			PolicyContributionDetailDto policyContributionDetailDto);
	ApiResponseDto<PolicyDto> findByPolicyNumber(String policyNumber);
	ApiResponseDto<List<ContributionResponseDto>> getPolicyContributionDetails(ContributionRequestDto contributionRequestDto);
	ApiResponseDto<List<PolicyStatementPeriod>> getPolicyStatementPeriod(Long id);
	ApiResponseDto<List<PolicyContrySummarydto>> getSummaryContriDetailForRenewal(Long tmppolicyId);
	ApiResponseDto<List<PolicyDto>> filterForClaim(PolicySearchDto policySearchDto);
	Long getPolicySubStatus(Long policySubStatusId);
	ApiResponseDto<PolicyContributionDetailDto> getlatestcontributiondetail(Long masterPolicyId);
}
