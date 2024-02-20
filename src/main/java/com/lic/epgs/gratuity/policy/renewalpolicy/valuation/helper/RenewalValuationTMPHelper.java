package com.lic.epgs.gratuity.policy.renewalpolicy.valuation.helper;

import org.modelmapper.ModelMapper;

import com.lic.epgs.gratuity.policy.entity.MasterPolicyEntity;
import com.lic.epgs.gratuity.policy.renewalpolicy.dto.RenewalPolicyTMPDto;
import com.lic.epgs.gratuity.policy.renewalpolicy.entitiy.RenewalPolicyTMPEntity;
import com.lic.epgs.gratuity.policy.renewalpolicy.valuation.dto.RenewalValuationTMPDto;
import com.lic.epgs.gratuity.policy.renewalpolicy.valuation.entity.RenewalValuationTMPEntity;
import com.lic.epgs.gratuity.policy.renewalpolicy.valuationmatrix.entity.RenewalValuationWithdrawalTMPEntity;
import com.lic.epgs.gratuity.policy.valuation.entity.PolicyMasterValuationEntity;
import com.lic.epgs.gratuity.policy.valuation.entity.PolicyValuationEntity;
import com.lic.epgs.gratuity.policy.valuation.entity.PolicyValuationHistoryEntity;
import com.lic.epgs.gratuity.policy.valuationmatrix.entity.PolicyValuationWithdrawalRateEntity;
import com.lic.epgs.gratuity.policy.valuationmatrix.entity.PolicyValuationWithdrawalRateHistoryEntity;

public class RenewalValuationTMPHelper {

	public static RenewalValuationTMPEntity pmsttoTmp(PolicyMasterValuationEntity policyMasterValuationEntity) {

		return new ModelMapper().map(policyMasterValuationEntity, RenewalValuationTMPEntity.class) ;
	}

	public static RenewalValuationWithdrawalTMPEntity pmsttoTmp(PolicyValuationWithdrawalRateEntity policyValuationWithdrawalRateEntity) {

		return new ModelMapper().map(policyValuationWithdrawalRateEntity, RenewalValuationWithdrawalTMPEntity.class);
	}

	public static RenewalValuationTMPDto entityToDto(RenewalValuationTMPEntity renewalValuationTMPEntity) {

		return new ModelMapper().map(renewalValuationTMPEntity, RenewalValuationTMPDto.class);
	}

	public static RenewalValuationTMPDto entityToDtoupdate(RenewalValuationTMPEntity renewalValuationTMPEntity) {
		
		return new ModelMapper().map(renewalValuationTMPEntity, RenewalValuationTMPDto.class);
	}
		
	public static RenewalPolicyTMPDto entityToDto(RenewalPolicyTMPEntity renewalPolicyTMPEntity) {

		return new ModelMapper().map(renewalPolicyTMPEntity, RenewalPolicyTMPDto.class);
	}
	
       public static RenewalPolicyTMPDto entityToDtoTemPolicy(RenewalPolicyTMPEntity renewalPolicyTMPEntity) {
		
		return new ModelMapper().map(renewalPolicyTMPEntity, RenewalPolicyTMPDto.class);
	}

	public static RenewalValuationWithdrawalTMPEntity histtoTmp(
			PolicyValuationWithdrawalRateHistoryEntity getPolicyValuationWithdrawalRateHistoryEntity) {
		return new ModelMapper().map(getPolicyValuationWithdrawalRateHistoryEntity, RenewalValuationWithdrawalTMPEntity.class) ;
	}
		
	
	public static RenewalValuationTMPEntity histtoTmp(
			PolicyValuationHistoryEntity  policyValuationHistoryEntity) {
		return new ModelMapper().map(policyValuationHistoryEntity, RenewalValuationTMPEntity.class) ;
	}
		
	


}

