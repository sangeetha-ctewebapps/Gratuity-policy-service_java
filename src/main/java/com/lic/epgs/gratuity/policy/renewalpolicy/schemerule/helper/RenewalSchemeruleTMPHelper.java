package com.lic.epgs.gratuity.policy.renewalpolicy.schemerule.helper;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;

import com.lic.epgs.gratuity.common.dto.MemberCategoryDto;
import com.lic.epgs.gratuity.policy.renewalpolicy.gratuitybenefit.dto.RenewalGratuityBenefitTMPDto;
import com.lic.epgs.gratuity.policy.renewalpolicy.gratuitybenefit.entity.RenewalGratuityBenefitTMPEntity;
import com.lic.epgs.gratuity.policy.renewalpolicy.lifecover.dto.RenewalLifeCoverTMPDto;
import com.lic.epgs.gratuity.policy.renewalpolicy.lifecover.entity.RenewalLifeCoverTMPEntity;
import com.lic.epgs.gratuity.policy.renewalpolicy.schemerule.dto.RenewalLifeCoverAndGratuityDto;
import com.lic.epgs.gratuity.policy.renewalpolicy.schemerule.entity.RenewalSchemeruleTMPEntity;
import com.lic.epgs.gratuity.policy.schemerule.entity.PolicySchemeEntity;
import com.lic.epgs.gratuity.policy.schemerule.entity.PolicySchemeRuleHistoryEntity;
import com.lic.epgs.gratuity.quotation.gratuitybenefit.entity.GratuityBenefitEntity;

public class RenewalSchemeruleTMPHelper {

	public static RenewalSchemeruleTMPEntity pmsttoTmp(PolicySchemeEntity policySchemeEntity) {

		return new ModelMapper().map(policySchemeEntity, RenewalSchemeruleTMPEntity.class);
	}

	public static RenewalSchemeruleTMPEntity histtoTmp(PolicySchemeRuleHistoryEntity policySchemeRuleHistoryEntity) {

		return new ModelMapper().map(policySchemeRuleHistoryEntity, RenewalSchemeruleTMPEntity.class);
	}
	
	
	
	public static RenewalLifeCoverAndGratuityDto singleDtoToJoinDto(RenewalLifeCoverTMPDto renewalLifeCoverTMPDto) {

		return new ModelMapper().map(renewalLifeCoverTMPDto, RenewalLifeCoverAndGratuityDto.class);
	}

	public static List<RenewalLifeCoverAndGratuityDto> lifeandGratToDto(
			List<RenewalLifeCoverTMPDto> renewalLifeCoverTMPDto, List<MemberCategoryDto> getMemberCategoryDto,
			List<RenewalGratuityBenefitTMPDto> renewalGratuityBenefitTMPDto) {

		List<RenewalLifeCoverAndGratuityDto> renewalLifeCoverAndGratuityDtolist1 = new ArrayList<RenewalLifeCoverAndGratuityDto>();
		List<RenewalLifeCoverAndGratuityDto> renewalLifeCoverAndGratuityDtolists = (renewalLifeCoverTMPDto.stream()
				.map(RenewalSchemeruleTMPHelper::singleDtoToJoinDto).collect(Collectors.toList()));

		for (RenewalLifeCoverAndGratuityDto item : renewalLifeCoverAndGratuityDtolists) {
			RenewalLifeCoverAndGratuityDto getrenewalLifeCoverAndGratuityDtolist = item;

			for (RenewalGratuityBenefitTMPDto getRenewalGratuityBenefitTMPDto : renewalGratuityBenefitTMPDto) {

				if (getrenewalLifeCoverAndGratuityDtolist.getCategoryId()
						.equals(getRenewalGratuityBenefitTMPDto.getCategoryId())) {
					getrenewalLifeCoverAndGratuityDtolist
							.setGratutiySubBenefitId(getRenewalGratuityBenefitTMPDto.getGratutiySubBenefitId());
					getrenewalLifeCoverAndGratuityDtolist
							.setGratutiyBenefitTypeId(getRenewalGratuityBenefitTMPDto.getGratutiyBenefitTypeId());
					getrenewalLifeCoverAndGratuityDtolist.setGratuityBenefitId(getRenewalGratuityBenefitTMPDto.getId());
					getrenewalLifeCoverAndGratuityDtolist.setPmstGratutiyBenefitId(getRenewalGratuityBenefitTMPDto.getPmstGratutiyBenefitId());		
					getrenewalLifeCoverAndGratuityDtolist.setGratuityNoSlabBenefitTypeId(getRenewalGratuityBenefitTMPDto.getGratuityNoSlabBenefitTypeId());
                  
					if (getRenewalGratuityBenefitTMPDto.getCategoryId().equals(item.getCategoryId())) {
						getrenewalLifeCoverAndGratuityDtolist
								.setRenewalGratuityBenefitProps(getRenewalGratuityBenefitTMPDto
										.getRenewalGraBenePropTmpDto().stream().collect(Collectors.toList()));
					}

					for (MemberCategoryDto memberCategoryDto : getMemberCategoryDto) {
						if (memberCategoryDto.getId().equals(item.getCategoryId())) {
							getrenewalLifeCoverAndGratuityDtolist.setCategoryName(memberCategoryDto.getName());
						}
					}
				}
			}
			renewalLifeCoverAndGratuityDtolist1.add(getrenewalLifeCoverAndGratuityDtolist);
		}
		return renewalLifeCoverAndGratuityDtolist1;

	}

	public static RenewalGratuityBenefitTMPEntity dtoLifeandGratToEntity(
			RenewalLifeCoverAndGratuityDto renewalLifeCoverAndGratuityDto) {

		return new ModelMapper().map(renewalLifeCoverAndGratuityDto, RenewalGratuityBenefitTMPEntity.class);
	}

}
