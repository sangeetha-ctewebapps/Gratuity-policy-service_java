package com.lic.epgs.gratuity.policy.helper;

import org.modelmapper.ModelMapper;

import com.lic.epgs.gratuity.policy.dto.PmstTempMemTrfPropsDto;
import com.lic.epgs.gratuity.policy.dto.PmstTmpConversionPropsDto;
import com.lic.epgs.gratuity.policy.dto.PmstTmpMergerPropsDto;
import com.lic.epgs.gratuity.policy.dto.PmstTmpPolTrfPropsDto;
import com.lic.epgs.gratuity.policy.entity.PmstTempMemTrfPropsEntity;
import com.lic.epgs.gratuity.policy.entity.PmstTmpConversionPropsEntity;
import com.lic.epgs.gratuity.policy.entity.PmstTmpMergerPropsEntity;
import com.lic.epgs.gratuity.policy.entity.PmstTmpPolTrfPropsEntity;
import com.lic.epgs.gratuity.policy.renewalpolicy.entitiy.RenewalPolicyTMPEntity;
import com.lic.epgs.gratuity.policy.renewalpolicy.member.entity.RenewalPolicyTMPMemberEntity;

public class CommonPolicyServiceHelper {

	public static PmstTmpMergerPropsDto entitytoDto(PmstTmpMergerPropsEntity pmstTmpMergerPropsEntity) {
		
		return new ModelMapper().map(pmstTmpMergerPropsEntity, PmstTmpMergerPropsDto.class);
	}

	
public static PmstTmpPolTrfPropsDto entitytoDto(PmstTmpPolTrfPropsEntity pmstTmpPolTrfPropsEntity) {
		
		return new ModelMapper().map(pmstTmpPolTrfPropsEntity, PmstTmpPolTrfPropsDto.class);
	}

	public static PmstTmpConversionPropsDto entitytoDto(PmstTmpConversionPropsEntity get) {
		
		return  new ModelMapper().map(get, PmstTmpConversionPropsDto.class);
	}

	public static RenewalPolicyTMPMemberEntity copytomembersourcetodest(
			RenewalPolicyTMPMemberEntity getRenewalPolicyTMPMemberEntity,
			RenewalPolicyTMPEntity desrenewalPolicyTMPEntity) {
		getRenewalPolicyTMPMemberEntity.setId(null);
		getRenewalPolicyTMPMemberEntity.setTmpPolicyId(desrenewalPolicyTMPEntity.getId());
		return getRenewalPolicyTMPMemberEntity;
	}
	
	

}
