package com.lic.epgs.gratuity.policy.schemerule.helper;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;

import com.lic.epgs.gratuity.common.dto.MemberCategoryDto;
import com.lic.epgs.gratuity.policy.entity.PolicyHistoryEntity;
import com.lic.epgs.gratuity.policy.gratuitybenefit.dto.PolicyGratuityBenefitDto;
import com.lic.epgs.gratuity.policy.lifecover.dto.PolicyLifeCoverDto;
import com.lic.epgs.gratuity.policy.renewalpolicy.schemerule.entity.RenewalSchemeruleTMPEntity;
import com.lic.epgs.gratuity.policy.schemerule.dto.PolicyLifeCoverAndGratuityDto;
import com.lic.epgs.gratuity.policy.schemerule.dto.PolicySchemeRuleDto;
import com.lic.epgs.gratuity.policy.schemerule.entity.StagingPolicySchemeRule;
import com.lic.epgs.gratuity.policy.schemerule.entity.PolicySchemeEntity;
import com.lic.epgs.gratuity.policy.schemerule.entity.PolicySchemeRuleHistoryEntity;
import com.lic.epgs.gratuity.quotation.gratuitybenefit.dto.GratuityBenefitDto;
import com.lic.epgs.gratuity.quotation.lifecover.dto.LifeCoverAndGratuityDto;
import com.lic.epgs.gratuity.quotation.schemerule.dto.SchemeRuleDto;
import com.lic.epgs.gratuity.quotation.schemerule.entity.MasterSchemeRuleEntity;
import com.lic.epgs.gratuity.quotation.schemerule.entity.SchemeRuleEntity;
import com.lic.epgs.gratuity.quotation.schemerule.helper.SchemeRuleHelper;

public class PolicySchemeRuleHelper {

	public static PolicySchemeRuleDto entityToDto(PolicySchemeEntity policySchemeEntity) {
		return new ModelMapper().map(policySchemeEntity, PolicySchemeRuleDto.class);
	}

	public static PolicySchemeRuleDto entityToDto(StagingPolicySchemeRule plctmpSchemeRule) {
	
		return new ModelMapper().map(plctmpSchemeRule, PolicySchemeRuleDto.class);
	}
	
	public static StagingPolicySchemeRule masterQuotationentityToPolicyStagingEntity(MasterSchemeRuleEntity entity) {
		return new ModelMapper().map(entity, StagingPolicySchemeRule.class);
	}
	
	public static PolicySchemeEntity stgPolicySchemeRuleToMasterEntity(StagingPolicySchemeRule entity) {
		return new ModelMapper().map(entity, PolicySchemeEntity.class);
	}

