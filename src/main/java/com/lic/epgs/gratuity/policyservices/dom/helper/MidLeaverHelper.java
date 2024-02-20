package com.lic.epgs.gratuity.policyservices.dom.helper;

import org.modelmapper.ModelMapper;

import com.lic.epgs.gratuity.policyservices.aom.dto.AOMSearchDto;
import com.lic.epgs.gratuity.policyservices.aom.entity.AOMSearchEntity;
import com.lic.epgs.gratuity.policyservices.dom.dto.PmstMidleaverPropsDto;
import com.lic.epgs.gratuity.policyservices.dom.entity.DOMSearchEntity;
import com.lic.epgs.gratuity.policyservices.dom.entity.PmstMidleaverPropsEntity;

public class MidLeaverHelper {

	public static PmstMidleaverPropsDto entitytodto(PmstMidleaverPropsEntity getPmstMidleaverPropsEntity) {

		return new ModelMapper().map(getPmstMidleaverPropsEntity, PmstMidleaverPropsDto.class);
	}

	public static AOMSearchDto entityToDto(AOMSearchEntity aOMSearchEntity) {
		return new ModelMapper().map(aOMSearchEntity, AOMSearchDto.class);
	}

	public static AOMSearchDto entityToDto(DOMSearchEntity aOMSearchEntity) {
		return new ModelMapper().map(aOMSearchEntity, AOMSearchDto.class);
	}

}
