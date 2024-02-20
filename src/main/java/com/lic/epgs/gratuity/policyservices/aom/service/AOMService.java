package com.lic.epgs.gratuity.policyservices.aom.service;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.lic.epgs.gratuity.common.dto.ApiResponseDto;
import com.lic.epgs.gratuity.mph.entity.TempMPHEntity;
import com.lic.epgs.gratuity.policy.dto.PolicyContributionDetailDto;
import com.lic.epgs.gratuity.policy.dto.PolicyDto;
import com.lic.epgs.gratuity.policy.dto.PolicySearchDto;
import com.lic.epgs.gratuity.policy.member.entity.TempMemberAddressEntity;
import com.lic.epgs.gratuity.policy.member.entity.TempMemberAppointeeEntity;
import com.lic.epgs.gratuity.policy.member.entity.TempMemberBankAccountEntity;
import com.lic.epgs.gratuity.policy.member.entity.TempMemberNomineeEntity;
import com.lic.epgs.gratuity.policy.premiumadjustment.dto.PolicyDepositdto;
import com.lic.epgs.gratuity.policy.renewalpolicy.dto.RenewalPolicyTMPDto;
import com.lic.epgs.gratuity.policy.renewalpolicy.entitiy.PolicyTmpSearchEntity;
import com.lic.epgs.gratuity.policy.renewalpolicy.member.entity.RenewalPolicyTMPMemberAddressEntity;
import com.lic.epgs.gratuity.policy.renewalpolicy.member.entity.RenewalPolicyTMPMemberAppointeeEntity;
import com.lic.epgs.gratuity.policy.renewalpolicy.member.entity.RenewalPolicyTMPMemberBankAccountEntity;
import com.lic.epgs.gratuity.policy.renewalpolicy.member.entity.RenewalPolicyTMPMemberNomineeEntity;
import com.lic.epgs.gratuity.policyservices.aom.dto.AOMSearchDto;
import com.lic.epgs.gratuity.policyservices.aom.dto.DocumentUploadDto;
import com.lic.epgs.gratuity.policyservices.aom.dto.GetOverviewDto;
import com.lic.epgs.gratuity.policyservices.aom.dto.MemberTmpDto;
import com.lic.epgs.gratuity.policyservices.memberlevel.transfer.dto.DocumentDetailsResponse;
import com.lic.epgs.gratuity.policyservices.memberlevel.transfer.dto.RemoveDocReq;
import com.lic.epgs.gratuity.policyservices.memberlevel.transfer.dto.UploadDocReq;
import com.lic.epgs.gratuity.quotation.valuationmatrix.dto.ValuationMatrixDto;
import com.lic.epgs.gratuity.simulation.dto.PolicyDepositDto;

public interface AOMService {

	ApiResponseDto<List<PolicyDto>> masterPolicySearch(PolicySearchDto policySearchDto);

	ApiResponseDto<List<MemberTmpDto>> importMemberData(Long masterPolicyId, Long batchId, String username);

	ApiResponseDto<?> saveTempMember(MemberTmpDto memberTmpDto, Long policyId, Long tempPolicyId);

	ApiResponseDto<List<PolicyTmpSearchEntity>> quotationSearchPolicy(String policyNumber, String type);

	ApiResponseDto<List<PolicyTmpSearchEntity>> ServiceSerachPolicy(String policyNumber, String type);

	ApiResponseDto<RenewalPolicyTMPDto> forReject(Long id, String username);

	ApiResponseDto<RenewalPolicyTMPDto> forApprove(Long id, String username);

	ApiResponseDto<RenewalPolicyTMPDto> sendBacktoMaker(Long id, String username);

	ApiResponseDto<RenewalPolicyTMPDto> sentPolicyForApproval(Long id, String username);

	ApiResponseDto<RenewalPolicyTMPDto> sentForApproval(Long id, String username);

	ApiResponseDto<RenewalPolicyTMPDto> sentPolicyBacktoMaker(Long id, String username);

	ApiResponseDto<RenewalPolicyTMPDto> sentPolicyforApprove(Long tempid, String username) throws Exception;

	ApiResponseDto<RenewalPolicyTMPDto> sentPolicyforReject(Long id, String username,
			RenewalPolicyTMPDto renewalPolicyTMPDto);

	ApiResponseDto<ValuationMatrixDto> calculatePremeium(Long tempPolicyId, String username);

	ApiResponseDto<List<AOMSearchDto>> otherCriteiraQuotationSearch(AOMSearchDto aomSearchDto, String type);

	ApiResponseDto<List<AOMSearchDto>> otherCriteiraPolicySearch(AOMSearchDto aomSearchDto, String type);

	ApiResponseDto<RenewalPolicyTMPDto> makePaymentAdjustmentforAOM(
			PolicyContributionDetailDto policyContributionDetailDto);

	ApiResponseDto<GetOverviewDto> getOverView(Long tmpPolicyId);

	ApiResponseDto<List<PolicyDepositDto>> getAdjustedDeposit(Long tmpPolicyId);

	ApiResponseDto<RenewalPolicyTMPDto> updatePaymentAdjustmentforAom(
			PolicyContributionDetailDto policyContributionDetailDto);

	public String generateMemberPDF(Long tmpPolicyId);

	RenewalPolicyTMPMemberBankAccountEntity saveTempMemberBankAccount(RenewalPolicyTMPMemberBankAccountEntity request,
			Long memberId);

	RenewalPolicyTMPMemberAddressEntity saveTempMemberAddress(RenewalPolicyTMPMemberAddressEntity request,
			Long memberId);

	RenewalPolicyTMPMemberNomineeEntity saveTempMemberNominee(RenewalPolicyTMPMemberNomineeEntity request,
			Long memberId);

	RenewalPolicyTMPMemberAppointeeEntity saveTempMemberAppointee(RenewalPolicyTMPMemberAppointeeEntity request,
			Long memberId);

	ApiResponseDto<String> sendMPHLetterInEmail(Long tmpPolicyId);

	public String sendEMailToMph(String pdfBase64, String fileName, Long policyId);

	public ApiResponseDto<RenewalPolicyTMPDto> getInMakerBucket(Long masterPolicyId, Long tmpPolicyId);

	public ApiResponseDto<Map<String, String>> getPremiumRate(Long tmpPolicyId);

	public Map<String, Object> uploadDocs(DocumentUploadDto documentUploadDto) throws JsonProcessingException;

	public List<DocumentUploadDto> getDocumentDetails(Long tmpPolicyId);

	public ResponseEntity<Map<String, Object>> removeUploadedDocs(Long documentId, String username)
			throws JsonMappingException, JsonProcessingException;

	ApiResponseDto<String> inActiveMemberInBulk(List<Long> memId);
}
