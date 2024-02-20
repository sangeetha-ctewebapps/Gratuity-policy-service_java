package com.lic.epgs.gratuity.policy.valuationmatrix.helper;

import java.sql.Timestamp;
import java.util.Date;

import org.modelmapper.ModelMapper;

import com.lic.epgs.gratuity.policy.lifecover.entity.HistoryLifeCoverEntity;
import com.lic.epgs.gratuity.policy.lifecover.entity.PolicyLifeCoverEntity;
import com.lic.epgs.gratuity.policy.renewalpolicy.valuationmatrix.entity.RenewalValuationBasicTMPEntity;
import com.lic.epgs.gratuity.policy.renewalpolicy.valuationmatrix.entity.RenewalValuationMatrixTMPEntity;
import com.lic.epgs.gratuity.policy.renewalpolicy.valuationmatrix.entity.RenewalValuationWithdrawalTMPEntity;
import com.lic.epgs.gratuity.policy.schemerule.entity.PolicySchemeRuleHistoryEntity;
import com.lic.epgs.gratuity.policy.valuation.entity.PolicyValuationHistoryEntity;
import com.lic.epgs.gratuity.policy.valuationmatrix.dto.PolicyValuationBasicDto;
import com.lic.epgs.gratuity.policy.valuationmatrix.dto.PolicyValuationMatrixDto;
import com.lic.epgs.gratuity.policy.valuationmatrix.dto.PolicyValuationWithdrawalRateDto;
import com.lic.epgs.gratuity.policy.valuationmatrix.entity.PolicyValuationBasicHistoryEntity;
import com.lic.epgs.gratuity.policy.valuationmatrix.entity.PolicyValuationMatrixEntity;
import com.lic.epgs.gratuity.policy.valuationmatrix.entity.PolicyValuationMatrixHistoryEntity;
import com.lic.epgs.gratuity.policy.valuationmatrix.entity.PolicyValuationWithdrawalRateEntity;
import com.lic.epgs.gratuity.policy.valuationmatrix.entity.PolicyValuationWithdrawalRateHistoryEntity;
import com.lic.epgs.gratuity.policy.valuationmatrix.entity.PolicyValutationBasicEntity;
import com.lic.epgs.gratuity.policy.valuationmatrix.entity.StagingPolicyValuationMatrixEntity;
import com.lic.epgs.gratuity.policy.valuationmatrix.entity.StagingPolicyValuationWithdrawalRateEntity;
import com.lic.epgs.gratuity.policy.valuationmatrix.entity.StagingPolicyValutationBasicEntity;
import com.lic.epgs.gratuity.quotation.valuation.entity.MasterValuationEntity;
import com.lic.epgs.gratuity.quotation.valuation.entity.ValuationEntity;
import com.lic.epgs.gratuity.quotation.valuationmatrix.entity.MasterValuationBasicEntity;
import com.lic.epgs.gratuity.quotation.valuationmatrix.entity.MasterValuationMatrixEntity;
import com.lic.epgs.gratuity.quotation.valuationmatrix.entity.MasterValuationWithdrawalRateEntity;

public class PolicyValuationMatrixHelper {

