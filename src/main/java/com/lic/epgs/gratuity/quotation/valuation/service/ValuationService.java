package com.lic.epgs.gratuity.quotation.valuation.service;

import com.lic.epgs.gratuity.common.dto.ApiResponseDto;
import com.lic.epgs.gratuity.quotation.valuation.dto.ValuationDto;

/**
 * @author Ismail Khan
 *
 */

public interface ValuationService {
	
	ApiResponseDto<ValuationDto> findByQuotationId(Long id, String type);
	ApiResponseDto<ValuationDto> create(ValuationDto valuationDto);
	ApiResponseDto<ValuationDto> update(Long id, ValuationDto valuationDto);
}
