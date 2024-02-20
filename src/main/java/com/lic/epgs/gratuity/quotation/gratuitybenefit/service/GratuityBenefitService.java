package com.lic.epgs.gratuity.quotation.gratuitybenefit.service;

import java.util.List;

import com.lic.epgs.gratuity.common.dto.ApiResponseDto;
import com.lic.epgs.gratuity.quotation.gratuitybenefit.dto.GratuityBenefitDto;
import com.lic.epgs.gratuity.quotation.gratuitybenefit.dto.GratuityBenefitPropsDto;

/**
 * @author Ismail Khan A
 *
 */

public interface GratuityBenefitService {

	ApiResponseDto<List<GratuityBenefitDto>> findByQuotationId(Long id, String type);
	ApiResponseDto<List<GratuityBenefitDto>> create(GratuityBenefitDto gratuityBenefitDto);
	ApiResponseDto<List<GratuityBenefitDto>> update(GratuityBenefitDto gratuityBenefitDto);
	ApiResponseDto<List<GratuityBenefitDto>> update(Long quotationId, GratuityBenefitPropsDto gratuityBenefitPropsDto);
	ApiResponseDto<List<GratuityBenefitDto>> delete(Long quotationId, Long id);
	ApiResponseDto<List<GratuityBenefitDto>> delete(Long id);
}
