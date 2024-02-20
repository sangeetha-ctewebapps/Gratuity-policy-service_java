package com.lic.epgs.gratuity.policy.renewalpolicy.valuationmatrix.helper;

import org.modelmapper.ModelMapper;

import com.lic.epgs.gratuity.policy.renewalpolicy.valuationmatrix.dto.RenewalValuationBasicTMPDto;
import com.lic.epgs.gratuity.policy.renewalpolicy.valuationmatrix.dto.RenewalValuationMatrixDto;
import com.lic.epgs.gratuity.policy.renewalpolicy.valuationmatrix.dto.RenewalValuationWithdrawalTMPDto;
import com.lic.epgs.gratuity.policy.renewalpolicy.valuationmatrix.entity.RenewalValuationBasicTMPEntity;
import com.lic.epgs.gratuity.policy.renewalpolicy.valuationmatrix.entity.RenewalValuationMatrixTMPEntity;
import com.lic.epgs.gratuity.policy.renewalpolicy.valuationmatrix.entity.RenewalValuationWithdrawalTMPEntity;

public class RenewalValuationTMPHelper {

	public static RenewalValuationBasicTMPEntity dtoToEntity1(RenewalValuationBasicTMPDto renewalValuationBasicTMPDto) {
	
		return new ModelMapper().map(renewalValuationBasicTMPDto, RenewalValuationBasicTMPEntity.class);
	}

	public static RenewalValuationMatrixTMPEntity dtoToEntity2(RenewalValuationMatrixDto renewalValuationMatrixDto) {
			return new ModelMapper().map(renewalValuationMatrixDto, RenewalValuationMatrixTMPEntity.class);
	}

	public static RenewalValuationBasicTMPDto renewalValuationBasicEntityToDto(
			RenewalValuationBasicTMPEntity renewalValuationBasicTMPEntity) {
		RenewalValuationBasicTMPDto get=new ModelMapper().map(renewalValuationBasicTMPEntity, RenewalValuationBasicTMPDto.class);
		get.setGratuityAmountCelling((float)Math.round(renewalValuationBasicTMPEntity.getGratuityAmountCelling()==null?0.0:renewalValuationBasicTMPEntity.getGratuityAmountCelling()));
		get.setMaxLifeCoverSumAssured((float)Math.round(renewalValuationBasicTMPEntity.getMaxLifeCoverSumAssured()==null?0.0:renewalValuationBasicTMPEntity.getMaxLifeCoverSumAssured()));
		get.setMaxSalary((float)Math.round(renewalValuationBasicTMPEntity.getMaxSalary()==null?0.0:renewalValuationBasicTMPEntity.getMaxSalary()));
		get.setPastServiceDeath((float)Math.round(renewalValuationBasicTMPEntity.getPastServiceDeath()==null?0.0:renewalValuationBasicTMPEntity.getPastServiceDeath()));
		get.setPastServiceWithdrawal((float)Math.round(renewalValuationBasicTMPEntity.getPastServiceWithdrawal()==null?0.0:renewalValuationBasicTMPEntity.getPastServiceWithdrawal()));
		get.setPastServiceRetirement((float)Math.round(renewalValuationBasicTMPEntity.getPastServiceRetirement()==null?0.0:renewalValuationBasicTMPEntity.getPastServiceRetirement()));
		get.setCurrentServiceDeath((float)Math.round(renewalValuationBasicTMPEntity.getCurrentServiceDeath()==null?0.0:renewalValuationBasicTMPEntity.getCurrentServiceDeath()));
		get.setGratuityAmountCelling((float)Math.round(renewalValuationBasicTMPEntity.getGratuityAmountCelling()==null?0.0:renewalValuationBasicTMPEntity.getGratuityAmountCelling()));
		get.setCurrentServiceWithdrawal((float)Math.round(renewalValuationBasicTMPEntity.getCurrentServiceWithdrawal()==null?0.0:renewalValuationBasicTMPEntity.getCurrentServiceWithdrawal()));
		get.setCurrentServiceRetirement((float)Math.round(renewalValuationBasicTMPEntity.getCurrentServiceRetirement()==null?0.0:renewalValuationBasicTMPEntity.getCurrentServiceRetirement()));
		get.setAccruedGratuity((float)Math.round(renewalValuationBasicTMPEntity.getAccruedGratuity()==null?0.0:renewalValuationBasicTMPEntity.getAccruedGratuity()));
		get.setTotalGratuity((float)Math.round(renewalValuationBasicTMPEntity.getTotalGratuity()==null?0.0:renewalValuationBasicTMPEntity.getTotalGratuity()));
		get.setMaximumSalary((float)Math.round(renewalValuationBasicTMPEntity.getMaximumSalary()==null?0.0:renewalValuationBasicTMPEntity.getMaximumSalary()));
		get.setMinimumSalary((float)Math.round(renewalValuationBasicTMPEntity.getMinimumSalary()==null?0.0:renewalValuationBasicTMPEntity.getMinimumSalary()));
		get.setIbnrValue((double)Math.round(renewalValuationBasicTMPEntity.getIbnrValue()==null?0.0:renewalValuationBasicTMPEntity.getIbnrValue()));
		get.setStdPremiumRateAocm((double)Math.round(renewalValuationBasicTMPEntity.getStdPremiumRateAocm()==null?0.0:renewalValuationBasicTMPEntity.getStdPremiumRateAocm()));
		get.setModifiedPremiumRateAocm((double)Math.round(renewalValuationBasicTMPEntity.getModifiedPremiumRateAocm()==null?0.0:renewalValuationBasicTMPEntity.getModifiedPremiumRateAocm()));	
		get.setStdPremRateCrdbiltyFctr((double)Math.round(renewalValuationBasicTMPEntity.getStdPremRateCrdbiltyFctr()==null?0.0:renewalValuationBasicTMPEntity.getStdPremRateCrdbiltyFctr()));
		get.setModfdPremRateCrdbiltyFctr((double)Math.round(renewalValuationBasicTMPEntity.getModfdPremRateCrdbiltyFctr()==null?0.0:renewalValuationBasicTMPEntity.getModfdPremRateCrdbiltyFctr()));
		get.setTotalSumAssured((double)Math.round(renewalValuationBasicTMPEntity.getTotalSumAssured()==null?0.0:renewalValuationBasicTMPEntity.getTotalSumAssured()));
		get.setTotalPremium((double)Math.round(renewalValuationBasicTMPEntity.getTotalPremium()==null?0.0:renewalValuationBasicTMPEntity.getTotalPremium()));		
		return get;
	}

	public static RenewalValuationMatrixDto renewalValuationMatrixEntityToDto(
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
		
		return get;
	}
	
	public static RenewalValuationWithdrawalTMPDto renewalValuationWithdrawalRateEntityToDto(
			RenewalValuationWithdrawalTMPEntity renewalValuationWithdrawalTMPEntity) {
	
		return new ModelMapper().map(renewalValuationWithdrawalTMPEntity, RenewalValuationWithdrawalTMPDto.class);
	}


}
