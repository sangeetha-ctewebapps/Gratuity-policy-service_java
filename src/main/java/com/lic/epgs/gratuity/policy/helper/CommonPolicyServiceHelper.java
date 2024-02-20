package com.lic.epgs.gratuity.policy.helper;

import java.util.Date;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;

import com.lic.epgs.gratuity.common.entity.MemberCategoryEntity;
import com.lic.epgs.gratuity.policy.dto.PmstTempMemTrfPropsDto;
import com.lic.epgs.gratuity.policy.dto.PmstTmpConversionPropsDto;
import com.lic.epgs.gratuity.policy.dto.PmstTmpMergerPropsDto;
import com.lic.epgs.gratuity.policy.dto.PmstTmpPolTrfPropsDto;
import com.lic.epgs.gratuity.policy.entity.MasterPolicyEntity;
import com.lic.epgs.gratuity.policy.entity.PmstTempMemTrfPropsEntity;
import com.lic.epgs.gratuity.policy.entity.PmstTmpConversionPropsEntity;
import com.lic.epgs.gratuity.policy.entity.PmstTmpMergerPropsEntity;
import com.lic.epgs.gratuity.policy.entity.PmstTmpPolTrfPropsEntity;
import com.lic.epgs.gratuity.policy.entity.PolicyHistoryEntity;
import com.lic.epgs.gratuity.policy.renewalpolicy.entitiy.RenewalPolicyTMPEntity;
import com.lic.epgs.gratuity.policy.renewalpolicy.member.entity.RenewalPolicyTMPMemberEntity;
import com.lic.epgs.gratuity.policyservice.freelookcancellation.dto.FreeLookCancellationDto;
import com.lic.epgs.gratuity.policyservice.freelookcancellation.entity.FreeLookCancellationEntity;

public class CommonPolicyServiceHelper {
	
	private static ModelMapper modelMapper = new ModelMapper();
	
	static {
		modelMapper.addMappings(new PropertyMap<PmstTmpMergerPropsEntity, PmstTmpMergerPropsDto>() {
			@Override
			protected void configure() {
				map(source.getDestinationPolicyID(), destination.getDestiMasterPolicyId());
				map(source.getSourcePolicyID(), destination.getSourcemasterPolicyId());
			}
		});		
	}

	public static PmstTmpMergerPropsDto entitytoDto(PmstTmpMergerPropsEntity pmstTmpMergerPropsEntity) {
		
		return modelMapper.map(pmstTmpMergerPropsEntity, PmstTmpMergerPropsDto.class);
	}

	
public static PmstTmpPolTrfPropsDto entitytoDto(PmstTmpPolTrfPropsEntity pmstTmpPolTrfPropsEntity) {
		
		return new ModelMapper().map(pmstTmpPolTrfPropsEntity, PmstTmpPolTrfPropsDto.class);
	}

	public static PmstTmpConversionPropsDto entitytoDto(PmstTmpConversionPropsEntity get) {
		
		return  new ModelMapper().map(get, PmstTmpConversionPropsDto.class);
	}

	public static RenewalPolicyTMPMemberEntity copytomembersourcetodest(
			RenewalPolicyTMPMemberEntity getRenewalPolicyTMPMemberEntity,
			RenewalPolicyTMPEntity desrenewalPolicyTMPEntity, List<MemberCategoryEntity> sourceMemCategory,
			List<MemberCategoryEntity> destinationMemCategory) { // lIST<Member_category> srcmemberCategory, lIST<Member_category> desmemberCategory
		getRenewalPolicyTMPMemberEntity.setId(null);
//		getRenewalPolicyTMPMemberEntity.setPolicyId(desrenewalPolicyTMPEntity.getMasterPolicyId());
		getRenewalPolicyTMPMemberEntity.setTmpPolicyId(desrenewalPolicyTMPEntity.getId());
		getRenewalPolicyTMPMemberEntity.setCreatedDate(new Date());
		
			String sourceCategoryName = "";
			Long destinationMemberCategoryId = 0L;
		for(MemberCategoryEntity mc : sourceMemCategory) {
			
			if (mc.getId() == getRenewalPolicyTMPMemberEntity.getCategoryId()) {				
				sourceCategoryName = mc.getName();				
			}
		}
		for (MemberCategoryEntity mc : destinationMemCategory) {
			if (mc.getName() == sourceCategoryName) {
				destinationMemberCategoryId = mc.getId();				
			}
		}
		getRenewalPolicyTMPMemberEntity.setCategoryId(destinationMemberCategoryId);
		return getRenewalPolicyTMPMemberEntity;
	}


	public static RenewalPolicyTMPMemberEntity cloneEntity(RenewalPolicyTMPMemberEntity getRenewalPolicyTMPMemberEntity) {

		ModelMapper modelMapper = new ModelMapper();
		
		modelMapper.createTypeMap(getRenewalPolicyTMPMemberEntity, RenewalPolicyTMPMemberEntity.class)
			.addMappings(mapper -> {
				mapper.skip(RenewalPolicyTMPMemberEntity::setId);
			});
		return modelMapper.map(getRenewalPolicyTMPMemberEntity, RenewalPolicyTMPMemberEntity.class);
				
	}
	
	public static FreeLookCancellationDto entiToDto(FreeLookCancellationEntity freeLookCancellationEntity) {
		 
		return new ModelMapper().map(freeLookCancellationEntity,FreeLookCancellationDto.class);
	}


	public static RenewalPolicyTMPMemberEntity copytomembersourcetodest(RenewalPolicyTMPMemberEntity cloneEntity,
			RenewalPolicyTMPEntity desrenewalPolicyTMPEntity) {
		cloneEntity.setId(null);
		cloneEntity.setPolicyId(desrenewalPolicyTMPEntity.getMasterPolicyId());
		cloneEntity.setTmpPolicyId(desrenewalPolicyTMPEntity.getId());
		cloneEntity.setCreatedDate(new Date());
		return cloneEntity;
	}

}
