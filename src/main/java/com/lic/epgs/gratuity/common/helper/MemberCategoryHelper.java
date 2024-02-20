package com.lic.epgs.gratuity.common.helper;

import org.modelmapper.ModelMapper;

import com.lic.epgs.gratuity.common.dto.MemberCategoryDto;
import com.lic.epgs.gratuity.common.entity.MemberCategoryEntity;

public class MemberCategoryHelper {

	public static MemberCategoryEntity clone(MemberCategoryEntity getMemberCategoryEntity, String username, Long id) {
		MemberCategoryDto memberCategoryDto =new ModelMapper().map(getMemberCategoryEntity, MemberCategoryDto.class);
		memberCategoryDto.setId(null);
		
		MemberCategoryEntity memberCategoryEntity =new  ModelMapper().map(memberCategoryDto, MemberCategoryEntity.class);
		memberCategoryEntity.setQstgQuoationId(id);
		memberCategoryEntity.setModifiedBy(username);
		return memberCategoryEntity;
	}

}
