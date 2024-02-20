package com.lic.epgs.gratuity.policy.renewalpolicy.gratuitybenefit.helper;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;

import com.lic.epgs.gratuity.common.entity.MemberCategoryEntity;
import com.lic.epgs.gratuity.policy.gratuitybenefit.dto.PolicyGratuityBenefitDto;
import com.lic.epgs.gratuity.policy.gratuitybenefit.dto.PolicyGratuityBenefitPropsDto;
import com.lic.epgs.gratuity.policy.gratuitybenefit.entity.HistoryGratuityBenefitEntity;
import com.lic.epgs.gratuity.policy.gratuitybenefit.entity.HistoryGratuityBenefitPropsEntity;
import com.lic.epgs.gratuity.policy.gratuitybenefit.entity.PolicyGratuityBenefitEntity;
import com.lic.epgs.gratuity.policy.gratuitybenefit.entity.PolicyGratuityBenefitPropsEntity;
import com.lic.epgs.gratuity.policy.gratuitybenefit.entity.StagingPolicyGratuityBenefitEntity;
import com.lic.epgs.gratuity.policy.gratuitybenefit.entity.StagingPolicyGratuityBenefitPropsEntity;
import com.lic.epgs.gratuity.policy.gratuitybenefit.helper.PolicyGratuityBenefitHelper;
import com.lic.epgs.gratuity.policy.renewalpolicy.gratuitybenefit.dto.RenewalGratuityBenefitPropsTMPDto;
import com.lic.epgs.gratuity.policy.renewalpolicy.gratuitybenefit.dto.RenewalGratuityBenefitTMPDto;
import com.lic.epgs.gratuity.policy.renewalpolicy.gratuitybenefit.entity.RenewalGratuityBenefitTMPEntity;
import com.lic.epgs.gratuity.policy.renewalpolicy.gratuitybenefit.entity.RenewalsGratuityBenefitPropsTMPEntity;
import com.lic.epgs.gratuity.policy.renewalpolicy.schemerule.dto.RenewalLifeCoverAndGratuityDto;
import com.lic.epgs.gratuity.policy.valuationmatrix.entity.PolicyValuationWithdrawalRateEntity;
import com.lic.epgs.gratuity.quotation.gratuitybenefit.dto.GratuityBenefitDto;
import com.lic.epgs.gratuity.quotation.gratuitybenefit.entity.GratuityBenefitEntity;
import com.lic.epgs.gratuity.quotation.gratuitybenefit.helper.GratuityBenefitHelper;

public class RenewalGratuityBenefitTMPHelper {

	public static RenewalGratuityBenefitTMPEntity pmsttoTmp(PolicyGratuityBenefitEntity getpolicyGratuityBenefitEntity,
			Long policyId, Long memberId) {
		RenewalGratuityBenefitTMPDto renewalGratuityBenefitTMPDto = new ModelMapper()
				.map(getpolicyGratuityBenefitEntity, RenewalGratuityBenefitTMPDto.class);
		renewalGratuityBenefitTMPDto.setId(null);
		renewalGratuityBenefitTMPDto.setCategoryId(memberId);
		
		RenewalGratuityBenefitTMPEntity renewalGratuityBenefitTMPEntity = new ModelMapper()
				.map(renewalGratuityBenefitTMPDto, RenewalGratuityBenefitTMPEntity.class);
		Set<RenewalsGratuityBenefitPropsTMPEntity> renewalsGratuityBenefitPropsTMPEntity = new HashSet<RenewalsGratuityBenefitPropsTMPEntity>();
		
		for (PolicyGratuityBenefitPropsEntity policyGratuityBenefitPropsEntity : getpolicyGratuityBenefitEntity
				.getGratuityBenefits()) {
		
			RenewalsGratuityBenefitPropsTMPEntity newrenewalsGratuityBenefitPropsTMPEntity = RenewalGratuityBenefitTMPHelper
					.copyMasterGradBenefProsEntityToTmpPolicyEntity(policyGratuityBenefitPropsEntity);
			
			newrenewalsGratuityBenefitPropsTMPEntity
					.setRenewalGratuityBenefitTMPEntity(renewalGratuityBenefitTMPEntity);
			
			renewalsGratuityBenefitPropsTMPEntity.add(newrenewalsGratuityBenefitPropsTMPEntity);
		}
		
		renewalGratuityBenefitTMPEntity.setRenewalgratuityPropsBenefit(renewalsGratuityBenefitPropsTMPEntity);
		renewalGratuityBenefitTMPEntity.setTmpPolicyId(policyId);
		renewalGratuityBenefitTMPEntity.setPmstGratutiyBenefitId(getpolicyGratuityBenefitEntity.getId());
		renewalGratuityBenefitTMPEntity.setIsActive(true);
		return renewalGratuityBenefitTMPEntity;
	}