	public static PolicyValuationMatrixDto valuationEntityToDto(StagingPolicyValuationMatrixEntity stagingPolicyValuationMatrixEntity) {
		PolicyValuationMatrixDto get=new ModelMapper().map(stagingPolicyValuationMatrixEntity, PolicyValuationMatrixDto.class);
		get.setTotalSumAssured((double)Math.round(stagingPolicyValuationMatrixEntity.getTotalSumAssured()==null?0.0:stagingPolicyValuationMatrixEntity.getTotalSumAssured()));
		get.setCurrentServiceLiability((float)Math.round(stagingPolicyValuationMatrixEntity.getCurrentServiceLiability()==null?0.0:stagingPolicyValuationMatrixEntity.getCurrentServiceLiability()));
		get.setPastServiceLiability((float)Math.round(stagingPolicyValuationMatrixEntity.getPastServiceLiability()==null?0.0:stagingPolicyValuationMatrixEntity.getPastServiceLiability()));
		get.setFutureServiceLiability((float)Math.round(stagingPolicyValuationMatrixEntity.getFutureServiceLiability()==null?0.0:stagingPolicyValuationMatrixEntity.getFutureServiceLiability()));
		get.setPremium((float)Math.round(stagingPolicyValuationMatrixEntity.getPremium()==null?0.0:stagingPolicyValuationMatrixEntity.getPremium()));
		get.setGst((float)Math.round(stagingPolicyValuationMatrixEntity.getGst()==null?0.0:stagingPolicyValuationMatrixEntity.getGst()));
		get.setTotalPremium((double)Math.round(stagingPolicyValuationMatrixEntity.getTotalPremium()==null?0.0:stagingPolicyValuationMatrixEntity.getTotalPremium()));
		get.setAmountReceived((float)Math.round(stagingPolicyValuationMatrixEntity.getAmountReceived()==null?0.0:stagingPolicyValuationMatrixEntity.getAmountReceived()));
		get.setAmountPayable((float)Math.round(stagingPolicyValuationMatrixEntity.getAmountPayable()==null?0.0:stagingPolicyValuationMatrixEntity.getAmountPayable()));
		get.setBalanceToBePaid((float)Math.round(stagingPolicyValuationMatrixEntity.getBalanceToBePaid()==null?0.0:stagingPolicyValuationMatrixEntity.getBalanceToBePaid()));
		return get;
	}

	public static PolicyValuationMatrixDto valuationEntityToDto(PolicyValuationMatrixEntity policyValuationMatrixEntity) {
		PolicyValuationMatrixDto get= new ModelMapper().map(policyValuationMatrixEntity, PolicyValuationMatrixDto.class);
		get.setTotalSumAssured((double)Math.round(policyValuationMatrixEntity.getTotalSumAssured()==null?0.0:policyValuationMatrixEntity.getTotalSumAssured()));
		get.setCurrentServiceLiability((float)Math.round(policyValuationMatrixEntity.getCurrentServiceLiability()==null?0.0:policyValuationMatrixEntity.getCurrentServiceLiability()));
		get.setPastServiceLiability((float)Math.round(policyValuationMatrixEntity.getPastServiceLiability()==null?0.0:policyValuationMatrixEntity.getPastServiceLiability()));
		get.setFutureServiceLiability((float)Math.round(policyValuationMatrixEntity.getFutureServiceLiability()==null?0.0:policyValuationMatrixEntity.getFutureServiceLiability()));
		get.setPremium((float)Math.round(policyValuationMatrixEntity.getPremium()==null?0.0:policyValuationMatrixEntity.getPremium()));
		get.setGst((float)Math.round(policyValuationMatrixEntity.getGst()==null?0.0:policyValuationMatrixEntity.getGst()));
		get.setTotalPremium((double)Math.round(policyValuationMatrixEntity.getTotalPremium()==null?0.0:policyValuationMatrixEntity.getTotalPremium()));
		get.setAmountReceived((float)Math.round(policyValuationMatrixEntity.getAmountReceived()==null?0.0:policyValuationMatrixEntity.getAmountReceived()));
		get.setAmountPayable((float)Math.round(policyValuationMatrixEntity.getAmountPayable()==null?0.0:policyValuationMatrixEntity.getAmountPayable()));
		get.setBalanceToBePaid((float)Math.round(policyValuationMatrixEntity.getBalanceToBePaid()==null?0.0:policyValuationMatrixEntity.getBalanceToBePaid()));
		return get;
		
		
		
	}
	
	public static StagingPolicyValuationMatrixEntity masterQuotationentityToPolicyStagingEntity(MasterValuationMatrixEntity entity) {
		return new ModelMapper().map(entity, StagingPolicyValuationMatrixEntity.class);
	}
	
	public static PolicyValuationMatrixEntity policyValuationEntityToMasterEntity(StagingPolicyValuationMatrixEntity entity) {
		return new ModelMapper().map(entity, PolicyValuationMatrixEntity.class);
	}

	public static StagingPolicyValutationBasicEntity masterToStagingDto(
			MasterValuationBasicEntity masterValuationBasicEntity) {
		return new ModelMapper().map(masterValuationBasicEntity, StagingPolicyValutationBasicEntity.class);
	}

