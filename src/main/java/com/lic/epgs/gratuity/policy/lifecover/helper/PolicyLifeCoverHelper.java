package com.lic.epgs.gratuity.policy.lifecover.helper;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.modelmapper.ModelMapper;

import com.lic.epgs.gratuity.common.dto.MemberCategoryDto;
import com.lic.epgs.gratuity.common.entity.MemberCategoryEntity;
import com.lic.epgs.gratuity.policy.lifecover.dto.PolicyLifeCoverDto;
import com.lic.epgs.gratuity.policy.lifecover.entity.StagingPolicyLifeCoverEntity;
import com.lic.epgs.gratuity.policy.renewalpolicy.lifecover.entity.RenewalLifeCoverTMPEntity;
import com.lic.epgs.gratuity.policy.schemerule.entity.PolicySchemeRuleHistoryEntity;
import com.lic.epgs.gratuity.policy.valuationmatrix.entity.PolicyValuationWithdrawalRateEntity;
import com.lic.epgs.gratuity.policy.valuationmatrix.entity.PolicyValuationWithdrawalRateHistoryEntity;
import com.lic.epgs.gratuity.quotation.lifecover.entity.LifeCoverEntity;
import com.lic.epgs.gratuity.quotation.lifecover.entity.MasterLifeCoverEntity;
import com.lic.epgs.gratuity.policy.lifecover.entity.HistoryLifeCoverEntity;
import com.lic.epgs.gratuity.policy.lifecover.entity.PolicyLifeCoverEntity;

/**
 * @author Ismail Khan A
 *
 */

public class PolicyLifeCoverHelper {
	public static PolicyLifeCoverDto entityToDto(StagingPolicyLifeCoverEntity entity) {
		return new ModelMapper().map(entity, PolicyLifeCoverDto.class);
	}
	
	public static PolicyLifeCoverDto entityToDto(PolicyLifeCoverEntity entity) {
		return new ModelMapper().map(entity, PolicyLifeCoverDto.class);
	}
	
	public static StagingPolicyLifeCoverEntity dtoToEntity(PolicyLifeCoverDto dto) {
		return new ModelMapper().map(dto, StagingPolicyLifeCoverEntity.class);
	}
	
	public static PolicyLifeCoverEntity entityToMasterEntity(StagingPolicyLifeCoverEntity entity) {
		return new ModelMapper().map(entity, PolicyLifeCoverEntity.class);
	}
	
	public static StagingPolicyLifeCoverEntity masterQuotationentityToPolicyStagingEntity(MasterLifeCoverEntity entity) {
		return new ModelMapper().map(entity, StagingPolicyLifeCoverEntity.class);
	}
	
	public static PolicyLifeCoverEntity stgPolicyLifeCoverToMasterEntity(StagingPolicyLifeCoverEntity entity) {
		return new ModelMapper().map(entity, PolicyLifeCoverEntity.class);
	}

	public static HistoryLifeCoverEntity policyMastertoHistransfer(PolicyLifeCoverEntity addPolicyLifeCoverEntity, Long hisPolicyId,
			String username) {
		HistoryLifeCoverEntity historyLifeCoverEntity = new ModelMapper().map(addPolicyLifeCoverEntity, HistoryLifeCoverEntity.class);
		historyLifeCoverEntity.setId(null);
		if (historyLifeCoverEntity.getModifiedDate() == null) {
			historyLifeCoverEntity.setPolicyId(hisPolicyId);

			//			policyHistoryEntity.setEffectiveFromDate(masterPolicyEntity.getCreatedDate());
//			policyHistoryEntity.setEffectiveToDate(new Date());
			
			historyLifeCoverEntity.setCreatedBy(username);
			historyLifeCoverEntity.setCreatedDate(new Date());
			historyLifeCoverEntity.setModifiedBy(null);
			historyLifeCoverEntity.setModifiedDate(new Date());
		} else {
			Timestamp effictiveDate = new Timestamp(historyLifeCoverEntity.getModifiedDate().getTime() + (60 * 1000L));
			historyLifeCoverEntity.setPolicyId(hisPolicyId);
//			policyHistoryEntity.setEffectiveFromDate(effictiveDate);
//			policyHistoryEntity.setEffectiveToDate(new Date());
			historyLifeCoverEntity.setCreatedBy(username);
			historyLifeCoverEntity.setCreatedDate(new Date());
			historyLifeCoverEntity.setModifiedBy(null);
			historyLifeCoverEntity.setModifiedDate(new Date());
		}
		return historyLifeCoverEntity;
	}

