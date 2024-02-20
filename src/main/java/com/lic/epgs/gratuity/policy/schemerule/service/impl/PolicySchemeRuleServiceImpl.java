package com.lic.epgs.gratuity.policy.schemerule.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.lic.epgs.gratuity.common.dto.ApiResponseDto;
import com.lic.epgs.gratuity.common.dto.MemberCategoryDto;
import com.lic.epgs.gratuity.common.repository.MemberCategoryRepository;
import com.lic.epgs.gratuity.policy.gratuitybenefit.dto.PolicyGratuityBenefitDto;
import com.lic.epgs.gratuity.policy.gratuitybenefit.entity.PolicyGratuityBenefitEntity;
import com.lic.epgs.gratuity.policy.gratuitybenefit.entity.StagingPolicyGratuityBenefitEntity;
import com.lic.epgs.gratuity.policy.gratuitybenefit.helper.PolicyGratuityBenefitHelper;
import com.lic.epgs.gratuity.policy.gratuitybenefit.repository.PolicyGratuityBenefitRepository;
import com.lic.epgs.gratuity.policy.gratuitybenefit.repository.StagingPolicyGratuityBenefitRepository;
import com.lic.epgs.gratuity.policy.lifecover.dto.PolicyLifeCoverDto;
import com.lic.epgs.gratuity.policy.lifecover.entity.PolicyLifeCoverEntity;
import com.lic.epgs.gratuity.policy.lifecover.entity.StagingPolicyLifeCoverEntity;
import com.lic.epgs.gratuity.policy.lifecover.helper.PolicyLifeCoverHelper;
import com.lic.epgs.gratuity.policy.lifecover.repository.PolicyLifeCoverRepository;
import com.lic.epgs.gratuity.policy.lifecover.repository.StagingPolicyLifeCoverRepository;
import com.lic.epgs.gratuity.policy.schemerule.dto.PolicyLifeCoverAndGratuityDto;
import com.lic.epgs.gratuity.policy.schemerule.dto.PolicySchemeRuleDto;
import com.lic.epgs.gratuity.policy.schemerule.entity.StagingPolicySchemeRule;
import com.lic.epgs.gratuity.policy.schemerule.entity.PolicySchemeEntity;
import com.lic.epgs.gratuity.policy.schemerule.helper.PolicySchemeRuleHelper;
import com.lic.epgs.gratuity.policy.schemerule.repository.StagingPolicySchemeRuleRepository;
import com.lic.epgs.gratuity.policy.schemerule.repository.PolicySchemeRuleRepository;
import com.lic.epgs.gratuity.policy.schemerule.service.PolicySchemeRuleService;
import com.lic.epgs.gratuity.quotation.gratuitybenefit.dto.GratuityBenefitDto;
import com.lic.epgs.gratuity.quotation.gratuitybenefit.entity.GratuityBenefitEntity;
import com.lic.epgs.gratuity.quotation.gratuitybenefit.entity.MasterGratuityBenefitEntity;
import com.lic.epgs.gratuity.quotation.gratuitybenefit.helper.GratuityBenefitHelper;
import com.lic.epgs.gratuity.quotation.lifecover.dto.LifeCoverDto;
import com.lic.epgs.gratuity.quotation.lifecover.entity.LifeCoverEntity;
import com.lic.epgs.gratuity.quotation.lifecover.entity.MasterLifeCoverEntity;
import com.lic.epgs.gratuity.quotation.lifecover.helper.LifeCoverHelper;
import com.lic.epgs.gratuity.quotation.schemerule.dto.NewSchemeRuleDto;
import com.lic.epgs.gratuity.quotation.schemerule.dto.SchemeRuleDto;
import com.lic.epgs.gratuity.quotation.schemerule.entity.SchemeRuleEntity;
import com.lic.epgs.gratuity.quotation.schemerule.helper.SchemeRuleHelper;
import com.lic.epgs.gratuity.quotation.schemerule.repository.SchemeRuleRepository;

