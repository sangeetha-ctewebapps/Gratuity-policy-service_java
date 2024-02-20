package com.lic.epgs.gratuity.policy.valuation.helper;

import java.sql.Timestamp;
import java.util.Date;

import org.modelmapper.ModelMapper;

import com.lic.epgs.gratuity.policy.lifecover.entity.HistoryLifeCoverEntity;
import com.lic.epgs.gratuity.policy.lifecover.entity.PolicyLifeCoverEntity;
import com.lic.epgs.gratuity.policy.renewalpolicy.lifecover.entity.RenewalLifeCoverTMPEntity;
import com.lic.epgs.gratuity.policy.renewalpolicy.valuation.entity.RenewalValuationTMPEntity;
import com.lic.epgs.gratuity.policy.renewalpolicy.valuationmatrix.entity.RenewalValuationBasicTMPEntity;
import com.lic.epgs.gratuity.policy.renewalpolicy.valuationmatrix.entity.RenewalValuationMatrixTMPEntity;
import com.lic.epgs.gratuity.policy.schemerule.entity.PolicySchemeEntity;
import com.lic.epgs.gratuity.policy.schemerule.entity.PolicySchemeRuleHistoryEntity;
import com.lic.epgs.gratuity.policy.valuation.dto.PolicyValuationDto;
import com.lic.epgs.gratuity.policy.valuation.entity.PolicyMasterValuationEntity;
import com.lic.epgs.gratuity.policy.valuation.entity.PolicyValuationEntity;
import com.lic.epgs.gratuity.policy.valuation.entity.PolicyValuationHistoryEntity;
import com.lic.epgs.gratuity.policy.valuationmatrix.entity.PolicyValuationBasicHistoryEntity;
import com.lic.epgs.gratuity.policy.valuationmatrix.entity.PolicyValuationMatrixEntity;
import com.lic.epgs.gratuity.policy.valuationmatrix.entity.PolicyValutationBasicEntity;
import com.lic.epgs.gratuity.quotation.valuation.entity.MasterValuationEntity;

public class PolicyValuationHelper {

	public static PolicyValuationEntity masterQuotationentityToPolicyStagingEntity(
			MasterValuationEntity masterValuationEntity) {
		
		return new ModelMapper().map(masterValuationEntity, PolicyValuationEntity.class);
	}

	public static PolicyMasterValuationEntity policyValuationEntityToMasterEntity(
			PolicyValuationEntity policyValuationEntity) {
		return new ModelMapper().map(policyValuationEntity, PolicyMasterValuationEntity.class);
	}

	public static PolicyValuationDto entityToDto(PolicyValuationEntity policyValuationEntity) {
		return new ModelMapper().map(policyValuationEntity, PolicyValuationDto.class);
	}

	public static PolicyValuationDto entityToDto(PolicyMasterValuationEntity policyMasterValuationEntity) {
		return new ModelMapper().map(policyMasterValuationEntity, PolicyValuationDto.class);
	}

	public static PolicyMasterValuationEntity updateTempToValuationMaster(
			RenewalValuationTMPEntity renewalValuationTMPEntity, String username) {
		PolicyMasterValuationEntity policyMasterValuationEntity =new ModelMapper().map(renewalValuationTMPEntity, PolicyMasterValuationEntity.class);
		policyMasterValuationEntity.setId(renewalValuationTMPEntity.getPmstValuationId());
		policyMasterValuationEntity.setModifiedBy(username);
		policyMasterValuationEntity.setIsActive(true);
		return policyMasterValuationEntity;
	}

	public static PolicyValuationHistoryEntity policyMastertoHistransfer(PolicyMasterValuationEntity policyMasterValuationEntity, Long id,
			String username) {
		PolicyValuationHistoryEntity policyValuationHistoryEntity = new ModelMapper().map(policyMasterValuationEntity, PolicyValuationHistoryEntity.class);
		policyValuationHistoryEntity.setId(null);
		if (policyValuationHistoryEntity.getModifiedDate() == null) {
			policyValuationHistoryEntity.setPolicyId(id);			
			policyValuationHistoryEntity.setCreatedBy(username);
			policyValuationHistoryEntity.setCreatedDate(new Date());
			policyValuationHistoryEntity.setModifiedBy(null);
			policyValuationHistoryEntity.setModifiedDate(new Date());
		} else {
			Timestamp effictiveDate = new Timestamp(policyValuationHistoryEntity.getModifiedDate().getTime() + (60 * 1000L));
			policyValuationHistoryEntity.setPolicyId(id);
			policyValuationHistoryEntity.setCreatedBy(username);
			policyValuationHistoryEntity.setCreatedDate(new Date());
			policyValuationHistoryEntity.setModifiedBy(null);
			policyValuationHistoryEntity.setModifiedDate(new Date());
		}
		return policyValuationHistoryEntity;
	}
	
		




	
	}

	
	
		
	


