package com.lic.epgs.gratuity.quotation.valuationmatrix.service;

import com.lic.epgs.gratuity.common.dto.ApiResponseDto;
import com.lic.epgs.gratuity.quotation.valuationmatrix.dto.ValuationDetailDto;
import com.lic.epgs.gratuity.quotation.valuationmatrix.dto.ValuationMatrixDto;

public interface ValuationDetailService {
	
	ApiResponseDto<ValuationDetailDto> findByQuotationId(Long id, String type);
	ApiResponseDto<ValuationMatrixDto> findMatrixByQuotationId(Long id, String type);
	ApiResponseDto<ValuationDetailDto> create(ValuationDetailDto valuationDetailDto);
	ApiResponseDto<ValuationMatrixDto> createMatrix(ValuationMatrixDto valuationMatrixDto);
	ApiResponseDto<ValuationDetailDto> update(Long id, ValuationDetailDto valuationDetailDto);
	ApiResponseDto<ValuationMatrixDto> updateMatrix(ValuationMatrixDto valuationMatrixDto);
	ApiResponseDto<String> statusforvaluationMatrix(Long quotationId);
	
	
}