	public static PolicyValutationBasicEntity policyValuationstagingToMasterDto(
			StagingPolicyValutationBasicEntity stagingPolicyValutationBasicEntity) {
		return new ModelMapper().map(stagingPolicyValutationBasicEntity, PolicyValutationBasicEntity.class);
	}

	public static StagingPolicyValuationWithdrawalRateEntity masterWntityToPolicyEntity(
			MasterValuationWithdrawalRateEntity getMasterValuationWithdrawalRateEntity) {	
		return new ModelMapper().map(getMasterValuationWithdrawalRateEntity, StagingPolicyValuationWithdrawalRateEntity.class);
	}

	public static PolicyValuationWithdrawalRateEntity stagingTomasterEntity(
			StagingPolicyValuationWithdrawalRateEntity getStagingPolicyValuationWithdrawalRateEntity) {
		return new ModelMapper().map(getStagingPolicyValuationWithdrawalRateEntity, PolicyValuationWithdrawalRateEntity.class);
	}

	public static PolicyValuationBasicDto valuationBasicEntityToDto(
			StagingPolicyValutationBasicEntity stagingPolicyValutationBasicEntity) {
		PolicyValuationBasicDto get=new ModelMapper().map(stagingPolicyValutationBasicEntity, PolicyValuationBasicDto.class);
		get.setGratuityAmountCelling((float)Math.round(stagingPolicyValutationBasicEntity.getGratuityAmountCelling()==null?0.0:stagingPolicyValutationBasicEntity.getGratuityAmountCelling()));
		get.setMaxLifeCoverSumAssured((float)Math.round(stagingPolicyValutationBasicEntity.getMaxLifeCoverSumAssured()==null?0.0:stagingPolicyValutationBasicEntity.getMaxLifeCoverSumAssured()));
		get.setMaxSalary((float)Math.round(stagingPolicyValutationBasicEntity.getMaxSalary()==null?0.0:stagingPolicyValutationBasicEntity.getMaxSalary()));
		get.setPastServiceDeath((float)Math.round(stagingPolicyValutationBasicEntity.getPastServiceDeath()==null?0.0:stagingPolicyValutationBasicEntity.getPastServiceDeath()));
		get.setPastServiceWithdrawal((float)Math.round(stagingPolicyValutationBasicEntity.getPastServiceWithdrawal()==null?0.0:stagingPolicyValutationBasicEntity.getPastServiceWithdrawal()));
		get.setPastServiceRetirement((float)Math.round(stagingPolicyValutationBasicEntity.getPastServiceRetirement()==null?0.0:stagingPolicyValutationBasicEntity.getPastServiceRetirement()));
		get.setCurrentServiceDeath((float)Math.round(stagingPolicyValutationBasicEntity.getCurrentServiceDeath()==null?0.0:stagingPolicyValutationBasicEntity.getCurrentServiceDeath()));
		get.setCurrentServiceWithdrawal((float)Math.round(stagingPolicyValutationBasicEntity.getCurrentServiceWithdrawal()==null?0.0:stagingPolicyValutationBasicEntity.getCurrentServiceWithdrawal()));
		get.setCurrentServiceRetirement((float)Math.round(stagingPolicyValutationBasicEntity.getCurrentServiceRetirement()==null?0.0:stagingPolicyValutationBasicEntity.getCurrentServiceRetirement()));
		get.setAccruedGratuity((float)Math.round(stagingPolicyValutationBasicEntity.getAccruedGratuity()==null?0.0:stagingPolicyValutationBasicEntity.getAccruedGratuity()));
		get.setTotalGratuity((float)Math.round(stagingPolicyValutationBasicEntity.getTotalGratuity()==null?0.0:stagingPolicyValutationBasicEntity.getTotalGratuity()));
		get.setMaximumSalary((float)Math.round(stagingPolicyValutationBasicEntity.getMaximumSalary()==null?0.0:stagingPolicyValutationBasicEntity.getMaximumSalary()));
		get.setMinimumSalary((float)Math.round(stagingPolicyValutationBasicEntity.getMinimumSalary()==null?0.0:stagingPolicyValutationBasicEntity.getMinimumSalary()));
		return get;
	}

