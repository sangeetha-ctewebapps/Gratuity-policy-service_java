package com.lic.epgs.gratuity.policy.gratuitybenefit.helper;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.ui.ModelMap;

import com.lic.epgs.gratuity.policy.gratuitybenefit.dto.PolicyGratuityBenefitDto;
import com.lic.epgs.gratuity.policy.gratuitybenefit.dto.PolicyGratuityBenefitPropsDto;
import com.lic.epgs.gratuity.policy.gratuitybenefit.entity.StagingPolicyGratuityBenefitEntity;
import com.lic.epgs.gratuity.policy.gratuitybenefit.entity.StagingPolicyGratuityBenefitPropsEntity;
import com.lic.epgs.gratuity.policy.renewalpolicy.gratuitybenefit.dto.RenewalGratuityBenefitPropsTMPDto;
import com.lic.epgs.gratuity.policy.renewalpolicy.gratuitybenefit.dto.RenewalGratuityBenefitTMPDto;
import com.lic.epgs.gratuity.policy.renewalpolicy.gratuitybenefit.entity.RenewalGratuityBenefitTMPEntity;
import com.lic.epgs.gratuity.policy.renewalpolicy.gratuitybenefit.entity.RenewalsGratuityBenefitPropsTMPEntity;
import com.lic.epgs.gratuity.policy.renewalpolicy.gratuitybenefit.helper.RenewalGratuityBenefitTMPHelper;
import com.lic.epgs.gratuity.policy.gratuitybenefit.entity.HistoryGratuityBenefitEntity;
import com.lic.epgs.gratuity.policy.gratuitybenefit.entity.HistoryGratuityBenefitPropsEntity;
import com.lic.epgs.gratuity.policy.gratuitybenefit.entity.PolicyGratuityBenefitEntity;
import com.lic.epgs.gratuity.policy.gratuitybenefit.entity.PolicyGratuityBenefitPropsEntity;
import com.lic.epgs.gratuity.quotation.gratuitybenefit.dto.GratuityBenefitDto;
import com.lic.epgs.gratuity.quotation.gratuitybenefit.dto.GratuityBenefitPropsDto;
import com.lic.epgs.gratuity.quotation.gratuitybenefit.entity.GratuityBenefitEntity;
import com.lic.epgs.gratuity.quotation.gratuitybenefit.entity.GratuityBenefitPropsEntity;
import com.lic.epgs.gratuity.quotation.gratuitybenefit.entity.MasterGratuityBenefitEntity;
import com.lic.epgs.gratuity.quotation.gratuitybenefit.entity.MasterGratuityBenefitPropsEntity;
import com.lic.epgs.gratuity.quotation.gratuitybenefit.helper.GratuityBenefitHelper;

public class PolicyGratuityBenefitHelper {
	private static PolicyGratuityBenefitDto entityToDto(PolicyGratuityBenefitEntity newPolicyGratuityBenefitEntity) {
		return new ModelMapper().map(newPolicyGratuityBenefitEntity, PolicyGratuityBenefitDto.class);
	}

	private static PolicyGratuityBenefitPropsDto entityToDto(PolicyGratuityBenefitPropsEntity policygratuitybenefitpropsentity1) {
		return new ModelMapper().map(policygratuitybenefitpropsentity1, PolicyGratuityBenefitPropsDto.class);
	}
	
	
private static PolicyGratuityBenefitDto tmpEntityToDto(StagingPolicyGratuityBenefitEntity getPLCTMPGratuityBenefitEntity) {
		
		return new ModelMapper().map(getPLCTMPGratuityBenefitEntity, PolicyGratuityBenefitDto.class);
	}

	private static PolicyGratuityBenefitPropsDto tmpentitytoDto(StagingPolicyGratuityBenefitPropsEntity plctmpgratuitybenefitpropsentity1) {
		return new ModelMapper().map(plctmpgratuitybenefitpropsentity1, PolicyGratuityBenefitPropsDto.class);
	}
	
