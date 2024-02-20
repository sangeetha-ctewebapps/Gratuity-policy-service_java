package com.lic.epgs.gratuity.policy.renewalpolicy.schemerule.service;

import java.util.List;

import com.lic.epgs.gratuity.common.dto.ApiResponseDto;
import com.lic.epgs.gratuity.policy.renewalpolicy.schemerule.dto.RenewalLifeCoverAndGratuityDto;
import com.lic.epgs.gratuity.quotation.lifecover.dto.LifeCoverAndGratuityDto;



public interface RenewalSchemeruleTMPService {

	ApiResponseDto<List<RenewalLifeCoverAndGratuityDto>> createLifeCoverandGratuity(
			RenewalLifeCoverAndGratuityDto renewalLifeCoverAndGratuityDto);

	ApiResponseDto<List<RenewalLifeCoverAndGratuityDto>> updateLifeCoverandGratuity(
			RenewalLifeCoverAndGratuityDto renewallifeCoverAndGratuityDto);

	ApiResponseDto<List<RenewalLifeCoverAndGratuityDto>> delete(Long policyId, Long id);

	ApiResponseDto<List<RenewalLifeCoverAndGratuityDto>> deleteLifeandGratuity(Long policyId, Long lifecoverID,
			Long gratuityId);

	ApiResponseDto<List<RenewalLifeCoverAndGratuityDto>> findAll(Long policyId, String type);

	
	
	

}
