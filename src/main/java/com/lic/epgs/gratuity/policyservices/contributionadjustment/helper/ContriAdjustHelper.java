package com.lic.epgs.gratuity.policyservices.contributionadjustment.helper;

import org.modelmapper.ModelMapper;

import com.lic.epgs.gratuity.policy.claim.entity.PolicyTempSearchEntity;
import com.lic.epgs.gratuity.policyservices.contributionadjustment.dto.ContributionAdjustmentPropsDto;
import com.lic.epgs.gratuity.policyservices.contributionadjustment.entity.ContributionAdjustmentPropsEntity;

public class ContriAdjustHelper {
	
	
	public static ContributionAdjustmentPropsDto   entityToDto(ContributionAdjustmentPropsEntity contributionAdjustmentPropsEntity) {
		return new ModelMapper().map(contributionAdjustmentPropsEntity, ContributionAdjustmentPropsDto.class);
	}
	
	public static ContributionAdjustmentPropsDto tmppolicytocontributiondto(PolicyTempSearchEntity policyTempSearchEntity) {
		return new ModelMapper().map(policyTempSearchEntity, ContributionAdjustmentPropsDto.class);
	}

}
