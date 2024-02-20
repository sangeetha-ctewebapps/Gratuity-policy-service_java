package com.lic.epgs.gratuity.policyservices.dom.service;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.lic.epgs.gratuity.common.dto.ApiResponseDto;
import com.lic.epgs.gratuity.mph.dto.TempMPHBankDto;
import com.lic.epgs.gratuity.policy.entity.MasterPolicyEntity;
import com.lic.epgs.gratuity.policy.renewalpolicy.dto.PolicyTmpSearchDto;
import com.lic.epgs.gratuity.policy.renewalpolicy.dto.RenewalPolicyTMPDto;
import com.lic.epgs.gratuity.policy.renewalpolicy.entitiy.RenewalPolicyTMPEntity;
import com.lic.epgs.gratuity.policyservices.aom.dto.AOMSearchDto;
import com.lic.epgs.gratuity.policyservices.dom.dto.DOMSearchDto;
import com.lic.epgs.gratuity.policyservices.dom.dto.MemberLeaverDto;
import com.lic.epgs.gratuity.policyservices.dom.dto.MidleaverBenficiaryDto;
import com.lic.epgs.gratuity.policyservices.dom.dto.PmstMidleaverPropsDto;

public interface DOMService {

	ApiResponseDto<PmstMidleaverPropsDto> masterPolicyForCreateServiceforIndividual(
			PmstMidleaverPropsDto pmstMidleaverPropsDto);

	ApiResponseDto<List<PolicyTmpSearchDto>> searchByPolicyNumber(String type, String policyNumber);

	ApiResponseDto<List<AOMSearchDto>> otherSearchByPolicyNumber(String type, DOMSearchDto domSearchDto);

	ApiResponseDto<PmstMidleaverPropsDto> getOverView(Long tmpPolicyId);

	ApiResponseDto<PmstMidleaverPropsDto> saveRefund(Long tmpPolicyId, Boolean isPremiumRefund, String userName);

	ApiResponseDto<List<MemberLeaverDto>> getMemberList(Long tmpPolicyId);

	ApiResponseDto<PmstMidleaverPropsDto> midleaverSubmitForApproval(Long tmpPolicyId, String userName);

	ApiResponseDto<PmstMidleaverPropsDto> midleaverSendBackToMaker(Long tmpPolicyId, String userName);

	ApiResponseDto<PmstMidleaverPropsDto> midleaverReject(Long tmpPolicyId, String userName);

	// ApiResponseDto<PmstMidleaverPropsDto> midleaverApprove(Long tmpPolicyId,
	// String userName) throws JsonProcessingException;

	ApiResponseDto<PmstMidleaverPropsDto> midleaverApprove(Long tmpPolicyId, String userName);

	ApiResponseDto<PmstMidleaverPropsDto> payoutSendForApproval(Long tmpPolicyId, String userName);

	ApiResponseDto<PmstMidleaverPropsDto> payoutSendBackToMaker(Long tmpPolicyId, String userName);

	ApiResponseDto<PmstMidleaverPropsDto> payoutReject(Long tmpPolicyId, String userName);

	ApiResponseDto<PmstMidleaverPropsDto> payoutApprove(Long tmpPolicyId, String userName);

	ApiResponseDto<List<TempMPHBankDto>> getBeneficiaries(Long tmpPolicyId, String userName);

	ApiResponseDto<RenewalPolicyTMPDto> findMidLeaversInMakerBucket(Long masterPolicyId, Long tmpPolicyId);

	ApiResponseDto<PmstMidleaverPropsDto> importMemberData(Long pmstPolicyId, Long batchId, String userName);

	ApiResponseDto<MidleaverBenficiaryDto> saveMidleaverBenficiary(Long tmpPolicyId, Long mphTmpBankId,
			String userName);

	ApiResponseDto<MidleaverBenficiaryDto> updateMidLeaverBenficiary(Long tmpPolicyId, Long mphTmpBankId,
			String userName);

	ApiResponseDto<TempMPHBankDto> getMidLeaverBenficiary(Long tmpPolicyId);

	RenewalPolicyTMPEntity masterToTempPolicyDetail(MasterPolicyEntity masterPolicyEntity);

	ApiResponseDto<PmstMidleaverPropsDto> discard(Long tmpPolicyId, String userName);
}
