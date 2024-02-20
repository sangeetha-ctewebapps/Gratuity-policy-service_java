package com.lic.epgs.gratuity.quotation.premium.service;

import java.util.List;

import com.lic.epgs.gratuity.common.dto.ApiResponseDto;

import com.lic.epgs.gratuity.quotation.premium.dto.GratuityCalculationDto;
import com.lic.epgs.gratuity.quotation.premium.dto.KeyValuePairDto;
import com.lic.epgs.gratuity.quotation.premium.dto.PremiumDto;
import com.lic.epgs.gratuity.quotation.premium.dto.RateTableSelectionDto;
import com.lic.epgs.gratuity.quotation.valuationmatrix.dto.ValuationDetailDto;

public interface PremiumService {
	ApiResponseDto<List<PremiumDto>> findByQuotationId(Long quotationId);
	ApiResponseDto<List<PremiumDto>> create(PremiumDto premiumDtoDto);
	ApiResponseDto<ValuationDetailDto> calculateGratuity(Long quotationId, String username);
	ApiResponseDto<List<GratuityCalculationDto>> findAll(Long quotationId);
	ApiResponseDto<List<KeyValuePairDto>> findAllKeyValue();
	ApiResponseDto<KeyValuePairDto> findDefaultValues();
	ApiResponseDto<List<RateTableSelectionDto>> industrygroup(RateTableSelectionDto rateTableSelectionDto);
	ApiResponseDto<List<RateTableSelectionDto>> getratetableinorder(String ratevalue);
}
