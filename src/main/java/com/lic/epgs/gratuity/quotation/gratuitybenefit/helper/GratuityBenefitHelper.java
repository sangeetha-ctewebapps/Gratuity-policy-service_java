package com.lic.epgs.gratuity.quotation.gratuitybenefit.helper;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;

import com.lic.epgs.gratuity.quotation.dto.QuotationDto;
import com.lic.epgs.gratuity.quotation.entity.QuotationEntity;
import com.lic.epgs.gratuity.quotation.gratuitybenefit.dto.GratuityBenefitDto;
import com.lic.epgs.gratuity.quotation.gratuitybenefit.dto.GratuityBenefitPropsDto;
import com.lic.epgs.gratuity.quotation.gratuitybenefit.entity.GratuityBenefitEntity;
import com.lic.epgs.gratuity.quotation.gratuitybenefit.entity.GratuityBenefitPropsEntity;
import com.lic.epgs.gratuity.quotation.gratuitybenefit.entity.GratuityBenefitEntity;
import com.lic.epgs.gratuity.quotation.gratuitybenefit.entity.MasterGratuityBenefitEntity;
import com.lic.epgs.gratuity.quotation.gratuitybenefit.entity.MasterGratuityBenefitPropsEntity;

/**
 * @author Vigneshwaran
 *
 */

public class GratuityBenefitHelper {
	public static GratuityBenefitDto entityToDto(GratuityBenefitEntity entity) {
		return new ModelMapper().map(entity, GratuityBenefitDto.class);
	}
	
	public static GratuityBenefitDto entityToDto(MasterGratuityBenefitEntity entity) {
		return new ModelMapper().map(entity, GratuityBenefitDto.class);
	}
	
	public static GratuityBenefitEntity dtoToEntity(GratuityBenefitDto dto) {
		return new ModelMapper().map(dto, GratuityBenefitEntity.class);
	}
	
	public static GratuityBenefitPropsDto entityToDto(GratuityBenefitPropsEntity entity) {
		return new ModelMapper().map(entity, GratuityBenefitPropsDto.class);
	}
	
	public static GratuityBenefitPropsDto masterEntityToDto(MasterGratuityBenefitPropsEntity entity) {
		return new ModelMapper().map(entity, GratuityBenefitPropsDto.class);
	}
	
	public static GratuityBenefitPropsEntity dtoToEntity(GratuityBenefitPropsDto dto) {
		return new ModelMapper().map(dto, GratuityBenefitPropsEntity.class);
	}
	
	public static MasterGratuityBenefitEntity entityToMasterEntity(GratuityBenefitEntity entity) {
		return new ModelMapper().map(entity, MasterGratuityBenefitEntity.class);
	}
	
	public static MasterGratuityBenefitPropsEntity entityToMasterEntity1(GratuityBenefitPropsEntity entity) {
		return new ModelMapper().map(entity, MasterGratuityBenefitPropsEntity.class);
	}
	
	public static List<GratuityBenefitDto> entitesToDto(List<GratuityBenefitEntity> entities) {
		List<GratuityBenefitDto> GratuityBenefitDtos = new ArrayList<GratuityBenefitDto>();
		for (GratuityBenefitEntity gratuityBenefitEntity1 : entities) {
			GratuityBenefitDto GratuityBenefitDto = GratuityBenefitHelper.entityToDto(gratuityBenefitEntity1);
			GratuityBenefitDto.setGratuityBenefitProps(gratuityBenefitEntity1.getGratuityBenefits().stream()
					.map(GratuityBenefitHelper::entityToDto).collect(Collectors.toList()));
			GratuityBenefitDtos.add(GratuityBenefitDto);
		}
		
		return GratuityBenefitDtos;
	}
	
	public static List<GratuityBenefitDto> masterEntitesToDto(List<MasterGratuityBenefitEntity> entities) {
		List<GratuityBenefitDto> GratuityBenefitDtos = new ArrayList<GratuityBenefitDto>();
		for (MasterGratuityBenefitEntity gratuityBenefitEntity1 : entities) {
			GratuityBenefitDto GratuityBenefitDto = GratuityBenefitHelper.entityToDto(gratuityBenefitEntity1);
			GratuityBenefitDto.setGratuityBenefitProps(gratuityBenefitEntity1.getGratuityBenefits().stream()
					.map(GratuityBenefitHelper::masterEntityToDto).collect(Collectors.toList()));
			GratuityBenefitDtos.add(GratuityBenefitDto);
		}
		
		return GratuityBenefitDtos;
	}

