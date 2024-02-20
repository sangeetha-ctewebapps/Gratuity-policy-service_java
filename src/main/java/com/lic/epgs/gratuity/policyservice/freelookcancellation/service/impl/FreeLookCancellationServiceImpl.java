package com.lic.epgs.gratuity.policyservice.freelookcancellation.service.impl;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lic.epgs.gratuity.common.constants.HttpConstants;
import com.lic.epgs.gratuity.common.dto.ApiResponseDto;
import com.lic.epgs.gratuity.common.entity.TaskAllocationEntity;
import com.lic.epgs.gratuity.common.entity.TaskProcessEntity;
import com.lic.epgs.gratuity.common.repository.TaskAllocationRepository;
import com.lic.epgs.gratuity.common.repository.TaskProcessRepository;
import com.lic.epgs.gratuity.common.service.CommonService;
import com.lic.epgs.gratuity.policy.dto.MasterPolicyContributionDetailsDto;
import com.lic.epgs.gratuity.policy.entity.MasterPolicyAdjustmentDetailEntity;
import com.lic.epgs.gratuity.policy.entity.MasterPolicyContributionDetails;
import com.lic.epgs.gratuity.policy.entity.MasterPolicyEntity;
import com.lic.epgs.gratuity.policy.helper.CommonPolicyServiceHelper;
import com.lic.epgs.gratuity.policy.member.entity.PolicyMemberEntity;
import com.lic.epgs.gratuity.policy.member.repository.PolicyMemberRepository;
import com.lic.epgs.gratuity.policy.repository.MasterPolicyContributionDetailRepository;
import com.lic.epgs.gratuity.policy.repository.MasterPolicyRepository;
import com.lic.epgs.gratuity.policyservice.freelookcancellation.dto.FreeLookCancellationDto;
import com.lic.epgs.gratuity.policyservice.freelookcancellation.entity.FreeLookCancellationEntity;
import com.lic.epgs.gratuity.policyservice.freelookcancellation.repository.FreeLookCancellationRepository;
import com.lic.epgs.gratuity.policyservice.freelookcancellation.service.FreeLookCancellationService;
import com.lic.epgs.gratuity.policyservices.common.dto.LeavingMemberDto;
import com.lic.epgs.gratuity.policyservices.common.dto.PremiumGstRefundDto;
import com.lic.epgs.gratuity.policyservices.common.service.PolicyServicingCommonService;

@Service
public class FreeLookCancellationServiceImpl implements FreeLookCancellationService{
	
	
	@Autowired
	private FreeLookCancellationRepository freeLookCancellationRepository;
	
	@Autowired
	private TaskProcessRepository taskProcessRepository;
	
	@Autowired
	private CommonService commonService;
	
	@Autowired
	private MasterPolicyContributionDetailRepository masterPolicyContributionDetailRepository;
	
	@Autowired
	private TaskAllocationRepository taskAllocationRepository;
	
	@Autowired
	private PolicyMemberRepository policyMemberRepository;
	
	@Autowired
	private PolicyServicingCommonService policyServicingCommonService;
	
	@Autowired
	private MasterPolicyRepository masterPolicyRepository;
	
	ModelMapper modelMapper=new ModelMapper();
	
	protected final Logger logger = LogManager.getLogger(getClass());

