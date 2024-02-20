package com.lic.epgs.gratuity.policyservices.memberlevel.transfer.helper;

import org.modelmapper.ModelMapper;

import com.lic.epgs.gratuity.policy.claim.entity.PolicyTempSearchEntity;
import com.lic.epgs.gratuity.policyservices.memberlevel.transfer.dto.AdjustmentPropsDto;
import com.lic.epgs.gratuity.policyservices.memberlevel.transfer.entity.ContriAdjustmentPropsEntity;

public class ContriHelper {
	
	
	public static AdjustmentPropsDto   entityToDto(ContriAdjustmentPropsEntity contriAdjustmentPropsEntity) {
		return new ModelMapper().map(contriAdjustmentPropsEntity, AdjustmentPropsDto.class);
	}
	
	public static AdjustmentPropsDto tmppolicytocontributiondto(PolicyTempSearchEntity policyTempSearchEntity) {
		return new ModelMapper().map(policyTempSearchEntity, AdjustmentPropsDto.class);
	}

}