	public static List<PolicyLifeCoverAndGratuityDto>  lifeandGratToDto(List<PolicyLifeCoverDto> lifeCoverDtoList,
			List<MemberCategoryDto> memberCategoryDtoList, List<PolicyGratuityBenefitDto> gratuityBenefitDtoList) {
	
			List<PolicyLifeCoverAndGratuityDto> lifeCoverAndGratuityDtolist1=new ArrayList<PolicyLifeCoverAndGratuityDto>();
			List<PolicyLifeCoverAndGratuityDto> lifeCoverAndGratuityDtolists=(lifeCoverDtoList.stream().map(PolicySchemeRuleHelper::singleDtoToJoinDto)
					.collect(Collectors.toList()));
			
			for(PolicyLifeCoverAndGratuityDto item :lifeCoverAndGratuityDtolists ) {
				PolicyLifeCoverAndGratuityDto getlifeCoverAndGratuityDtolist=item;
				
				for (PolicyGratuityBenefitDto getGratuityBenefitDto : gratuityBenefitDtoList) {
					
					if (getlifeCoverAndGratuityDtolist.getCategoryId().equals(getGratuityBenefitDto.getCategoryId())) {
						getlifeCoverAndGratuityDtolist
								.setGratutiySubBenefitId(getGratuityBenefitDto.getGratutiySubBenefitId());
						getlifeCoverAndGratuityDtolist
								.setGratutiyBenefitTypeId(getGratuityBenefitDto.getGratutiyBenefitTypeId());
						getlifeCoverAndGratuityDtolist.setGratuityBenefitId(getGratuityBenefitDto.getId());
						if (getGratuityBenefitDto.getCategoryId().equals( item.getCategoryId())) {
							getlifeCoverAndGratuityDtolist.setPolicyGratuityBenefitProps(
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


	
	public static PolicyLifeCoverAndGratuityDto singleDtoToJoinDto(PolicyLifeCoverDto policyLifeCoverDto) {
		return new ModelMapper().map(policyLifeCoverDto, PolicyLifeCoverAndGratuityDto.class);
	}

	public static PolicySchemeRuleHistoryEntity policyMastertoHistransfer(PolicySchemeEntity policySchemeEntity, Long hisPolicyId, String username) {
		PolicySchemeRuleHistoryEntity policySchemeRuleHistoryEntity = new ModelMapper().map(policySchemeEntity, PolicySchemeRuleHistoryEntity.class);
		policySchemeRuleHistoryEntity.setId(null);
		if (policySchemeRuleHistoryEntity.getModifiedDate() == null) {
			policySchemeRuleHistoryEntity.setPolicyId(hisPolicyId);

			//			policyHistoryEntity.setEffectiveFromDate(masterPolicyEntity.getCreatedDate());
//			policyHistoryEntity.setEffectiveToDate(new Date());
			
			policySchemeRuleHistoryEntity.setCreatedBy(username);
			policySchemeRuleHistoryEntity.setCreatedDate(new Date());
			policySchemeRuleHistoryEntity.setModifiedBy(null);
			policySchemeRuleHistoryEntity.setModifiedDate(new Date());
		} else {
			Timestamp effictiveDate = new Timestamp(policySchemeRuleHistoryEntity.getModifiedDate().getTime() + (60 * 1000L));
			policySchemeRuleHistoryEntity.setPolicyId(hisPolicyId);
//			policyHistoryEntity.setEffectiveFromDate(effictiveDate);
//			policyHistoryEntity.setEffectiveToDate(new Date());
			policySchemeRuleHistoryEntity.setCreatedBy(username);
			policySchemeRuleHistoryEntity.setCreatedDate(new Date());
			policySchemeRuleHistoryEntity.setModifiedBy(null);
			policySchemeRuleHistoryEntity.setModifiedDate(new Date());
		}
		return policySchemeRuleHistoryEntity;
	}

	public static PolicySchemeEntity updateTempToSchemeMaster(RenewalSchemeruleTMPEntity renewalSchemeruleTMPEntity,
			String username) {
		PolicySchemeEntity policySchemeEntity =new ModelMapper().map(renewalSchemeruleTMPEntity, PolicySchemeEntity.class);
		policySchemeEntity.setId(renewalSchemeruleTMPEntity.getPmstSchemeRuleId());
		policySchemeEntity.setModifiedBy(username);
		policySchemeEntity.setModifiedDate(new Date());
		policySchemeEntity.setIsActive(true);
		return policySchemeEntity;
	}
	
	
	public static PolicySchemeEntity createTempToSchemeMaster(RenewalSchemeruleTMPEntity renewalSchemeruleTMPEntity,
			String username,Long masterPolicyId) {
		PolicySchemeEntity policySchemeEntity =new ModelMapper().map(renewalSchemeruleTMPEntity, PolicySchemeEntity.class);
		policySchemeEntity.setId(null);
		policySchemeEntity.setPolicyId(masterPolicyId);
		policySchemeEntity.setModifiedBy(username);
		policySchemeEntity.setModifiedDate(new Date());
		policySchemeEntity.setIsActive(true);
		return policySchemeEntity;
	}
}