	@Override
	public ApiResponseDto<FreeLookCancellationDto> saveFreeLookCancellationDetails(
			FreeLookCancellationDto freeLookCancellationDto) {
		
		Long flcDraftStatus=584l;
		
		FreeLookCancellationEntity freeLookCancellationEntity=new FreeLookCancellationEntity();
		TaskProcessEntity taskProcessEntity=taskProcessRepository.findByProcessName("FREE LOOK CANCELLATION");
		TaskAllocationEntity taskAllocationEntity=new TaskAllocationEntity();
		
		freeLookCancellationEntity.setIsActive(freeLookCancellationDto.getIsActive());
		freeLookCancellationEntity.setFreeLookCancellationRequestNumber(commonService.getSequence(HttpConstants.FREE_LOOK_CANCELLATION));
		freeLookCancellationEntity.setFreeLookCancellationRequestDate(freeLookCancellationDto.getFreeLookCancellationRequestDate());
		freeLookCancellationEntity.setCalculateFreeLookCancellationPerHardCopy(freeLookCancellationDto.getCalculateFreeLookCancellationPerHardCopy());;
		freeLookCancellationEntity.setCreatedBy(freeLookCancellationDto.getCreatedBy());
		freeLookCancellationEntity.setPolicyId(freeLookCancellationDto.getPolicyId());
		freeLookCancellationEntity.setCreatedDate(new Date());
		//freeLookCancellationEntity.setCreditPremiumToMph(freeLookCancellationDto.getCreditPremiumToMph());
		freeLookCancellationEntity.setDebitPremiumFromMph(freeLookCancellationDto.getDebitPremiumFromMph());
		freeLookCancellationEntity.setEffectiveCancelDate(freeLookCancellationDto.getEffectiveCancelDate());
		freeLookCancellationEntity.setFreeLookStatus(flcDraftStatus.toString());
		freeLookCancellationEntity.setLifeCoverPremium(freeLookCancellationDto.getLifeCoverPremium());
		freeLookCancellationEntity.setMedicalTestExpenses(freeLookCancellationDto.getMedicalTestExpenses());
		freeLookCancellationEntity.setModifiedBy(freeLookCancellationDto.getModifiedBy());
		freeLookCancellationEntity.setModifiedDate(freeLookCancellationDto.getModifiedDate());
		freeLookCancellationEntity.setRejectionReasonCode(freeLookCancellationDto.getRejectionReasonCode());
		freeLookCancellationEntity.setRejectionRemarks(freeLookCancellationDto.getRejectionRemarks());
		freeLookCancellationEntity.setStampDuty(freeLookCancellationDto.getStampDuty());
		freeLookCancellationEntity.setTotalAccuredIntrest(freeLookCancellationDto.getTotalAccuredIntrest());
		freeLookCancellationEntity.setTotalContribution(freeLookCancellationDto.getTotalContribution());
		freeLookCancellationEntity.setTotalFundValue(freeLookCancellationDto.getTotalFundValue());
		freeLookCancellationEntity.setTotalRefundAmount(freeLookCancellationDto.getTotalRefundAmount());
		freeLookCancellationEntity.setWorkFlowStatus(freeLookCancellationDto.getWorkFlowStatus());
		freeLookCancellationEntity.setPolicyDocumentDispatchDate(freeLookCancellationDto.getPolicyDocumentDispatchDate());
		freeLookCancellationEntity.setPolicyDocumentReceivedDate(freeLookCancellationDto.getPolicyDocumentReceivedDate());
		freeLookCancellationEntity.setGst(freeLookCancellationDto.getGst());
		freeLookCancellationEntity.setDebitGstFromMph(freeLookCancellationDto.getDebitGstFromMph());
		//freeLookCancellationEntity.setCreditGstToMph(freeLookCancellationDto.getCreditGstToMph());
		freeLookCancellationRepository.save(freeLookCancellationEntity);
				
		MasterPolicyEntity masterPolicyEntity=masterPolicyRepository.findByIdAndIsActiveTrue(freeLookCancellationDto.getPolicyId());
		Double premiumRefund = 0.0;
			Double gstRefund = 0.0;
			if(freeLookCancellationDto.getCalculateFreeLookCancellationPerHardCopy()==1) {
			List<PolicyMemberEntity> memberEntity = policyMemberRepository
					.findByPolicyId(masterPolicyEntity.getId());
			PremiumGstRefundDto premiumGstRefundDto = null;
			for (PolicyMemberEntity member : memberEntity) {
				LeavingMemberDto leavingMemberDto = new LeavingMemberDto();
				leavingMemberDto.setMasterMemberId(member.getId());
				leavingMemberDto.setDateOfLeaving(freeLookCancellationDto.getFreeLookCancellationRequestDate());
				premiumGstRefundDto = policyServicingCommonService.getRefundablePremiumAndGST(
						masterPolicyEntity.getId(), leavingMemberDto);
				gstRefund += premiumGstRefundDto.getGstRefundable();
				premiumRefund += premiumGstRefundDto.getPremiumRefunable();
			}
				freeLookCancellationEntity.setCreditGstToMph(gstRefund);
	            freeLookCancellationEntity.setCreditPremiumToMph(premiumRefund);
			}else {
				List<PolicyMemberEntity> memberEntity = policyMemberRepository
						.findByPolicyId(masterPolicyEntity.getId());
				PremiumGstRefundDto premiumGstRefundDto = null;
				for (PolicyMemberEntity member : memberEntity) {
					LeavingMemberDto leavingMemberDto = new LeavingMemberDto();
					leavingMemberDto.setMasterMemberId(member.getId());
					leavingMemberDto.setDateOfLeaving(freeLookCancellationDto.getFreeLookCancellationRequestDate());
					premiumGstRefundDto = policyServicingCommonService.getRefundablePremiumAndGST(
							masterPolicyEntity.getId(), leavingMemberDto);
					gstRefund += premiumGstRefundDto.getGstRefundable();
					premiumRefund += premiumGstRefundDto.getPremiumRefunable();
				}
					freeLookCancellationEntity.setCreditGstToMph(gstRefund);
		            freeLookCancellationEntity.setCreditPremiumToMph(premiumRefund);
			}
			
		taskAllocationEntity.setRequestId(freeLookCancellationEntity.getFreeLookCancellationRequestNumber());
		taskAllocationEntity.setTaskProcessTaskPrId(taskProcessEntity.getId());
		taskAllocationEntity.setCreatedBy(freeLookCancellationEntity.getCreatedBy());
		taskAllocationEntity.setCreatedDate(new Date());
		taskAllocationEntity.setModulePrimaryId(freeLookCancellationEntity.getId());
		taskAllocationEntity.setIsActive(true);
		taskAllocationRepository.save(taskAllocationEntity);
		
		//FreeLookCancellationDto responseDto=CommonPolicyServiceHelper.entiToDto(freeLookCancellationEntity);
		
		FreeLookCancellationDto cancellationDto=modelMapper.map(freeLookCancellationEntity,FreeLookCancellationDto.class);
	    return ApiResponseDto.created(cancellationDto);
	}

