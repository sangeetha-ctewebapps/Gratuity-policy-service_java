package com.lic.epgs.gratuity.quotation.lifecover.service;

import java.util.List;

import com.lic.epgs.gratuity.common.dto.ApiResponseDto;
import com.lic.epgs.gratuity.quotation.lifecover.dto.LifeCoverDto;

/**
 * @author Vigneshwaran
 *
 */

public interface LifeCoverService {
	ApiResponseDto<List<LifeCoverDto>> findByQuotationId(Long id, String type);
	ApiResponseDto<List<LifeCoverDto>> create(LifeCoverDto lifeCoverDtos);
	ApiResponseDto<List<LifeCoverDto>> update(Long id, LifeCoverDto lifeCoverDtos);
	ApiResponseDto<List<LifeCoverDto>> delete(Long id);
}
