package com.lic.epgs.gratuity.fund.service;

import java.util.Date;

import com.lic.epgs.gratuity.fund.dto.CalculateResDto;
import com.lic.epgs.gratuity.fund.dto.CalculationReqDto;
import com.lic.epgs.gratuity.fund.dto.ClaimReqDto;
import com.lic.epgs.gratuity.fund.dto.ClaimResDto;
import com.lic.epgs.gratuity.fund.dto.DocumentDto;
import com.lic.epgs.gratuity.fund.dto.FreelookCancellationReqDto;
import com.lic.epgs.gratuity.fund.dto.JsonResponse;
import com.lic.epgs.gratuity.fund.dto.PolicyFundStmtRequestDto;
import com.lic.epgs.gratuity.fund.dto.PolicyFundStmtResponseDto;
import com.lic.epgs.gratuity.fund.dto.PolicyStmtGenReqDto;
import com.lic.epgs.gratuity.fund.dto.PolicyStmtGenRespDto;

public interface FundService {

	boolean setCreditEntries(Long policyId, Date date);

	CalculateResDto calculate(CalculationReqDto calculationReqDto);

	ClaimResDto setClaimEntry(ClaimReqDto claimReqDto);

	boolean setFreelookCancellation(FreelookCancellationReqDto freelookCancellationReqDto);

	boolean setMerger(Long serviceId);

	boolean setConversion(Long serviceId);

	PolicyStmtGenRespDto generateFundStatement(PolicyStmtGenReqDto requestDto);
	
	JsonResponse fetchBatchStatus(String batchNo);
	
	PolicyFundStmtResponseDto fetchDetailsFrPolicyFundStatement(PolicyFundStmtRequestDto requestDto);

	DocumentDto downloadPolicyFundStatementForPolicyId(String policyId);
}