	private static RenewalsGratuityBenefitPropsTMPEntity copyMasterGradBenefProsEntityToTmpPolicyEntity(
			PolicyGratuityBenefitPropsEntity policyGratuityBenefitPropsEntity) {
		RenewalGratuityBenefitPropsTMPDto policyGratuityBenefitPropsDto = new ModelMapper()
				.map(policyGratuityBenefitPropsEntity, RenewalGratuityBenefitPropsTMPDto.class);
		policyGratuityBenefitPropsDto.setId(null);
		RenewalsGratuityBenefitPropsTMPEntity renewalsGratuityBenefitPropsTMPEntity = new ModelMapper()
				.map(policyGratuityBenefitPropsDto, RenewalsGratuityBenefitPropsTMPEntity.class);
//		renewalsGratuityBenefitPropsTMPEntity.setIsActive(true);
		renewalsGratuityBenefitPropsTMPEntity.setPmstGratuityBenefitPropsId(policyGratuityBenefitPropsEntity.getId());
		renewalsGratuityBenefitPropsTMPEntity.setCreatedDate(new Date());
		return renewalsGratuityBenefitPropsTMPEntity;
	}
	
	
	
	private static RenewalsGratuityBenefitPropsTMPEntity copyMasterGradBenefProsEntityToHisPolicyEntity(
			HistoryGratuityBenefitPropsEntity historyGratuityBenefitPropsEntity) {
		RenewalGratuityBenefitPropsTMPDto policyGratuityBenefitPropsDto = new ModelMapper()
				.map(historyGratuityBenefitPropsEntity, RenewalGratuityBenefitPropsTMPDto.class);
		policyGratuityBenefitPropsDto.setId(null);
		RenewalsGratuityBenefitPropsTMPEntity renewalsGratuityBenefitPropsTMPEntity = new ModelMapper()
				.map(policyGratuityBenefitPropsDto, RenewalsGratuityBenefitPropsTMPEntity.class);
		renewalsGratuityBenefitPropsTMPEntity.setIsActive(true);
		renewalsGratuityBenefitPropsTMPEntity.setPmstHisGratuityBenefitPropsId(historyGratuityBenefitPropsEntity.getId());
		renewalsGratuityBenefitPropsTMPEntity.setCreatedDate(new Date());
		return renewalsGratuityBenefitPropsTMPEntity;
	}


	public static RenewalsGratuityBenefitPropsTMPEntity dtoToEntity(
			RenewalGratuityBenefitPropsTMPDto gratuityBenefitPropsDto) {

		return new ModelMapper().map(gratuityBenefitPropsDto, RenewalsGratuityBenefitPropsTMPEntity.class);
	}

