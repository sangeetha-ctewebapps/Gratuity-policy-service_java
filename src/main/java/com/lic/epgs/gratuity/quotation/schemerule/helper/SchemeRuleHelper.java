package com.lic.epgs.gratuity.quotation.schemerule.helper;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;

import com.lic.epgs.gratuity.common.dto.MemberCategoryDto;
import com.lic.epgs.gratuity.common.entity.MemberCategoryEntity;
import com.lic.epgs.gratuity.quotation.gratuitybenefit.dto.GratuityBenefitDto;
import com.lic.epgs.gratuity.quotation.gratuitybenefit.dto.GratuityBenefitPropsDto;
import com.lic.epgs.gratuity.quotation.gratuitybenefit.entity.GratuityBenefitEntity;
import com.lic.epgs.gratuity.quotation.gratuitybenefit.entity.GratuityBenefitPropsEntity;
import com.lic.epgs.gratuity.quotation.gratuitybenefit.helper.GratuityBenefitHelper;
import com.lic.epgs.gratuity.quotation.lifecover.dto.LifeCoverAndGratuityDto;
import com.lic.epgs.gratuity.quotation.lifecover.dto.LifeCoverDto;
import com.lic.epgs.gratuity.quotation.lifecover.entity.LifeCoverEntity;
import com.lic.epgs.gratuity.quotation.schemerule.dto.SchemeRuleDto;
import com.lic.epgs.gratuity.quotation.schemerule.entity.MasterSchemeRuleEntity;
import com.lic.epgs.gratuity.quotation.schemerule.entity.SchemeRuleEntity;

public class SchemeRuleHelper {
	
	public static SchemeRuleDto entityToDto(SchemeRuleEntity entity) {
		return new ModelMapper().map(entity, SchemeRuleDto.class);
	}
	
	public static SchemeRuleDto entityToDto(MasterSchemeRuleEntity entity) {
		return new ModelMapper().map(entity, SchemeRuleDto.class);
	}

	public static MasterSchemeRuleEntity entityToMasterEntity(SchemeRuleEntity entity) {
		return new ModelMapper().map(entity, MasterSchemeRuleEntity.class);
	}
	
	public static SchemeRuleEntity dtoToEntity(SchemeRuleDto dto) {
		return new ModelMapper().map(dto, SchemeRuleEntity.class);
	}
	
	public static SchemeRuleEntity clone(SchemeRuleEntity entity, String username, Long quotationId) {
		SchemeRuleDto schemeRuleDto = new ModelMapper().map(entity, SchemeRuleDto.class);
		schemeRuleDto.setId(null);
		
		SchemeRuleEntity schemeRuleEntity = new ModelMapper().map(schemeRuleDto, SchemeRuleEntity.class);
		schemeRuleEntity.setQuotationId(quotationId);
		schemeRuleEntity.setCreatedBy(username);
		schemeRuleEntity.setCreatedDate(new Date());
		schemeRuleEntity.setIsActive(true);
		
		return schemeRuleEntity;
	}
	public static MasterSchemeRuleEntity masterClone(MasterSchemeRuleEntity masterSchemeRuleEntity, String username,
			Long quotationId) {
		SchemeRuleDto schemeRuleDto = new ModelMapper().map(masterSchemeRuleEntity, SchemeRuleDto.class);
		schemeRuleDto.setId(null);
		MasterSchemeRuleEntity newMasterSchemeRuleEntity=new ModelMapper().map(schemeRuleDto, MasterSchemeRuleEntity.class);
		newMasterSchemeRuleEntity.setQuotationId(quotationId);
		newMasterSchemeRuleEntity.setCreatedBy(username);
		newMasterSchemeRuleEntity.setCreatedDate(new Date());
		newMasterSchemeRuleEntity.setIsActive(true);
		return newMasterSchemeRuleEntity;
	}

