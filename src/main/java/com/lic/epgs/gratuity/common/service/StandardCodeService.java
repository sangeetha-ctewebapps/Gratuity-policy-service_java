package com.lic.epgs.gratuity.common.service;

import java.util.List;

import com.lic.epgs.gratuity.common.dto.ApiResponseDto;
import com.lic.epgs.gratuity.common.dto.GratutityCeilingDto;
import com.lic.epgs.gratuity.common.dto.StandardCodeDto;

/**
 * @author Gopi
 *
 */

public interface StandardCodeService {
	
	ApiResponseDto<List<StandardCodeDto>> findAll();

	ApiResponseDto<List<GratutityCeilingDto>> findGetGratutity();
	
}