	public static GratuityBenefitEntity clone(GratuityBenefitEntity gratuityBenefitEntity, String username, Long quotationId) {
		GratuityBenefitDto gratuityBenefitDto = new ModelMapper().map(gratuityBenefitEntity, GratuityBenefitDto.class);
		gratuityBenefitDto.setId(null);
		GratuityBenefitEntity newGratuityBenefitEntity = new ModelMapper().map(gratuityBenefitDto, GratuityBenefitEntity.class);
			
		Set<GratuityBenefitPropsEntity> gratuityBenefitPropsEntities = new HashSet<GratuityBenefitPropsEntity>();
		for (GratuityBenefitPropsEntity gratuityBenefitPropsEntity : gratuityBenefitEntity.getGratuityBenefits()) {
				GratuityBenefitPropsEntity newGratuityBenefitPropsEntity = GratuityBenefitHelper.clone(gratuityBenefitPropsEntity, username);
				newGratuityBenefitPropsEntity.setGratuityBenefit(newGratuityBenefitEntity);
				
				gratuityBenefitPropsEntities.add(newGratuityBenefitPropsEntity);
		}
			
		newGratuityBenefitEntity.setGratuityBenefits(gratuityBenefitPropsEntities);
		newGratuityBenefitEntity.setQuotationId(quotationId);
		newGratuityBenefitEntity.setCreatedBy(username);
		newGratuityBenefitEntity.setCreatedDate(new Date());
		newGratuityBenefitEntity.setIsActive(true);
		return newGratuityBenefitEntity;
	}
	
	public static GratuityBenefitPropsEntity clone(GratuityBenefitPropsEntity gratuityBenefitPropsEntity, String username) {
		
		GratuityBenefitPropsDto gratuityBenefitPropsDto =new ModelMapper().map(gratuityBenefitPropsEntity, GratuityBenefitPropsDto.class);
		gratuityBenefitPropsDto.setId(null);
		
			GratuityBenefitPropsEntity newGratuityBenefitPropsEntity = new ModelMapper().map(gratuityBenefitPropsDto,GratuityBenefitPropsEntity.class);
			
		newGratuityBenefitPropsEntity.setIsActive(true);
		newGratuityBenefitPropsEntity.setCreatedBy(username);
		newGratuityBenefitPropsEntity.setCreatedDate(new Date());
		
		return newGratuityBenefitPropsEntity;
	}

	public static MasterGratuityBenefitEntity masterClone(MasterGratuityBenefitEntity getMasterGratuityBenefitEntity,
			String username, Long quotationId) {
		
		GratuityBenefitPropsDto gratuityBenefitPropsDto =new ModelMapper().map(getMasterGratuityBenefitEntity, GratuityBenefitPropsDto.class);
		gratuityBenefitPropsDto.setId(null);
		MasterGratuityBenefitEntity newMasterGratuityBenefitEntity = new ModelMapper().map(gratuityBenefitPropsDto, MasterGratuityBenefitEntity.class);
		
		Set<MasterGratuityBenefitPropsEntity> masterGratuityBenefitPropsEntity = new HashSet<MasterGratuityBenefitPropsEntity>();
		for(MasterGratuityBenefitPropsEntity cloneMasterGratuityBenefitPropsEntity : getMasterGratuityBenefitEntity.getGratuityBenefits()) {
			
			MasterGratuityBenefitPropsEntity newMasterGratuityBenefitPropsEntity=GratuityBenefitHelper.cloneMaster(cloneMasterGratuityBenefitPropsEntity,username);
			newMasterGratuityBenefitPropsEntity.setGratuityBenefit(newMasterGratuityBenefitEntity);
			masterGratuityBenefitPropsEntity.add(newMasterGratuityBenefitPropsEntity);
		}	
		
		newMasterGratuityBenefitEntity.setGratuityBenefits(masterGratuityBenefitPropsEntity);
		newMasterGratuityBenefitEntity.setQuotationId(quotationId);
		newMasterGratuityBenefitEntity.setCreatedBy(username);
		newMasterGratuityBenefitEntity.setCreatedDate(new Date());
		newMasterGratuityBenefitEntity.setIsActive(true);

		return newMasterGratuityBenefitEntity;
	}