	public static List<PolicyGratuityBenefitDto> entitesToDto(List<PolicyGratuityBenefitEntity> entities) {
	
		List<PolicyGratuityBenefitDto> listPolicyGratuityBenefit=new ArrayList<PolicyGratuityBenefitDto>();
		
		for(PolicyGratuityBenefitEntity newPolicyGratuityBenefitEntity : entities) {
			PolicyGratuityBenefitDto policyGratuityBenefitDto=PolicyGratuityBenefitHelper.entityToDto(newPolicyGratuityBenefitEntity);
			policyGratuityBenefitDto.setGratuityBenefitProps(newPolicyGratuityBenefitEntity.getGratuityBenefits().stream()
					.map(PolicyGratuityBenefitHelper::entityToDto).collect(Collectors.toList()));
			listPolicyGratuityBenefit.add(policyGratuityBenefitDto);
		}
		return listPolicyGratuityBenefit;

	}

	public static List<PolicyGratuityBenefitDto> EntitesToDto(List<StagingPolicyGratuityBenefitEntity> entities) {
	
		List<PolicyGratuityBenefitDto> addPolicyGratuityBenefitDto=new ArrayList<PolicyGratuityBenefitDto>();
		for(StagingPolicyGratuityBenefitEntity getPLCTMPGratuityBenefitEntity :entities) {
			PolicyGratuityBenefitDto newPolicyGratuityBenefitDto=PolicyGratuityBenefitHelper.tmpEntityToDto(getPLCTMPGratuityBenefitEntity);
			newPolicyGratuityBenefitDto.setGratuityBenefitProps(getPLCTMPGratuityBenefitEntity.getGratuityBenefits().
					stream().map(PolicyGratuityBenefitHelper :: tmpentitytoDto).collect(Collectors.toList()));
			addPolicyGratuityBenefitDto.add(newPolicyGratuityBenefitDto);
			
		}
		return addPolicyGratuityBenefitDto;
	}

	
	public static StagingPolicyGratuityBenefitEntity masterQuotationentityToPolicyStagingEntity(MasterGratuityBenefitEntity masterGratuityBenefitEntity,
			Long policyId) {
		PolicyGratuityBenefitDto policyGratuityBenefitDto =new ModelMapper().map(masterGratuityBenefitEntity, PolicyGratuityBenefitDto.class);
		policyGratuityBenefitDto.setId(null);
		StagingPolicyGratuityBenefitEntity stagingPolicyGratuityBenefitEntity = new ModelMapper().map(policyGratuityBenefitDto, StagingPolicyGratuityBenefitEntity.class);
		Set<StagingPolicyGratuityBenefitPropsEntity> stagingPolicyGratuityBenefitPropsEntitSet = new HashSet<StagingPolicyGratuityBenefitPropsEntity>();
		for(MasterGratuityBenefitPropsEntity masterGradBenEntity : masterGratuityBenefitEntity.getGratuityBenefits()) {
			StagingPolicyGratuityBenefitPropsEntity stagingPolicyGratuityBenefitPropsEntity = PolicyGratuityBenefitHelper.copyMasterGradBenefProsEntityToStagingPolicyEntity(masterGradBenEntity);
			stagingPolicyGratuityBenefitPropsEntity.setGratuityBenefit(stagingPolicyGratuityBenefitEntity);
			stagingPolicyGratuityBenefitPropsEntitSet.add(stagingPolicyGratuityBenefitPropsEntity);
		}
		stagingPolicyGratuityBenefitEntity.setGratuityBenefits(stagingPolicyGratuityBenefitPropsEntitSet);
		stagingPolicyGratuityBenefitEntity.setPolicyId(policyId);;
		stagingPolicyGratuityBenefitEntity.setIsActive(true);
		return stagingPolicyGratuityBenefitEntity;
	}

	private static StagingPolicyGratuityBenefitPropsEntity copyMasterGradBenefProsEntityToStagingPolicyEntity(MasterGratuityBenefitPropsEntity masterGratuityBenefitPropsEntity) {
		PolicyGratuityBenefitPropsDto policyGratuityBenefitPropsDto =new ModelMapper().map(masterGratuityBenefitPropsEntity, PolicyGratuityBenefitPropsDto.class);
		policyGratuityBenefitPropsDto.setId(null);
		StagingPolicyGratuityBenefitPropsEntity newStagingPolicyGratuityBenefitPropsEntity1 = new ModelMapper().map(policyGratuityBenefitPropsDto,StagingPolicyGratuityBenefitPropsEntity.class);
		newStagingPolicyGratuityBenefitPropsEntity1.setIsActive(true);
		newStagingPolicyGratuityBenefitPropsEntity1.setCreatedDate(new Date());
		return newStagingPolicyGratuityBenefitPropsEntity1;
	}
	