	public static List<RenewalGratuityBenefitTMPDto> entitesToDto(List<RenewalGratuityBenefitTMPEntity> entities) {
		List<RenewalGratuityBenefitTMPDto> addRenewalGratuityBenefitDtos = new ArrayList<RenewalGratuityBenefitTMPDto>();
		for (RenewalGratuityBenefitTMPEntity gratuityBenefitEntity1 : entities) {
			RenewalGratuityBenefitTMPDto RenewalGratuityBenefitTMPDto = RenewalGratuityBenefitTMPHelper
					.entityToDto(gratuityBenefitEntity1);
			RenewalGratuityBenefitTMPDto
					.setRenewalGraBenePropTmpDto(gratuityBenefitEntity1.getRenewalgratuityPropsBenefit().stream()
							.map(RenewalGratuityBenefitTMPHelper::entityToPropDto).collect(Collectors.toList()));
			addRenewalGratuityBenefitDtos.add(RenewalGratuityBenefitTMPDto);
		}

		return addRenewalGratuityBenefitDtos;

	}

	private static RenewalGratuityBenefitTMPDto entityToDto(RenewalGratuityBenefitTMPEntity gratuityBenefitEntity1) {

		return new ModelMapper().map(gratuityBenefitEntity1, RenewalGratuityBenefitTMPDto.class);
	}

	private static RenewalGratuityBenefitPropsTMPDto entityToPropDto(
			RenewalsGratuityBenefitPropsTMPEntity renewalsGratuityBenefitPropsTMPEntity) {

		 return new ModelMapper().map(renewalsGratuityBenefitPropsTMPEntity, RenewalGratuityBenefitPropsTMPDto.class);
	}

	public static PolicyGratuityBenefitEntity updateTempToPolicyGratuityBenefitMaster(
			RenewalGratuityBenefitTMPEntity renewalGratuityBenefitTMPEntity, String username, Long masterpolicyID) {
		
		PolicyGratuityBenefitEntity policyGratuityBenefitEntity =new ModelMapper().map(renewalGratuityBenefitTMPEntity, PolicyGratuityBenefitEntity.class);
		Set<PolicyGratuityBenefitPropsEntity> addPolicyGratuityBenefitPropsEntity=new HashSet<PolicyGratuityBenefitPropsEntity>();
		for(RenewalsGratuityBenefitPropsTMPEntity renewalsGratuityBenefitPropsTMPEntity : renewalGratuityBenefitTMPEntity.getRenewalgratuityPropsBenefit()) {
					
			PolicyGratuityBenefitPropsEntity newPolicyGratuityBenefitPropsEntity=PolicyGratuityBenefitHelper.updateTempToPolicyGratuityBenefitMaster(renewalsGratuityBenefitPropsTMPEntity,username);
			
			newPolicyGratuityBenefitPropsEntity.setGratuityBenefit(policyGratuityBenefitEntity);
			addPolicyGratuityBenefitPropsEntity.add(newPolicyGratuityBenefitPropsEntity);
			}		
		policyGratuityBenefitEntity.setGratuityBenefits(addPolicyGratuityBenefitPropsEntity);
		policyGratuityBenefitEntity.setId(renewalGratuityBenefitTMPEntity.getPmstGratutiyBenefitId());
		policyGratuityBenefitEntity.setModifiedBy(username);
		policyGratuityBenefitEntity.setPolicyId(masterpolicyID);
		policyGratuityBenefitEntity.setIsActive(true);
		return policyGratuityBenefitEntity;
		
	}

