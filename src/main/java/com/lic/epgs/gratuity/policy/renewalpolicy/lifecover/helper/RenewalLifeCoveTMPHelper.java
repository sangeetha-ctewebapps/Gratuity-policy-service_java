package com.lic.epgs.gratuity.policy.renewalpolicy.lifecover.helper;

import java.util.List;

import org.modelmapper.ModelMapper;

import com.lic.epgs.gratuity.common.dto.MemberCategoryDto;
import com.lic.epgs.gratuity.common.entity.MemberCategoryEntity;
import com.lic.epgs.gratuity.policy.lifecover.entity.HistoryLifeCoverEntity;
import com.lic.epgs.gratuity.policy.lifecover.entity.PolicyLifeCoverEntity;
import com.lic.epgs.gratuity.policy.member.entity.HistoryMemberEntity;
import com.lic.epgs.gratuity.policy.renewalpolicy.lifecover.dto.RenewalLifeCoverTMPDto;
import com.lic.epgs.gratuity.policy.renewalpolicy.lifecover.entity.RenewalLifeCoverTMPEntity;
import com.lic.epgs.gratuity.policy.renewalpolicy.member.entity.RenewalPolicyTMPMemberEntity;
import com.lic.epgs.gratuity.quotation.lifecover.dto.LifeCoverDto;
import com.lic.epgs.gratuity.quotation.lifecover.entity.MasterLifeCoverEntity;

public class RenewalLifeCoveTMPHelper {

	public static RenewalLifeCoverTMPEntity pmsttoTmp(PolicyLifeCoverEntity lifeCoverEntity) {
	
		return new ModelMapper().map(lifeCoverEntity, RenewalLifeCoverTMPEntity.class);
	}
	
	

	public static RenewalLifeCoverTMPDto entityToDto(RenewalLifeCoverTMPEntity renewalLifeCoverTMPEntity) {
	
		return new ModelMapper().map(renewalLifeCoverTMPEntity, RenewalLifeCoverTMPDto.class);
	}

	public static MemberCategoryDto memberEntitestoDto(MemberCategoryEntity entity) {
		return new ModelMapper().map(entity, MemberCategoryDto.class);
	}



	public static RenewalLifeCoverTMPEntity histtoTmp(HistoryLifeCoverEntity historyLifeCoverEntity) {
return new ModelMapper().map(historyLifeCoverEntity, RenewalLifeCoverTMPEntity.class);
	

	}
}
