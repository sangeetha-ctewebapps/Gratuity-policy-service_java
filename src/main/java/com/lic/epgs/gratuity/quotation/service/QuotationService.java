package com.lic.epgs.gratuity.quotation.service;

import java.io.IOException;
import java.util.List;

import org.springframework.data.domain.Page;

import com.lic.epgs.gratuity.common.dto.ApiResponseDto;
import com.lic.epgs.gratuity.common.search.SearchRequest;
import com.lic.epgs.gratuity.policy.dto.GenerateCBSheetPdfDto;
import com.lic.epgs.gratuity.quotation.dto.NewQuotationDto;
import com.lic.epgs.gratuity.quotation.dto.QuotationAdjustmentPDFDto;
import com.lic.epgs.gratuity.quotation.dto.QuotationBasicDto;
import com.lic.epgs.gratuity.quotation.dto.QuotationDto;
import com.lic.epgs.gratuity.quotation.dto.QuotationPDFGenerationDto;
import com.lic.epgs.gratuity.quotation.dto.QuotationSearchDto;

/**
 * @author Gopi
 *
 */

public interface QuotationService {
	
	ApiResponseDto<QuotationDto> findById(Long id, String type);
	ApiResponseDto<QuotationDto> findByProposalNumber(String proposalNumber);
	ApiResponseDto<QuotationDto> create(String proposalNumber, NewQuotationDto newQuotationDto);
	ApiResponseDto<QuotationDto> update(Long id, QuotationBasicDto quotationBasicDto);
	Page<QuotationDto> search(SearchRequest request);
	ApiResponseDto<QuotationDto> associateBankAccount(Long id, Long bankAccountId, String modifiedBy);
	ApiResponseDto<QuotationDto> associateContactPerson(Long id, Long contactPersonId, String modifiedBy);
	ApiResponseDto<List<QuotationDto>> filter(QuotationSearchDto quotationSearchDto);
	ApiResponseDto<QuotationDto> submitForApproval(Long id, String username);
	ApiResponseDto<QuotationDto> sendBackToMaker(Long id, String username);
	ApiResponseDto<QuotationDto> approve(Long id, String username);
	ApiResponseDto<QuotationDto> reject(Long id, String username, QuotationDto quotationDto);
	ApiResponseDto<QuotationDto> clone(Long id, String type, String username);
	ApiResponseDto<QuotationDto> escalateToCo(Long id, String username);
	ApiResponseDto<QuotationDto> escalateToZo(Long id, String username);
	ApiResponseDto<QuotationDto> saveConsent(Long id, QuotationDto quotationDto);
	ApiResponseDto<QuotationDto> activateMasterQuotation(Long id);
//	ApiResponseDto<List<QuotationAdjustmentPDFDto>> adjustmentPDF(Long quotationId);	
	ApiResponseDto<List<GenerateCBSheetPdfDto>> getcbsheetpdf(Long quotationId, Long taggedStatusId);
	
	void deleteStaingDataByProposalNumber(String proposalNumber);
	Long getQuotationSubStatus(Long subStatusId);

}
