package com.lic.epgs.gratuity.quotation.schemerule.service;

import java.util.List;

import com.lic.epgs.gratuity.common.dto.ApiResponseDto;
import com.lic.epgs.gratuity.quotation.lifecover.dto.LifeCoverAndGratuityDto;
import com.lic.epgs.gratuity.quotation.schemerule.dto.NewSchemeRuleDto;
import com.lic.epgs.gratuity.quotation.schemerule.dto.SchemeRuleDto;

/**
 * @author Gopi
 *
 */

public interface SchemeRuleService {

	ApiResponseDto<SchemeRuleDto> findByQuotationId(Long id, String type);
	ApiResponseDto<SchemeRuleDto> create(NewSchemeRuleDto newSchemeRuleDto);
	ApiResponseDto<SchemeRuleDto> update(Long id, SchemeRuleDto schemeRuleDto);
	ApiResponseDto<List<LifeCoverAndGratuityDto>> createLifeCoverandGratuity(
			LifeCoverAndGratuityDto lifeCoverAndGratuityDto);
	ApiResponseDto<List<LifeCoverAndGratuityDto>> findAll(Long quotationId, String type);
	ApiResponseDto<List<LifeCoverAndGratuityDto>> updateLifeCoverandGratuity(
			LifeCoverAndGratuityDto lifeCoverAndGratuityDto);
	ApiResponseDto<List<LifeCoverAndGratuityDto>> delete(Long quotationId, Long id);
	ApiResponseDto<List<LifeCoverAndGratuityDto>> deleteLifeandGratuity(Long quotationId, Long lifecoverID,
			Long gratuityId);
}
