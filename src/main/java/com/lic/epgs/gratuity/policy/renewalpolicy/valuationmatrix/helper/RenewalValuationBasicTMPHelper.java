package com.lic.epgs.gratuity.policy.renewalpolicy.valuationmatrix.helper;

import org.modelmapper.ModelMapper;

import com.lic.epgs.gratuity.policy.renewalpolicy.valuationmatrix.entity.RenewalValuationBasicTMPEntity;
import com.lic.epgs.gratuity.policy.valuationmatrix.entity.PolicyValuationBasicHistoryEntity;
import com.lic.epgs.gratuity.policy.valuationmatrix.entity.PolicyValutationBasicEntity;

public class RenewalValuationBasicTMPHelper {

	public static RenewalValuationBasicTMPEntity pmsttoTmp(PolicyValutationBasicEntity policyValutationBasicEntity) {
	
		return new ModelMapper().map(policyValutationBasicEntity, RenewalValuationBasicTMPEntity.class);
	}

	public static RenewalValuationBasicTMPEntity histtoTmp(
			PolicyValuationBasicHistoryEntity policyValuationBasicHistoryEntity) {
	
		return new ModelMapper().map(policyValuationBasicHistoryEntity, RenewalValuationBasicTMPEntity.class);

	}

}
