package com.lic.epgs.gratuity.policy.renewalpolicy.valuationmatrix.helper;

import org.modelmapper.ModelMapper;

import com.lic.epgs.gratuity.policy.renewalpolicy.valuationmatrix.dto.RenewalValuationMatrixDto;
import com.lic.epgs.gratuity.policy.renewalpolicy.valuationmatrix.entity.RenewalValuationMatrixTMPEntity;
import com.lic.epgs.gratuity.policy.valuationmatrix.entity.PolicyValuationMatrixEntity;
import com.lic.epgs.gratuity.policy.valuationmatrix.entity.PolicyValuationMatrixHistoryEntity;
import com.lic.epgs.gratuity.quotation.valuationmatrix.entity.ValuationMatrixEntity;

public class RenewalValuationMatrixTMPHelper {

	public static RenewalValuationMatrixTMPEntity pmsttoTmp(PolicyValuationMatrixEntity policyValuationMatrixEntity) {
	
		return new ModelMapper().map(policyValuationMatrixEntity, RenewalValuationMatrixTMPEntity.class);
	}

	public static RenewalValuationMatrixTMPEntity dtoToEntity(RenewalValuationMatrixDto renewalValuationMatrixDto) {
		
		return new ModelMapper().map(renewalValuationMatrixDto, RenewalValuationMatrixTMPEntity.class);
	}

	public static RenewalValuationMatrixDto RenewalValuationMatrixTMPEntitytoDto(RenewalValuationMatrixTMPEntity renewalValuationMatrixTMPEntity) {
		
		return new ModelMapper().map(renewalValuationMatrixTMPEntity, RenewalValuationMatrixDto.class);
	}

	public static RenewalValuationMatrixDto RenewalValuationMatrixEntityToDto(
			RenewalValuationMatrixTMPEntity renewalValuationMatrixTMPEntity) {
		RenewalValuationMatrixDto get=new ModelMapper().map(renewalValuationMatrixTMPEntity, RenewalValuationMatrixDto.class);
		get.setTotalSumAssured((renewalValuationMatrixTMPEntity.getTotalSumAssured()==null?0.0:renewalValuationMatrixTMPEntity.getTotalSumAssured()));
		get.setCurrentServiceLiability((renewalValuationMatrixTMPEntity.getCurrentServiceLiability()==null?0.0:renewalValuationMatrixTMPEntity.getCurrentServiceLiability()));
		get.setPastServiceLiability((renewalValuationMatrixTMPEntity.getPastServiceLiability()==null?0.0:renewalValuationMatrixTMPEntity.getPastServiceLiability()));
		get.setFutureServiceLiability((renewalValuationMatrixTMPEntity.getFutureServiceLiability()==null?0.0:renewalValuationMatrixTMPEntity.getFutureServiceLiability()));
		get.setPremium((renewalValuationMatrixTMPEntity.getPremium()==null?0.0:renewalValuationMatrixTMPEntity.getPremium()));
		get.setGst((renewalValuationMatrixTMPEntity.getGst()==null?0.0:renewalValuationMatrixTMPEntity.getGst()));
		get.setTotalPremium((renewalValuationMatrixTMPEntity.getTotalPremium()==null?0.0:renewalValuationMatrixTMPEntity.getTotalPremium()));
		get.setAmountReceived((renewalValuationMatrixTMPEntity.getAmountReceived()==null?0.0:renewalValuationMatrixTMPEntity.getAmountReceived()));
		get.setAmountPayable((renewalValuationMatrixTMPEntity.getAmountPayable()==null?0.0:renewalValuationMatrixTMPEntity.getAmountPayable()));
		get.setBalanceToBePaid((renewalValuationMatrixTMPEntity.getBalanceToBePaid()==null?0.0:renewalValuationMatrixTMPEntity.getBalanceToBePaid()));
       return get ;
	}

	public static RenewalValuationMatrixTMPEntity histtoTmp(
			PolicyValuationMatrixHistoryEntity policyValuationMatrixHistoryEntity) {
		
		return new ModelMapper().map(policyValuationMatrixHistoryEntity, RenewalValuationMatrixTMPEntity.class);
	}

}