	public static PolicyValuationMatrixDto valuationMatrixEntityToDto(
			StagingPolicyValuationMatrixEntity stagingPolicyValuationMatrixEntity) {
		PolicyValuationMatrixDto get=new ModelMapper().map(stagingPolicyValuationMatrixEntity, PolicyValuationMatrixDto.class);
		get.setTotalSumAssured((double)Math.round(stagingPolicyValuationMatrixEntity.getTotalSumAssured()==null?0.0:stagingPolicyValuationMatrixEntity.getTotalSumAssured()));
		get.setCurrentServiceLiability((float)Math.round(stagingPolicyValuationMatrixEntity.getCurrentServiceLiability()==null?0.0:stagingPolicyValuationMatrixEntity.getCurrentServiceLiability()));
		get.setPastServiceLiability((float)Math.round(stagingPolicyValuationMatrixEntity.getPastServiceLiability()==null?0.0:stagingPolicyValuationMatrixEntity.getPastServiceLiability()));
		get.setFutureServiceLiability((float)Math.round(stagingPolicyValuationMatrixEntity.getFutureServiceLiability()==null?0.0:stagingPolicyValuationMatrixEntity.getFutureServiceLiability()));
		get.setPremium((float)Math.round(stagingPolicyValuationMatrixEntity.getPremium()==null?0.0:stagingPolicyValuationMatrixEntity.getPremium()));
		get.setGst((float)Math.round(stagingPolicyValuationMatrixEntity.getGst()==null?0.0:stagingPolicyValuationMatrixEntity.getGst()));
		get.setTotalPremium((double)Math.round(stagingPolicyValuationMatrixEntity.getTotalPremium()==null?0.0:stagingPolicyValuationMatrixEntity.getTotalPremium()));
		get.setAmountReceived((float)Math.round(stagingPolicyValuationMatrixEntity.getAmountReceived()==null?0.0:stagingPolicyValuationMatrixEntity.getAmountReceived()));
		get.setAmountPayable((float)Math.round(stagingPolicyValuationMatrixEntity.getAmountPayable()==null?0.0:stagingPolicyValuationMatrixEntity.getAmountPayable()));
		get.setBalanceToBePaid((float)Math.round(stagingPolicyValuationMatrixEntity.getBalanceToBePaid()==null?0.0:stagingPolicyValuationMatrixEntity.getBalanceToBePaid()));
	
		return get; 
	}
	
	public static PolicyValuationWithdrawalRateDto valuationWithdrawalRateEntityToDto(
			StagingPolicyValuationWithdrawalRateEntity stagingPolicyValuationWithdrawalRateEntity) {
	
		return new ModelMapper().map(stagingPolicyValuationWithdrawalRateEntity, PolicyValuationWithdrawalRateDto.class);
	}

	public static PolicyValuationBasicDto valuationBasicEntityToDto(PolicyValutationBasicEntity policyValutationBasicEntity) {
		PolicyValuationBasicDto	get=new ModelMapper().map(policyValutationBasicEntity, PolicyValuationBasicDto.class);
		get.setGratuityAmountCelling((float)Math.round(policyValutationBasicEntity.getGratuityAmountCelling()==null?0.0:policyValutationBasicEntity.getGratuityAmountCelling()));
		get.setMaxLifeCoverSumAssured((float)Math.round(policyValutationBasicEntity.getMaxLifeCoverSumAssured()==null?0.0:policyValutationBasicEntity.getMaxLifeCoverSumAssured()));
		get.setMaxSalary((float)Math.round(policyValutationBasicEntity.getMaxSalary()==null?0.0:policyValutationBasicEntity.getMaxSalary()));
		get.setPastServiceDeath((float)Math.round(policyValutationBasicEntity.getPastServiceDeath()==null?0.0:policyValutationBasicEntity.getPastServiceDeath()));
		get.setPastServiceWithdrawal((float)Math.round(policyValutationBasicEntity.getPastServiceWithdrawal()==null?0.0:policyValutationBasicEntity.getPastServiceWithdrawal()));
		get.setPastServiceRetirement((float)Math.round(policyValutationBasicEntity.getPastServiceRetirement()==null?0.0:policyValutationBasicEntity.getPastServiceRetirement()));
		get.setCurrentServiceDeath((float)Math.round(policyValutationBasicEntity.getCurrentServiceDeath()==null?0.0:policyValutationBasicEntity.getCurrentServiceDeath()));
		get.setCurrentServiceWithdrawal((float)Math.round(policyValutationBasicEntity.getCurrentServiceWithdrawal()==null?0.0:policyValutationBasicEntity.getCurrentServiceWithdrawal()));
		get.setCurrentServiceRetirement((float)Math.round(policyValutationBasicEntity.getCurrentServiceRetirement()==null?0.0:policyValutationBasicEntity.getCurrentServiceRetirement()));
		get.setAccruedGratuity((float)Math.round(policyValutationBasicEntity.getAccruedGratuity()==null?0.0:policyValutationBasicEntity.getAccruedGratuity()));
		get.setTotalGratuity((float)Math.round(policyValutationBasicEntity.getTotalGratuity()==null?0.0:policyValutationBasicEntity.getTotalGratuity()));
		get.setMaximumSalary((float)Math.round(policyValutationBasicEntity.getMaximumSalary()==null?0.0:policyValutationBasicEntity.getMaximumSalary()));
		get.setMinimumSalary((float)Math.round(policyValutationBasicEntity.getMinimumSalary()==null?0.0:policyValutationBasicEntity.getMinimumSalary()));
		return get;
		
		
	}

