package com.lic.epgs.gratuity.quotation.lifecover.helper;

import java.util.Date;

import org.modelmapper.ModelMapper;

import com.lic.epgs.gratuity.quotation.lifecover.dto.LifeCoverDto;
import com.lic.epgs.gratuity.quotation.lifecover.entity.LifeCoverEntity;
import com.lic.epgs.gratuity.quotation.lifecover.entity.MasterLifeCoverEntity;

public class LifeCoverFlatHelper {

	public static LifeCoverEntity clone(LifeCoverEntity lifeCoverEntity, String username, Long quotationId) {
		LifeCoverDto lifeCoverDto=new ModelMapper().map(lifeCoverEntity, LifeCoverDto.class);
		
		lifeCoverDto.setId(null);
		
		LifeCoverEntity newLifeCoverEntity=new ModelMapper().map(lifeCoverDto, LifeCoverEntity.class);
		newLifeCoverEntity.setQuotationId(quotationId);
		newLifeCoverEntity.setCreatedBy(username);
		newLifeCoverEntity.setCreatedDate(new Date());
		newLifeCoverEntity.setIsActive(true);
		return newLifeCoverEntity;
	}

	public static MasterLifeCoverEntity masterClone(MasterLifeCoverEntity masterLifeCoverEntity, String username,
			Long quotationId) {
		LifeCoverDto lifeCoverDto = new ModelMapper().map(masterLifeCoverEntity, LifeCoverDto.class);
		lifeCoverDto.setId(null);
		
		MasterLifeCoverEntity newMasterLifeCoverEntity=new ModelMapper().map(lifeCoverDto, MasterLifeCoverEntity.class);
		newMasterLifeCoverEntity.setCreatedBy(username);
		newMasterLifeCoverEntity.setCreatedDate(new Date());
		newMasterLifeCoverEntity.setQuotationId(quotationId);
		newMasterLifeCoverEntity.setIsActive(true);
		return newMasterLifeCoverEntity;
		
	}
	
//	public static MasterLifeCoverFlatEntity entityToMasterEntity(LifeCoverFlatEntity entity) {
//		return new ModelMapper().map(entity, MasterLifeCoverFlatEntity.class);
//	}

}
