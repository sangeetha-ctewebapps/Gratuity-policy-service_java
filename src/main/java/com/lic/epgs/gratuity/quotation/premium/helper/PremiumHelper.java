package com.lic.epgs.gratuity.quotation.premium.helper;

import org.modelmapper.ModelMapper;

import com.lic.epgs.gratuity.quotation.premium.entity.MasterPremiumEntity;
import com.lic.epgs.gratuity.quotation.premium.entity.PremiumEntity;

public class PremiumHelper {
	public static MasterPremiumEntity entityToMasterEntity(PremiumEntity entity) {
		return new ModelMapper().map(entity, MasterPremiumEntity.class);
	}
}
