package com.lic.epgs.gratuity.policy.helper;

import org.modelmapper.ModelMapper;

import com.lic.epgs.gratuity.policy.dto.PaymentDto;
import com.lic.epgs.gratuity.policy.entity.PaymentEntity;

public class PaymentHelper {
	public static PaymentDto entityToDto(PaymentEntity entity) {
		return new ModelMapper().map(entity, PaymentDto.class);
	}
	
	public static PaymentEntity dtoToEntity(PaymentDto dto) {
		return new ModelMapper().map(dto, PaymentEntity.class);
	}
}