	@Override
	public ApiResponseDto<FreeLookCancellationDto> getFreeLookCancellationDetails(Long policyId) {
		try {
        FreeLookCancellationEntity freeLookCancellationEntity = freeLookCancellationRepository.findByPolicyID(policyId);
        
        if (freeLookCancellationEntity == null) {
            logger.error("Policy with ID {} not found.", policyId);
        }
        
        FreeLookCancellationDto dto = modelMapper.map(freeLookCancellationEntity, FreeLookCancellationDto.class);
        return ApiResponseDto.success(dto);
		}catch(Exception e) {
			return ApiResponseDto.notFound(null);
		}
    }

	@Override
	public ApiResponseDto<FreeLookCancellationDto> updateFreeLookCancellationDetails(
			FreeLookCancellationDto freeLookCancellationDto) {
		FreeLookCancellationEntity freeLookCancellationEntity=freeLookCancellationRepository.findById(freeLookCancellationDto.getId()).get();
		
		freeLookCancellationEntity.setStampDuty(freeLookCancellationDto.getStampDuty());
		freeLookCancellationEntity.setMedicalTestExpenses(freeLookCancellationDto.getMedicalTestExpenses());
		freeLookCancellationEntity.setLifeCoverPremium(freeLookCancellationDto.getLifeCoverPremium());
		freeLookCancellationEntity.setDebitPremiumFromMph(freeLookCancellationDto.getDebitPremiumFromMph());
		freeLookCancellationEntity.setCreditPremiumToMph(freeLookCancellationDto.getCreditPremiumToMph());
		freeLookCancellationEntity.setFreeLookStatus(freeLookCancellationDto.getFreeLookStatus());
		freeLookCancellationEntity.setTotalAccuredIntrest(freeLookCancellationDto.getTotalAccuredIntrest());
		freeLookCancellationEntity.setTotalContribution(freeLookCancellationDto.getTotalContribution());
		freeLookCancellationEntity.setTotalFundValue(freeLookCancellationDto.getTotalFundValue());
		freeLookCancellationEntity.setTotalRefundAmount(freeLookCancellationDto.getTotalRefundAmount());
		freeLookCancellationEntity.setModifiedBy(freeLookCancellationDto.getModifiedBy());
		freeLookCancellationEntity.setModifiedDate(new Date());
		freeLookCancellationEntity.setIsActive(freeLookCancellationDto.getIsActive());
		freeLookCancellationEntity.setCreatedBy(freeLookCancellationDto.getCreatedBy());
		freeLookCancellationEntity.setCreatedDate(freeLookCancellationDto.getCreatedDate());
		freeLookCancellationRepository.save(freeLookCancellationEntity);
		
		FreeLookCancellationDto cancellationDto=modelMapper.map(freeLookCancellationEntity, FreeLookCancellationDto.class);
		
		return ApiResponseDto.created(cancellationDto);
	}

	@Override
	public ApiResponseDto<MasterPolicyContributionDetailsDto> getFlcPremiumDetails(Long masterPolicyId) {
		try {
		MasterPolicyContributionDetails masterPolicyContributionDetails=masterPolicyContributionDetailRepository.findBymasterPolicyIDandType(masterPolicyId);
		if (masterPolicyContributionDetails == null) {
            logger.error("Policy with ID {} not found.", masterPolicyId);
        }
		MasterPolicyContributionDetailsDto detailsDto=modelMapper.map(masterPolicyContributionDetails, MasterPolicyContributionDetailsDto.class);
		return ApiResponseDto.success(detailsDto);
		}catch(Exception e) {
			return ApiResponseDto.notFound(null);
		}
	}

	@Override
	public ApiResponseDto<List<FreeLookCancellationDto>> inProgressDetailsByPolicyNumber(String policyNumber) {
	    MasterPolicyEntity masterPolicyEntity = masterPolicyRepository.findByPolicyNumberAndIsActiveTrue(policyNumber);
	    
	    if (masterPolicyEntity != null) {
	        List<FreeLookCancellationEntity> freeLookCancellationEntities = freeLookCancellationRepository.findByPolicyId(masterPolicyEntity.getId());
	        
	        if (freeLookCancellationEntities != null && !freeLookCancellationEntities.isEmpty()) {
	            for (FreeLookCancellationEntity freeLookCancellationEntity : freeLookCancellationEntities) {
	                if ("584".equals(freeLookCancellationEntity.getFreeLookStatus()) && freeLookCancellationEntity.getIsActive() == 1) {
	                    return ApiResponseDto.success(freeLookCancellationEntities.stream()
	                            .map(CommonPolicyServiceHelper::entiToDto)
	                            .collect(Collectors.toList()));
	                }
	            }
	            return ApiResponseDto.success(Collections.emptyList());
	        } else {
	            return ApiResponseDto.success(null);
	        }
	    } else {
	        return ApiResponseDto.success(null);
	    }
	}

}
