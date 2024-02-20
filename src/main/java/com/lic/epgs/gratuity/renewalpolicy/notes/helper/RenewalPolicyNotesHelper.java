package com.lic.epgs.gratuity.renewalpolicy.notes.helper;

import org.modelmapper.ModelMapper;


import com.lic.epgs.gratuity.renewalpolicy.notes.dto.RenewalPolicyNotesDto;
import com.lic.epgs.gratuity.renewalpolicy.notes.entity.RenewalPolicyNotesEntity;

public class RenewalPolicyNotesHelper {

	public static RenewalPolicyNotesEntity dtoToEntity(RenewalPolicyNotesDto renewalPolicyNotesDto) {

		return new ModelMapper().map(renewalPolicyNotesDto, RenewalPolicyNotesEntity.class);
	}

	public static RenewalPolicyNotesDto entityToDto(RenewalPolicyNotesEntity renewalPolicyNotesEntity) {
		return new ModelMapper().map(renewalPolicyNotesEntity, RenewalPolicyNotesDto.class);
	}

//	public static RenewalPolicyNotesDto entityToDto(MasterNotesEntity renewalPolicyNotesEntity) {
//		return new ModelMapper().map(renewalPolicyNotesEntity, RenewalPolicyNotesDto.class);
//	}
//	
	
	
}