	public static PolicyGratuityBenefitEntity stgPolicyGratBenefToMasterEntity(StagingPolicyGratuityBenefitEntity stagingPolicyGratuityBenefitEntity,
			Long policyId) {
		PolicyGratuityBenefitDto policyGratuityBenefitDto =new ModelMapper().map(stagingPolicyGratuityBenefitEntity, PolicyGratuityBenefitDto.class);
		policyGratuityBenefitDto.setId(null);
		PolicyGratuityBenefitEntity policyGratuityBenefitEntity = new ModelMapper().map(policyGratuityBenefitDto, PolicyGratuityBenefitEntity.class);
		Set<PolicyGratuityBenefitPropsEntity> policyGratuityBenefitPropsEntitySet = new HashSet<PolicyGratuityBenefitPropsEntity>();
		for(StagingPolicyGratuityBenefitPropsEntity stagPolicyGratBenefPropsEntity : stagingPolicyGratuityBenefitEntity.getGratuityBenefits()) {
			PolicyGratuityBenefitPropsEntity policyGratuityBenefitPropsEntity = PolicyGratuityBenefitHelper.stgPolicyGratBenefPropsToMasterEntity(stagPolicyGratBenefPropsEntity);
			policyGratuityBenefitPropsEntity.setGratuityBenefit(policyGratuityBenefitEntity);
			policyGratuityBenefitPropsEntitySet.add(policyGratuityBenefitPropsEntity);
		}
		policyGratuityBenefitEntity.setGratuityBenefits(policyGratuityBenefitPropsEntitySet);
		policyGratuityBenefitEntity.setPolicyId(policyId);
		policyGratuityBenefitEntity.setIsActive(true);
		return policyGratuityBenefitEntity;
	}

	private static PolicyGratuityBenefitPropsEntity stgPolicyGratBenefPropsToMasterEntity(StagingPolicyGratuityBenefitPropsEntity gratuityBenefitPropsEntity) {
		PolicyGratuityBenefitPropsDto policyGratuityBenefitPropsDto =new ModelMapper().map(gratuityBenefitPropsEntity, PolicyGratuityBenefitPropsDto.class);
		policyGratuityBenefitPropsDto.setId(null);
		PolicyGratuityBenefitPropsEntity policyGratuityBenefitPropsEntity = new ModelMapper().map(policyGratuityBenefitPropsDto,PolicyGratuityBenefitPropsEntity.class);
		policyGratuityBenefitPropsEntity.setIsActive(true);
		policyGratuityBenefitPropsEntity.setCreatedDate(new Date());
		return policyGratuityBenefitPropsEntity;
	}

	public static List<PolicyGratuityBenefitDto> masterEntitesToDto(List<PolicyGratuityBenefitEntity> entities) {
		List<PolicyGratuityBenefitDto> addPolicyGratuityBenefitDto=new ArrayList<PolicyGratuityBenefitDto>();
		for(PolicyGratuityBenefitEntity getPLCTMPGratuityBenefitEntity :entities) {
			PolicyGratuityBenefitDto newPolicyGratuityBenefitDto=PolicyGratuityBenefitHelper.tmpMasterEntityToDto(getPLCTMPGratuityBenefitEntity);
			newPolicyGratuityBenefitDto.setGratuityBenefitProps(getPLCTMPGratuityBenefitEntity.getGratuityBenefits().
					stream().map(PolicyGratuityBenefitHelper :: tmpPropEntityToDto).collect(Collectors.toList()));
			addPolicyGratuityBenefitDto.add(newPolicyGratuityBenefitDto);
			
		}
		return addPolicyGratuityBenefitDto;
	}

	private static PolicyGratuityBenefitDto tmpMasterEntityToDto(
			PolicyGratuityBenefitEntity getPLCTMPGratuityBenefitEntity) {
		
		return new ModelMapper().map(getPLCTMPGratuityBenefitEntity, PolicyGratuityBenefitDto.class);
	}
	
