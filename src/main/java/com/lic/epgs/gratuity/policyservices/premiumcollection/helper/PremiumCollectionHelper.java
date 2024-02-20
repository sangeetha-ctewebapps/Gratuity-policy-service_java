package com.lic.epgs.gratuity.policyservices.premiumcollection.helper;

import java.time.LocalDate;
import java.time.temporal.IsoFields;

import org.modelmapper.ModelMapper;

import com.lic.epgs.gratuity.policyservices.premiumcollection.dto.LifeCoverPremiumCollectionPropsDto;
import com.lic.epgs.gratuity.policyservices.premiumcollection.dto.PremiumCollectionDuesDto;
import com.lic.epgs.gratuity.policyservices.premiumcollection.entity.LCPremiumCollectionDuesEntity;
import com.lic.epgs.gratuity.policyservices.premiumcollection.entity.LifeCoverPremiumCollectionPropsEntity;

public class PremiumCollectionHelper{

	public static LifeCoverPremiumCollectionPropsDto entityToDto(LifeCoverPremiumCollectionPropsEntity lifeCoverPremiumCollectionPropsEntity) {
		return new ModelMapper().map(lifeCoverPremiumCollectionPropsEntity, LifeCoverPremiumCollectionPropsDto.class);
	}
	
	public static PremiumCollectionDuesDto entityToDto(LCPremiumCollectionDuesEntity lcPremiumCollectionDuesEntities) {
		return new ModelMapper().map(lcPremiumCollectionDuesEntities, PremiumCollectionDuesDto.class);
	}
	
	
}