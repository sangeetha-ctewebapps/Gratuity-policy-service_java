package com.lic.epgs.gratuity.common.service;

import java.util.List;

import org.jvnet.hk2.annotations.Service;

import com.lic.epgs.gratuity.common.dto.ApiResponseDto;
import com.lic.epgs.gratuity.common.dto.MemberCategoryDto;


public interface MemberCategoryService {

	 ApiResponseDto<List<MemberCategoryDto>> getAllMemberCategory(String type, String entryType, Long id);
}