	public static PolicyLifeCoverEntity updateTempToLifecoverMaster(
			RenewalLifeCoverTMPEntity addrenewalLifeCoverTMPEntity, String username, Long masterPolicyID) {
		PolicyLifeCoverEntity policyLifeCoverEntity = new ModelMapper().map(addrenewalLifeCoverTMPEntity, PolicyLifeCoverEntity.class);
		policyLifeCoverEntity.setId(addrenewalLifeCoverTMPEntity.getPmstLifeCoverId());
		policyLifeCoverEntity.setModifiedBy(username);
		policyLifeCoverEntity.setIsActive(true);
		policyLifeCoverEntity.setPolicyId(masterPolicyID);
		return policyLifeCoverEntity;
	}
	
	
	public static PolicyLifeCoverEntity updateTempToCreateLifecoverMaster(
			RenewalLifeCoverTMPEntity addrenewalLifeCoverTMPEntity, String username, Long masterPolicyID, List<MemberCategoryEntity> addMemberCategoryEntity, List<MemberCategoryEntity> oldmemberCategoryEntity) {
		PolicyLifeCoverEntity policyLifeCoverEntity = new ModelMapper().map(addrenewalLifeCoverTMPEntity, PolicyLifeCoverEntity.class);
		policyLifeCoverEntity.setId(addrenewalLifeCoverTMPEntity.getPmstLifeCoverId());
		policyLifeCoverEntity.setModifiedBy(username);
		policyLifeCoverEntity.setIsActive(true);
		policyLifeCoverEntity.setPolicyId(masterPolicyID);
		return policyLifeCoverEntity;
	}

	public static List<PolicyLifeCoverEntity> updateTempToCreateLifecoverMaster(
			List<RenewalLifeCoverTMPEntity> renewalLifeCoverTMPEntity, String username, Long masterPolicyID,
			List<MemberCategoryEntity> addMemberCategoryEntity, List<MemberCategoryEntity> oldmemberCategoryEntity) {
		List<PolicyLifeCoverEntity> createPolicyLifeCoverEntity = new ArrayList<PolicyLifeCoverEntity>();
		for (RenewalLifeCoverTMPEntity getrenewalLifeCoverTMPEntity : renewalLifeCoverTMPEntity) {
			PolicyLifeCoverEntity policyLifeCoverEntity = new ModelMapper().map(getrenewalLifeCoverTMPEntity,
					PolicyLifeCoverEntity.class);
			policyLifeCoverEntity.setId(null);
			policyLifeCoverEntity.setIsActive(true);
			policyLifeCoverEntity.setPolicyId(masterPolicyID);
			policyLifeCoverEntity.setCreatedDate(new Date());
			policyLifeCoverEntity.setCreatedBy(username);			
			
			for (MemberCategoryEntity getMemberCategoryEntity : oldmemberCategoryEntity) {
				for (MemberCategoryEntity getnewMemberCategoryEntity : addMemberCategoryEntity) {
					if (getMemberCategoryEntity.getId().equals(getrenewalLifeCoverTMPEntity.getCategoryId())) {
						if (getMemberCategoryEntity.getName().equals(getnewMemberCategoryEntity.getName())) {
							policyLifeCoverEntity.setCategoryId(getnewMemberCategoryEntity.getId());
						}
					}
				}
			}
			createPolicyLifeCoverEntity.add(policyLifeCoverEntity);
		}

		return createPolicyLifeCoverEntity;
	}
	
	
	
	
	
	
//	public static PolicyValuationWithdrawalRateHistoryEntity policyMastertoHistransfer(PolicyValuationWithdrawalRateEntity addPolicyValuationWithdrawalRateEntity, Long hisPolicyId,
//			String username) {
//		PolicyValuationWithdrawalRateHistoryEntity policyValuationWithdrawalRateHistoryEntity = new ModelMapper().map(addPolicyValuationWithdrawalRateEntity, PolicyValuationWithdrawalRateHistoryEntity.class);
//		if (policyValuationWithdrawalRateHistoryEntity.getModifiedDate() == null) {
//			policyValuationWithdrawalRateHistoryEntity.setPolicyId(hisPolicyId);
//			policyValuationWithdrawalRateHistoryEntity.setCreatedBy(username);
//			policyValuationWithdrawalRateHistoryEntity.setCreatedDate(new Date());
//			policyValuationWithdrawalRateHistoryEntity.setModifiedBy(null);
//			policyValuationWithdrawalRateHistoryEntity.setModifiedDate(new Date());
//		} else {
//			Timestamp effictiveDate = new Timestamp(policyValuationWithdrawalRateHistoryEntity.getModifiedDate().getTime() + (60 * 1000L));
//			policyValuationWithdrawalRateHistoryEntity.setPolicyId(hisPolicyId);
//			policyValuationWithdrawalRateHistoryEntity.setCreatedBy(username);
//			policyValuationWithdrawalRateHistoryEntity.setCreatedDate(new Date());
//			policyValuationWithdrawalRateHistoryEntity.setModifiedBy(null);
//			policyValuationWithdrawalRateHistoryEntity.setModifiedDate(new Date());
//		}
//		return policyValuationWithdrawalRateHistoryEntity;
//	}
//	
	
	
	
	
	
	
	
	
}