	public static RenewalGratuityBenefitTMPEntity histtoTmp(
			HistoryGratuityBenefitEntity gethistoryGratuityBenefitEntity, Long tempPolicyId, Long id) {
			RenewalGratuityBenefitTMPDto renewalGratuityBenefitTMPDto = new ModelMapper()
					.map(gethistoryGratuityBenefitEntity, RenewalGratuityBenefitTMPDto.class);
			renewalGratuityBenefitTMPDto.setId(null);
			renewalGratuityBenefitTMPDto.setCategoryId(id);
			RenewalGratuityBenefitTMPEntity renewalGratuityBenefitTMPEntity = new ModelMapper()
					.map(renewalGratuityBenefitTMPDto, RenewalGratuityBenefitTMPEntity.class);
			Set<RenewalsGratuityBenefitPropsTMPEntity> renewalsGratuityBenefitPropsTMPEntity = new HashSet<RenewalsGratuityBenefitPropsTMPEntity>();
			for (HistoryGratuityBenefitPropsEntity historyGratuityBenefitPropsEntity :gethistoryGratuityBenefitEntity.getGratuityBenefits()) {
				RenewalsGratuityBenefitPropsTMPEntity newrenewalsGratuityBenefitPropsTMPEntity = RenewalGratuityBenefitTMPHelper
						.copyMasterGradBenefProsEntityToHisPolicyEntity(historyGratuityBenefitPropsEntity);
				newrenewalsGratuityBenefitPropsTMPEntity
						.setRenewalGratuityBenefitTMPEntity(renewalGratuityBenefitTMPEntity);
				renewalsGratuityBenefitPropsTMPEntity.add(newrenewalsGratuityBenefitPropsTMPEntity);
			}
			renewalGratuityBenefitTMPEntity.setRenewalgratuityPropsBenefit(renewalsGratuityBenefitPropsTMPEntity);
			renewalGratuityBenefitTMPEntity.setTmpPolicyId(tempPolicyId);
			renewalGratuityBenefitTMPEntity.setPmstHisGratutiyBenefitId(gethistoryGratuityBenefitEntity.getId());
			renewalGratuityBenefitTMPEntity.setIsActive(true);
			return renewalGratuityBenefitTMPEntity;
		}
	
	
	public static List<PolicyGratuityBenefitEntity> updateTempTocreatePolicyGratuityBenefitMaster(
			List<RenewalGratuityBenefitTMPEntity> renewalGratuityBenefitTMPEntity, String username, Long masterpolicyID,
			List<MemberCategoryEntity> addMemberCategoryEntity, List<MemberCategoryEntity> oldmemberCategoryEntity) {
		 List<PolicyGratuityBenefitEntity> newPolicyGratuityBenefitEntity =new ArrayList<PolicyGratuityBenefitEntity>();
		
		 for(RenewalGratuityBenefitTMPEntity getRenewalGratuityBenefitTMPEntity :renewalGratuityBenefitTMPEntity) {
		PolicyGratuityBenefitEntity policyGratuityBenefitEntity =new ModelMapper().map(getRenewalGratuityBenefitTMPEntity, PolicyGratuityBenefitEntity.class);
		Set<PolicyGratuityBenefitPropsEntity> addPolicyGratuityBenefitPropsEntity=new HashSet<PolicyGratuityBenefitPropsEntity>();
		for(RenewalsGratuityBenefitPropsTMPEntity renewalsGratuityBenefitPropsTMPEntity : getRenewalGratuityBenefitTMPEntity.getRenewalgratuityPropsBenefit()) {
					
			PolicyGratuityBenefitPropsEntity newPolicyGratuityBenefitPropsEntity=PolicyGratuityBenefitHelper.createTempToPolicyGratuityBenefitMaster(renewalsGratuityBenefitPropsTMPEntity,username);
			addPolicyGratuityBenefitPropsEntity.add(newPolicyGratuityBenefitPropsEntity);
			}		
		policyGratuityBenefitEntity.setId(null);
		policyGratuityBenefitEntity.setCreatedBy(username);
		policyGratuityBenefitEntity.setCreatedDate(new Date());
		policyGratuityBenefitEntity.setPolicyId(masterpolicyID);
		policyGratuityBenefitEntity.setIsActive(true);
		
		for (MemberCategoryEntity getMemberCategoryEntity : oldmemberCategoryEntity) {
			for (MemberCategoryEntity getnewMemberCategoryEntity : addMemberCategoryEntity) {
				if (getMemberCategoryEntity.getId().equals(getRenewalGratuityBenefitTMPEntity.getCategoryId())) {
				if (getMemberCategoryEntity.getName().equals(getnewMemberCategoryEntity.getName())) {
					policyGratuityBenefitEntity.setCategoryId(getnewMemberCategoryEntity.getId());
				}
				}
			}
		}
		
		newPolicyGratuityBenefitEntity.add(policyGratuityBenefitEntity);
		
		}
		return newPolicyGratuityBenefitEntity;
		
	}


		

}


