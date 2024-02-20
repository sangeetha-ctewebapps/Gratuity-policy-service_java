package com.lic.epgs.gratuity.policy.claim.service;

import java.io.IOException;

import com.lic.epgs.gratuity.common.dto.ApiResponseDto;
import com.lic.epgs.gratuity.fund.dto.CalculateResDto;
import com.lic.epgs.gratuity.policy.claim.dto.GratuityCalculationsDto;

public interface PolicyClaimPdfService {

	String claimCalculationSheetGenerateReport(String onboardNumber ) throws IOException;

	String claimForwardingLetter(String userName, String payoutNumber) throws IOException ;

	ApiResponseDto<CalculateResDto> GetFundSize(Long policyid);

	ApiResponseDto<GratuityCalculationsDto> calculategratuity(GratuityCalculationsDto gratitutyCalculationDto);

	String generateReport(String userName, String payoutNumber) throws IOException;

	
}