	public static PolicyValuationMatrixDto valuationMatrixEntityToDto(
			PolicyValuationMatrixEntity policyValuationMatrixEntity) {
		PolicyValuationMatrixDto get= new ModelMapper().map(policyValuationMatrixEntity, PolicyValuationMatrixDto.class);
		get.setTotalSumAssured((double)Math.round(policyValuationMatrixEntity.getTotalSumAssured()==null?0.0:policyValuationMatrixEntity.getTotalSumAssured()));
		get.setCurrentServiceLiability((float)Math.round(policyValuationMatrixEntity.getCurrentServiceLiability()==null?0.0:policyValuationMatrixEntity.getCurrentServiceLiability()));
		get.setPastServiceLiability((float)Math.round(policyValuationMatrixEntity.getPastServiceLiability()==null?0.0:policyValuationMatrixEntity.getPastServiceLiability()));
		get.setFutureServiceLiability((float)Math.round(policyValuationMatrixEntity.getFutureServiceLiability()==null?0.0:policyValuationMatrixEntity.getFutureServiceLiability()));
		get.setPremium((float)Math.round(policyValuationMatrixEntity.getPremium()==null?0.0:policyValuationMatrixEntity.getPremium()));
		get.setGst((float)Math.round(policyValuationMatrixEntity.getGst()==null?0.0:policyValuationMatrixEntity.getGst()));
		get.setTotalPremium((double)Math.round(policyValuationMatrixEntity.getTotalPremium()==null?0.0:policyValuationMatrixEntity.getTotalPremium()));
		get.setAmountReceived((float)Math.round(policyValuationMatrixEntity.getAmountReceived()==null?0.0:policyValuationMatrixEntity.getAmountReceived()));
		get.setAmountPayable((float)Math.round(policyValuationMatrixEntity.getAmountPayable()==null?0.0:policyValuationMatrixEntity.getAmountPayable()));
		get.setBalanceToBePaid((float)Math.round(policyValuationMatrixEntity.getBalanceToBePaid()==null?0.0:policyValuationMatrixEntity.getBalanceToBePaid()));
		
		return get;
	}

	public static PolicyValuationWithdrawalRateDto valuationWithdrawalRateEntityToDto(
			PolicyValuationWithdrawalRateEntity policyValuationWithdrawalRateEntity) {
	
		return new ModelMapper().map(policyValuationWithdrawalRateEntity, PolicyValuationWithdrawalRateDto.class);
	}

