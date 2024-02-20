package com.lic.epgs.gratuity.policy.service;

import java.util.List;

import com.lic.epgs.gratuity.common.dto.ApiResponseDto;
import com.lic.epgs.gratuity.policy.dto.PolicyDto;
import com.lic.epgs.gratuity.policy.dto.PmstTmpConversionPropsDto;

public interface ConversionService {ApiResponseDto<PolicyDto> getConversionPolicyDetail(String policyNumber);

	ApiResponseDto<PmstTmpConversionPropsDto> saveConversionservice(
			PmstTmpConversionPropsDto pmstTmpConversionPropsDto);

	ApiResponseDto<PmstTmpConversionPropsDto> updateconversion(Long id, PmstTmpConversionPropsDto pmstTmpConversionPropsDto);

	ApiResponseDto<PmstTmpConversionPropsDto> conversionStatusUpdate(PmstTmpConversionPropsDto pmstTmpConversionPropsDto);

	ApiResponseDto<List<PmstTmpConversionPropsDto>> inprogresssearchfilter(PmstTmpConversionPropsDto pmstTmpConversionPropsDto);

	ApiResponseDto<List<PmstTmpConversionPropsDto>> getExistingSearchFilter(PmstTmpConversionPropsDto pmstTmpConversionPropsDto);

	ApiResponseDto<PmstTmpConversionPropsDto> getConversionDetailById(Long id);

}