	private static MasterGratuityBenefitPropsEntity cloneMaster(
			MasterGratuityBenefitPropsEntity newMasterGratuityBenefitPropsEntity, String username) {
		GratuityBenefitPropsDto gratuityBenefitPropsDto =new ModelMapper().map(newMasterGratuityBenefitPropsEntity, GratuityBenefitPropsDto.class);
		gratuityBenefitPropsDto.setId(null);
		
		MasterGratuityBenefitPropsEntity newMasterGratuityBenefitPropsEntity1 = new ModelMapper().map(gratuityBenefitPropsDto,MasterGratuityBenefitPropsEntity.class);
			
		newMasterGratuityBenefitPropsEntity1.setIsActive(true);
		newMasterGratuityBenefitPropsEntity1.setCreatedBy(username);
		newMasterGratuityBenefitPropsEntity1.setCreatedDate(new Date());
		
		return newMasterGratuityBenefitPropsEntity1;
	}
	
	public static MasterGratuityBenefitEntity entityToMasterGradtuityBenefitEntity(GratuityBenefitEntity gratuityBenefitEntity,
			Long policyId) {
		GratuityBenefitDto gratuityBenefitDto =new ModelMapper().map(gratuityBenefitEntity, GratuityBenefitDto.class);
		gratuityBenefitDto.setId(null);
		MasterGratuityBenefitEntity masterGratuityBenefitEntity = new ModelMapper().map(gratuityBenefitDto, MasterGratuityBenefitEntity.class);
		Set<MasterGratuityBenefitPropsEntity> masterGratuityBenefitPropsEntitSet = new HashSet<MasterGratuityBenefitPropsEntity>();
		for(GratuityBenefitPropsEntity gradBenEntity : gratuityBenefitEntity.getGratuityBenefits()) {
			MasterGratuityBenefitPropsEntity masterGratuityBenefitPropsEntity = GratuityBenefitHelper.cloneMaster1(gradBenEntity);
			masterGratuityBenefitPropsEntity.setGratuityBenefit(masterGratuityBenefitEntity);
			masterGratuityBenefitPropsEntitSet.add(masterGratuityBenefitPropsEntity);
		}
		masterGratuityBenefitEntity.setGratuityBenefits(masterGratuityBenefitPropsEntitSet);
		masterGratuityBenefitEntity.setQuotationId(policyId);
		masterGratuityBenefitEntity.setIsActive(true);
		return masterGratuityBenefitEntity;
	}

	private static MasterGratuityBenefitPropsEntity cloneMaster1(GratuityBenefitPropsEntity gratuityBenefitPropsEntity) {
		GratuityBenefitPropsDto gratuityBenefitPropsDto =new ModelMapper().map(gratuityBenefitPropsEntity, GratuityBenefitPropsDto.class);
		gratuityBenefitPropsDto.setId(null);
		MasterGratuityBenefitPropsEntity newMasterGratuityBenefitPropsEntity1 = new ModelMapper().map(gratuityBenefitPropsDto,MasterGratuityBenefitPropsEntity.class);
		newMasterGratuityBenefitPropsEntity1.setIsActive(true);
		newMasterGratuityBenefitPropsEntity1.setCreatedDate(new Date());
		return newMasterGratuityBenefitPropsEntity1;
	}

//	private static masterGratuityBenefitPropsEntity cloneMaster1(
//			GratuityBenefitPropsEntity gratuityBenefitPropsEntity) {
//		GratuityBenefitPropsDto gratuityBenefitPropsDto = new ModelMapper().map(gratuityBenefitPropsEntity, GratuityBenefitPropsDto.class);
//		gratuityBenefitPropsDto.setId(null);
//		
//		MasterGratuityBenefitPropsEntity masterGratuityBenefitPropsEntity = new ModelMapper().map(
//				gratuityBenefitPropsDto, MasterGratuityBenefitPropsEntity.class);
//		masterGratuityBenefitPropsEntity.setId(null);
//		masterGratuityBenefitPropsEntity.setCreatedDate(new Date());
//		
//		return masterGratuityBenefitPropsEntity;
//	}
}