	//need to check 
	public static List<LifeCoverAndGratuityDto> entiteslifeandGratToDto(List<LifeCoverEntity> lifeCoverEntities,
		List<MemberCategoryEntity> memberCategoryEntities,List<GratuityBenefitEntity> gratuityBenefitEntites) {
		
		List<LifeCoverAndGratuityDto> lifeCoverAndGratuityDtolist1 = new ArrayList<LifeCoverAndGratuityDto>();
		List<LifeCoverAndGratuityDto> lifeCoverAndGratuityDtolists =(lifeCoverEntities.stream().map(SchemeRuleHelper::entiteslifeandGratToDto)
				.collect(Collectors.toList()));
		
			for(LifeCoverAndGratuityDto item :lifeCoverAndGratuityDtolists ) {
			LifeCoverAndGratuityDto getLifeCoverAndGratuityDto=item;
		
			for(GratuityBenefitEntity getGratuityBenefitEntity : gratuityBenefitEntites) {
				if (getLifeCoverAndGratuityDto.getCategoryId().equals(getGratuityBenefitEntity.getCategoryId())) {
				getLifeCoverAndGratuityDto.setGratutiySubBenefitId(getGratuityBenefitEntity.getGratutiySubBenefitId());
				getLifeCoverAndGratuityDto.setGratutiyBenefitTypeId(getGratuityBenefitEntity.getGratutiyBenefitTypeId());
				getLifeCoverAndGratuityDto.setGratuityBenefitId(getGratuityBenefitEntity.getId());
			
				if (getGratuityBenefitEntity.getCategoryId().equals(item.getCategoryId())) {
					
					getLifeCoverAndGratuityDto.setGratuityBenefitProps(getGratuityBenefitEntity.getGratuityBenefits().stream()
					.map(SchemeRuleHelper::entityToLifeCoverAndGratuityDto).collect(Collectors.toList()));				
				}
				for(MemberCategoryEntity getMemberCategory:memberCategoryEntities ) {
				if(getMemberCategory.getId().equals(item.getCategoryId()) ) {
					getLifeCoverAndGratuityDto.setCategoryName(getMemberCategory.getName());			
				}
			}
			
		}
			}
			lifeCoverAndGratuityDtolist1.add(getLifeCoverAndGratuityDto);
		}
		return lifeCoverAndGratuityDtolist1;
	}
	
	
	public static LifeCoverAndGratuityDto entiteslifeandGratToDto(LifeCoverEntity lifeCoverEntity) {
		return new ModelMapper().map(lifeCoverEntity, LifeCoverAndGratuityDto.class);
		
	}
	
	public static GratuityBenefitPropsDto entityToLifeCoverAndGratuityDto(GratuityBenefitPropsEntity gratuityBenefitPropsEntity) {
		return new ModelMapper().map(gratuityBenefitPropsEntity, GratuityBenefitPropsDto.class);
		
	}
	public static GratuityBenefitEntity dtoLifeandGratToEntity(LifeCoverAndGratuityDto lifeCoverAndGratuityDto) {
		return new ModelMapper().map(lifeCoverAndGratuityDto, GratuityBenefitEntity.class);
	}

	public static List<LifeCoverAndGratuityDto> lifeandGratToDto(List<LifeCoverDto> lifeCoverDtoList,
			List<MemberCategoryDto> memberCategoryDtoList,List<GratuityBenefitDto> gratuityBenefitDtoList) {
		
		List<LifeCoverAndGratuityDto> lifeCoverAndGratuityDtolist1=new ArrayList<LifeCoverAndGratuityDto>();
		List<LifeCoverAndGratuityDto> lifeCoverAndGratuityDtolists=(lifeCoverDtoList.stream().map(SchemeRuleHelper::singleDtoToJoinDto)
				.collect(Collectors.toList()));
		
		for(LifeCoverAndGratuityDto item :lifeCoverAndGratuityDtolists ) {
			LifeCoverAndGratuityDto getlifeCoverAndGratuityDtolist=item;
			
			for (GratuityBenefitDto getGratuityBenefitDto : gratuityBenefitDtoList) {
				
				if (getlifeCoverAndGratuityDtolist.getCategoryId().equals(getGratuityBenefitDto.getCategoryId())) {
					getlifeCoverAndGratuityDtolist
							.setGratutiySubBenefitId(getGratuityBenefitDto.getGratutiySubBenefitId());
					getlifeCoverAndGratuityDtolist
							.setGratutiyBenefitTypeId(getGratuityBenefitDto.getGratutiyBenefitTypeId());
					getlifeCoverAndGratuityDtolist.setGratuityBenefitId(getGratuityBenefitDto.getId());
					if (getGratuityBenefitDto.getCategoryId().equals( item.getCategoryId())) {
						getlifeCoverAndGratuityDtolist.setGratuityBenefitProps(
								getGratuityBenefitDto.getGratuityBenefitProps().stream().collect(Collectors.toList()));
					}
				
					for (MemberCategoryDto getMemberCategoryDto : memberCategoryDtoList) {
						if (getMemberCategoryDto.getId().equals(item.getCategoryId()) ) {
							getlifeCoverAndGratuityDtolist.setCategoryName(getMemberCategoryDto.getName());
						}
					}
				}
			}
			lifeCoverAndGratuityDtolist1.add(getlifeCoverAndGratuityDtolist);
		}		
		return lifeCoverAndGratuityDtolist1;
	}
	
	public static LifeCoverAndGratuityDto singleDtoToJoinDto(LifeCoverDto lifeCoverDto) {
		return new ModelMapper().map(lifeCoverDto, LifeCoverAndGratuityDto.class);
	}
//	public static GratuityBenefitPropsDto singleDtoToLifeCoverAndGratuityDto(GratuityBenefitPropsDto gratuityBenefitPropsDto) {
//		return new ModelMapper().map(gratuityBenefitPropsDto, GratuityBenefitPropsDto.class);
//		
//	}

	


	
}
