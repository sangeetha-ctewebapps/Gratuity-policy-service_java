package com.lic.epgs.gratuity.quotation.service;

import java.io.IOException;
import java.util.List;

import com.lic.epgs.gratuity.common.dto.ApiResponseDto;
import com.lic.epgs.gratuity.quotation.dto.QuotationPDFGenerationDto;

public interface QuotationPdfService {

	String adjustmentVoucher(Long quotationId, String reportType) throws IOException;

	String generateReport(Long quotationId,Long tmpPolicyId,String reportType, Long taggedStatusId) throws IOException;

	ApiResponseDto<List<QuotationPDFGenerationDto>> generatePDF(Long quotationId, Long taggedStatusId);
	
	String getcandbsheetpdf(Long quotationId, Long taggedStatusId) throws IOException;
}
