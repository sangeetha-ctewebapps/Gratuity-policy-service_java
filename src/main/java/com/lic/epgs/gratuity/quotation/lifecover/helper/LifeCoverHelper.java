package com.lic.epgs.gratuity.quotation.lifecover.helper;

import org.modelmapper.ModelMapper;

import com.lic.epgs.gratuity.common.dto.MemberCategoryDto;
import com.lic.epgs.gratuity.common.entity.MemberCategoryEntity;
import com.lic.epgs.gratuity.quotation.lifecover.dto.LifeCoverDto;
import com.lic.epgs.gratuity.quotation.lifecover.entity.LifeCoverEntity;
import com.lic.epgs.gratuity.quotation.lifecover.entity.MasterLifeCoverEntity;

public class LifeCoverHelper {
	
	
	public static LifeCoverDto entityToDto(LifeCoverEntity entity) {
		return new ModelMapper().map(entity, LifeCoverDto.class);
	}
	
	public static LifeCoverDto entityToDto(MasterLifeCoverEntity entity) {
		return new ModelMapper().map(entity, LifeCoverDto.class);
	}
	
	public static LifeCoverEntity dtoToEntity(LifeCoverDto dto) {
		return new ModelMapper().map(dto, LifeCoverEntity.class);
	}
	
	public static MasterLifeCoverEntity entityToMasterEntity(LifeCoverEntity entity) {
		return new ModelMapper().map(entity, MasterLifeCoverEntity.class);
	}
	
	public static MemberCategoryDto memberEntitestoDto(MemberCategoryEntity entity) {
		return new ModelMapper().map(entity, MemberCategoryDto.class);
	}
}