	private static PolicyGratuityBenefitPropsDto tmpPropEntityToDto(PolicyGratuityBenefitPropsEntity policyGratuityBenefitPropsEntity) {
		return new ModelMapper().map(policyGratuityBenefitPropsEntity, PolicyGratuityBenefitPropsDto.class);
	}

	public static HistoryGratuityBenefitEntity policyMastertoHistransfer(
			PolicyGratuityBenefitEntity getpolicyGratuityBenefitEntity, Long hisPolicyID, String username) {
	
		
		HistoryGratuityBenefitEntity historyGratuityBenefitEntity = new ModelMapper()
				.map(getpolicyGratuityBenefitEntity, HistoryGratuityBenefitEntity.class);
		Set<HistoryGratuityBenefitPropsEntity> historyGratuityBenefitPropsEntity = new HashSet<HistoryGratuityBenefitPropsEntity>();
		for (PolicyGratuityBenefitPropsEntity policyGratuityBenefitPropsEntity : getpolicyGratuityBenefitEntity
				.getGratuityBenefits()) {
			HistoryGratuityBenefitPropsEntity newHistoryGratuityBenefitPropsEntity = PolicyGratuityBenefitHelper
					.copyMasterGradBenefProsEntityToHistory(policyGratuityBenefitPropsEntity);
			newHistoryGratuityBenefitPropsEntity.setGratuityBenefit(historyGratuityBenefitEntity);
					
			historyGratuityBenefitPropsEntity.add(newHistoryGratuityBenefitPropsEntity);
		}
		historyGratuityBenefitEntity.setId(null);
		historyGratuityBenefitEntity.setGratuityBenefits(historyGratuityBenefitPropsEntity);
		historyGratuityBenefitEntity.setPolicyId(hisPolicyID);
		historyGratuityBenefitEntity.setModifiedBy(username);
		historyGratuityBenefitEntity.setIsActive(true);
		return historyGratuityBenefitEntity;
	}

	private static HistoryGratuityBenefitPropsEntity copyMasterGradBenefProsEntityToHistory(
			PolicyGratuityBenefitPropsEntity policyGratuityBenefitPropsEntity) {
	
		HistoryGratuityBenefitPropsEntity historyGratuityBenefitPropsEntity = new ModelMapper()
				.map(policyGratuityBenefitPropsEntity, HistoryGratuityBenefitPropsEntity.class);
		historyGratuityBenefitPropsEntity.setId(null);
		historyGratuityBenefitPropsEntity.setIsActive(true);
		return historyGratuityBenefitPropsEntity;
	}

	public static PolicyGratuityBenefitPropsEntity updateTempToPolicyGratuityBenefitMaster(
			RenewalsGratuityBenefitPropsTMPEntity renewalsGratuityBenefitPropsTMPEntity, String username) {
		
	PolicyGratuityBenefitPropsEntity policyGratuityBenefitPropsEntity = new  ModelMapper()
				.map(renewalsGratuityBenefitPropsTMPEntity, PolicyGratuityBenefitPropsEntity.class);
	policyGratuityBenefitPropsEntity.setId(renewalsGratuityBenefitPropsTMPEntity.getPmstGratuityBenefitPropsId());
	policyGratuityBenefitPropsEntity.setModifiedBy(username);
	policyGratuityBenefitPropsEntity.setIsActive(true);
	return policyGratuityBenefitPropsEntity;
}
	
	
	public static PolicyGratuityBenefitPropsEntity createTempToPolicyGratuityBenefitMaster(
			RenewalsGratuityBenefitPropsTMPEntity renewalsGratuityBenefitPropsTMPEntity, String username) {
		
	PolicyGratuityBenefitPropsEntity policyGratuityBenefitPropsEntity = new  ModelMapper()
				.map(renewalsGratuityBenefitPropsTMPEntity, PolicyGratuityBenefitPropsEntity.class);
	policyGratuityBenefitPropsEntity.setId(null);
	policyGratuityBenefitPropsEntity.setCreatedBy(username);
	policyGratuityBenefitPropsEntity.setCreatedDate(new Date());
	policyGratuityBenefitPropsEntity.setIsActive(true);
	return policyGratuityBenefitPropsEntity;
}
	}

	

	

