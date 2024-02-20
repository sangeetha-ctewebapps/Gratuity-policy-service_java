package com.lic.epgs.gratuity.common.service;

import java.util.List;

import com.lic.epgs.gratuity.common.dto.ApiResponseDto;
import com.lic.epgs.gratuity.common.dto.PickListDto;
import com.lic.epgs.gratuity.common.dto.PickListItemDto;

/**
 * @author Gopi
 *
 */

public interface PickListItemService {

	ApiResponseDto<List<PickListItemDto>> findByPickListId(Long pickListId);
	ApiResponseDto<List<PickListDto>> findAllByOrderByPickListIdOrderByDisplayOrder();
	
}