	public static PolicyValuationMatrixHistoryEntity policyMastertoHistransfer(PolicyValuationMatrixEntity policyValuationMatrixEntity, Long id,
			String username) {
		
		PolicyValuationMatrixHistoryEntity policyValuationMatrixHistoryEntity = new ModelMapper().map(policyValuationMatrixEntity, PolicyValuationMatrixHistoryEntity.class);
		policyValuationMatrixHistoryEntity.setId(null);
		if (policyValuationMatrixHistoryEntity.getModifiedDate() == null) {
			policyValuationMatrixHistoryEntity.setPolicyId(id);
			policyValuationMatrixHistoryEntity.setCreatedBy(username);
			policyValuationMatrixHistoryEntity.setCreatedDate(new Date());
			policyValuationMatrixHistoryEntity.setModifiedBy(null);
			policyValuationMatrixHistoryEntity.setModifiedDate(new Date());
		} else {
			Timestamp effictiveDate = new Timestamp(policyValuationMatrixHistoryEntity.getModifiedDate().getTime() + (60 * 1000L));
			policyValuationMatrixHistoryEntity.setPolicyId(id);
			policyValuationMatrixHistoryEntity.setCreatedBy(username);
			policyValuationMatrixHistoryEntity.setCreatedDate(new Date());
			policyValuationMatrixHistoryEntity.setModifiedBy(null);
			policyValuationMatrixHistoryEntity.setModifiedDate(new Date());
		}
		return policyValuationMatrixHistoryEntity;
	}
	
	
	public static PolicyValuationBasicHistoryEntity policyMastertoHistransfer(PolicyValutationBasicEntity policyValutationBasicEntity, Long id,
			String username) {
		
		PolicyValuationBasicHistoryEntity policyValuationBasicHistoryEntity = new ModelMapper().map(policyValutationBasicEntity, PolicyValuationBasicHistoryEntity.class);
		policyValuationBasicHistoryEntity.setId(null);
		if (policyValuationBasicHistoryEntity.getModifiedDate() == null) {
			policyValuationBasicHistoryEntity.setPolicyId(id);
			policyValuationBasicHistoryEntity.setCreatedBy(username);
			policyValuationBasicHistoryEntity.setCreatedDate(new Date());
			policyValuationBasicHistoryEntity.setModifiedBy(null);
			policyValuationBasicHistoryEntity.setModifiedDate(new Date());
		} else {
			Timestamp effictiveDate = new Timestamp(policyValuationBasicHistoryEntity.getModifiedDate().getTime() + (60 * 1000L));
			policyValuationBasicHistoryEntity.setPolicyId(id);
			policyValuationBasicHistoryEntity.setCreatedBy(username);
			policyValuationBasicHistoryEntity.setCreatedDate(new Date());
			policyValuationBasicHistoryEntity.setModifiedBy(null);
			policyValuationBasicHistoryEntity.setModifiedDate(new Date());
		}
		return policyValuationBasicHistoryEntity;
	}
	
	
	public static PolicyValuationMatrixEntity updateTempToValuationMatrixMaster(
			RenewalValuationMatrixTMPEntity renewalValuationMatrixTMPEntity, String username) {
		
		PolicyValuationMatrixEntity policyValuationMatrixEntity =new ModelMapper().map(renewalValuationMatrixTMPEntity, PolicyValuationMatrixEntity.class);
		policyValuationMatrixEntity.setId(renewalValuationMatrixTMPEntity.getPmstValuationMatrixId());
		policyValuationMatrixEntity.setModifiedBy(username);
		policyValuationMatrixEntity.setIsActive(true);
		return policyValuationMatrixEntity;
	}
	
	public static PolicyValuationMatrixEntity createTempToValuationMatrixMaster(
			RenewalValuationMatrixTMPEntity renewalValuationMatrixTMPEntity, String username) {
		
		PolicyValuationMatrixEntity policyValuationMatrixEntity =new ModelMapper().map(renewalValuationMatrixTMPEntity, PolicyValuationMatrixEntity.class);
		policyValuationMatrixEntity.setId(null);
		policyValuationMatrixEntity.setCreatedBy(username);
		policyValuationMatrixEntity.setCreatedDate(new Date());
		policyValuationMatrixEntity.setIsActive(true);
		return policyValuationMatrixEntity;
	}

	public static PolicyValutationBasicEntity updateTempToValuationBasicMaster(
			RenewalValuationBasicTMPEntity renewalValuationBasicTMPEntity, String username) {
		
		PolicyValutationBasicEntity policyValutationBasicEntity =new ModelMapper().map(renewalValuationBasicTMPEntity, PolicyValutationBasicEntity.class);
		policyValutationBasicEntity.setId(renewalValuationBasicTMPEntity.getPmstValuationBasicId());
		policyValutationBasicEntity.setModifiedBy(username);
		policyValutationBasicEntity.setIsActive(true);
		return policyValutationBasicEntity;
	}
	