@Service
public class PolicySchemeRuleServiceImpl implements  PolicySchemeRuleService {

@Autowired
private PolicySchemeRuleRepository policySchemeRuleRepository;

@Autowired
private StagingPolicySchemeRuleRepository plcTMPSchemeRuleRepository;

@Autowired
private StagingPolicyLifeCoverRepository stagingPolicyLifeCoverRepository;

@Autowired
private PolicyLifeCoverRepository policyLifeCoverRepository;


@Autowired
private StagingPolicyGratuityBenefitRepository stagingPolicyGratuityBenefitRepository;

@Autowired
private PolicyGratuityBenefitRepository policyGratuityBenefitRepository;

@Autowired
private MemberCategoryRepository  memberCategoryRepository;

@Override
public ApiResponseDto<PolicySchemeRuleDto> findBypolicyId(Long policyId, String type) {
	if (type.equals("INPROGRESS")) {
		Optional<StagingPolicySchemeRule> entity = plcTMPSchemeRuleRepository.findBypolicyId(policyId);
		if (entity.isPresent()) {
			return ApiResponseDto.success(PolicySchemeRuleHelper.entityToDto(entity.get()));
		} else {
			return ApiResponseDto.notFound(new PolicySchemeRuleDto());
		}
	}else {
	
		Optional<PolicySchemeEntity> entity = policySchemeRuleRepository.findBypolicyId(policyId);
		if (entity.isPresent()) {
			return ApiResponseDto.success(PolicySchemeRuleHelper.entityToDto(entity.get()));
		} else {
			return ApiResponseDto.notFound(new PolicySchemeRuleDto());
		}
	}
	
}


	@Override
	public ApiResponseDto<List<PolicyLifeCoverAndGratuityDto>> findAll(Long policyId, String type) {
		if (type.equals("INPROGRESS")) {
			List<StagingPolicyLifeCoverEntity> entity = stagingPolicyLifeCoverRepository.findByPolicyId(policyId);
			List<StagingPolicyGratuityBenefitEntity> gratuityBenefitEntity = stagingPolicyGratuityBenefitRepository.findByPolicyId(policyId);
			if (entity.size()!=0 && gratuityBenefitEntity.size()!=0) {	
				List<PolicyLifeCoverDto> lifeCoverDto =(stagingPolicyLifeCoverRepository.findByPolicyId(policyId).stream().map(PolicyLifeCoverHelper::entityToDto)
						.collect(Collectors.toList()));
				List<PolicyGratuityBenefitDto> gratuityBenefitDto=PolicyGratuityBenefitHelper.EntitesToDto(stagingPolicyGratuityBenefitRepository.findByPolicyId(policyId));	
				
				List<MemberCategoryDto> getMemberCategoryDto=(memberCategoryRepository.findBypstgPolicyId(policyId).stream().map(LifeCoverHelper::memberEntitestoDto).
						collect(Collectors.toList()));
				return ApiResponseDto.success(PolicySchemeRuleHelper.lifeandGratToDto(lifeCoverDto,getMemberCategoryDto,gratuityBenefitDto));
			} else {
				return ApiResponseDto.notFound(null);
			}
		} else {
			List<PolicyLifeCoverEntity> entity = policyLifeCoverRepository.findByPolicyId(policyId);
			List<PolicyGratuityBenefitEntity> gratuityBenefitEntity = policyGratuityBenefitRepository.findBypolicyId(policyId);
			if (entity.size()!=0 && gratuityBenefitEntity.size()!=0) {	
				List<PolicyLifeCoverDto> lifeCoverDto =(policyLifeCoverRepository.findByPolicyId(policyId).stream().map(PolicyLifeCoverHelper::entityToDto)
						.collect(Collectors.toList()));
				List<PolicyGratuityBenefitDto> gratuityBenefitDto=PolicyGratuityBenefitHelper.masterEntitesToDto(policyGratuityBenefitRepository.findBypolicyId(policyId));	
				
				List<MemberCategoryDto> getMemberCategoryDto=(memberCategoryRepository.findBypmstPolicyId(policyId).stream().map(LifeCoverHelper::memberEntitestoDto).
						collect(Collectors.toList()));
				return ApiResponseDto.success(PolicySchemeRuleHelper.lifeandGratToDto(lifeCoverDto,getMemberCategoryDto,gratuityBenefitDto));
			} else {
				return ApiResponseDto.notFound(null);
			}
		}
	}



}
