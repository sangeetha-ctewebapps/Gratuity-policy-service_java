package com.lic.epgs.gratuity.policy.renewalpolicy.aocmcalculation.service;

import java.util.List;

import com.lic.epgs.gratuity.common.dto.ApiResponseDto;
import com.lic.epgs.gratuity.policy.dto.PolicyDto;
import com.lic.epgs.gratuity.policy.renewalpolicy.aocmcalculation.dto.AOCMCredibilityFactorDto;
import com.lic.epgs.gratuity.policy.renewalpolicy.aocmcalculation.dto.AOCMDto;
import com.lic.epgs.gratuity.policy.renewalpolicy.aocmcalculation.dto.AOCMExpenseRateDto;
import com.lic.epgs.gratuity.policy.renewalpolicy.valuationmatrix.dto.RenewalValuationBasicTMPDto;
import com.lic.epgs.gratuity.policy.renewalpolicy.valuationmatrix.dto.RenewalValuationDetailDto;

public interface AOCMService {

	ApiResponseDto<List<AOCMDto>> getAOCMCalculationData(Long policyId);

	ApiResponseDto<List<AOCMCredibilityFactorDto>> GetAOCMCredibilityFactor(String variant);

	ApiResponseDto<List<AOCMExpenseRateDto>> GetAOCMExpenseRate(String variant);

	ApiResponseDto<RenewalValuationDetailDto> UpdateAOCMCalculation(
			RenewalValuationBasicTMPDto renewalValuationBasicTMPDto);

	ApiResponseDto<PolicyDto> getAutoCoverDetails(Long policyId);

	Boolean getautocovercalculation(Long policyId, String autoCoverStatus,String userName);

}
