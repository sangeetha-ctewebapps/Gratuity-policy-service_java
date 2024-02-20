package com.lic.epgs.gratuity.quotation.document.service;

import java.util.List;

import com.lic.epgs.gratuity.common.dto.ApiResponseDto;
import com.lic.epgs.gratuity.quotation.document.dto.DocumentDto;

public interface DocumentService {

	ApiResponseDto<List<DocumentDto>> findByQuotationId(Long quotationId, String type);
	ApiResponseDto<List<DocumentDto>> create(DocumentDto documentDto);
	ApiResponseDto<List<DocumentDto>> update(Long id, DocumentDto documentDto);
	ApiResponseDto<List<DocumentDto>> delete(Long id);
	
}
