package com.lic.epgs.gratuity.simulation.helper;

import org.modelmapper.ModelMapper;

import com.lic.epgs.gratuity.simulation.dto.DepositDto;
import com.lic.epgs.gratuity.simulation.entity.DepositEntity;

public class DepositHelper {
	public static DepositDto entityToDto(DepositEntity entity) {
		return new ModelMapper().map(entity, DepositDto.class);
	}
}