	public static PolicyValutationBasicEntity createTempToValuationBasicMaster(
			RenewalValuationBasicTMPEntity renewalValuationBasicTMPEntity, String username) {
		
		PolicyValutationBasicEntity policyValutationBasicEntity =new ModelMapper().map(renewalValuationBasicTMPEntity, PolicyValutationBasicEntity.class);
		policyValutationBasicEntity.setId(null);
		policyValutationBasicEntity.setCreatedBy(username);
		policyValutationBasicEntity.setCreatedDate(new Date());	
		policyValutationBasicEntity.setIsActive(true);
		return policyValutationBasicEntity;
	}
	
	
	

	public static PolicyValuationWithdrawalRateHistoryEntity policyMastertoHistransfer(PolicyValuationWithdrawalRateEntity addPolicyValuationWithdrawalRateEntity, Long hisPolicyId,
			String username) {
		PolicyValuationWithdrawalRateHistoryEntity policyValuationWithdrawalRateHistoryEntity = new ModelMapper().map(addPolicyValuationWithdrawalRateEntity, PolicyValuationWithdrawalRateHistoryEntity.class);
		policyValuationWithdrawalRateHistoryEntity.setId(null);
		if (policyValuationWithdrawalRateHistoryEntity.getModifiedDate() == null) {
			policyValuationWithdrawalRateHistoryEntity.setPolicyId(hisPolicyId);
			policyValuationWithdrawalRateHistoryEntity.setCreatedBy(username);
			policyValuationWithdrawalRateHistoryEntity.setCreatedDate(new Date());
			policyValuationWithdrawalRateHistoryEntity.setModifiedBy(null);
			policyValuationWithdrawalRateHistoryEntity.setModifiedDate(new Date());
		} else {
			Timestamp effictiveDate = new Timestamp(policyValuationWithdrawalRateHistoryEntity.getModifiedDate().getTime() + (60 * 1000L));
			policyValuationWithdrawalRateHistoryEntity.setPolicyId(hisPolicyId);
			policyValuationWithdrawalRateHistoryEntity.setCreatedBy(username);
			policyValuationWithdrawalRateHistoryEntity.setCreatedDate(new Date());
			policyValuationWithdrawalRateHistoryEntity.setModifiedBy(null);
			policyValuationWithdrawalRateHistoryEntity.setModifiedDate(new Date());
		}
		return policyValuationWithdrawalRateHistoryEntity;
	}

	public static PolicyValuationWithdrawalRateEntity updateTempToLifecoverMaster(
			RenewalValuationWithdrawalTMPEntity renewalValuationWithdrawalTMPEntity, String username) {
	

		PolicyValuationWithdrawalRateEntity policyValuationWithdrawalRateEntity =new ModelMapper().map(renewalValuationWithdrawalTMPEntity, PolicyValuationWithdrawalRateEntity.class);
		policyValuationWithdrawalRateEntity.setId(null);
		policyValuationWithdrawalRateEntity.setModifiedBy(username);
		policyValuationWithdrawalRateEntity.setIsActive(true);
		return policyValuationWithdrawalRateEntity;
		
	}
	
	public static PolicyValuationWithdrawalRateEntity createTempToValuationWithdrawalMaster(
			RenewalValuationWithdrawalTMPEntity renewalValuationWithdrawalTMPEntity, String username) {
	

		PolicyValuationWithdrawalRateEntity policyValuationWithdrawalRateEntity =new ModelMapper().map(renewalValuationWithdrawalTMPEntity, PolicyValuationWithdrawalRateEntity.class);
		policyValuationWithdrawalRateEntity.setId(null);
		policyValuationWithdrawalRateEntity.setCreatedBy(username);
		policyValuationWithdrawalRateEntity.setCreatedDate(new Date());
		policyValuationWithdrawalRateEntity.setIsActive(true);
		return policyValuationWithdrawalRateEntity;
		
	}
}
