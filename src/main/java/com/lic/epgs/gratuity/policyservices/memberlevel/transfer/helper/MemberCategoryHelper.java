package com.lic.epgs.gratuity.policyservices.memberlevel.transfer.helper;

import org.modelmapper.ModelMapper;

import com.lic.epgs.gratuity.common.dto.MemberCategoryDto;
import com.lic.epgs.gratuity.common.entity.MemberCategoryEntity;
import com.lic.epgs.gratuity.policyservices.memberlevel.transfer.dto.MemberCategoryVersionDto;
import com.lic.epgs.gratuity.policyservices.memberlevel.transfer.entity.MemberCategoryEntityVersion;

public class MemberCategoryHelper {

	public static MemberCategoryEntityVersion clone(MemberCategoryEntityVersion getMemberCategoryEntity, String username, Long id) {
		MemberCategoryVersionDto memberCategoryDto =new ModelMapper().map(getMemberCategoryEntity, MemberCategoryVersionDto.class);
		memberCategoryDto.setId(null);
		
		MemberCategoryEntityVersion memberCategoryEntity =new  ModelMapper().map(memberCategoryDto, MemberCategoryEntityVersion.class);
		memberCategoryEntity.setQstgQuoationId(id);
		memberCategoryEntity.setModifiedBy(username);
		return memberCategoryEntity;
	}

}
