package com.lic.epgs.gratuity.policy.helper;

import org.modelmapper.ModelMapper;

import com.lic.epgs.gratuity.policy.dto.QuotationChargeDto;
import com.lic.epgs.gratuity.policy.entity.QuotationChargeEntity;

public class QuotationChargeHelper {
	public static QuotationChargeDto entityToDto(QuotationChargeEntity entity) {
		return new ModelMapper().map(entity, QuotationChargeDto.class);
	}
}
