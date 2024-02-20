package com.lic.epgs.gratuity.policy.endorsement.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lic.epgs.gratuity.common.dto.ApiResponseDto;
import com.lic.epgs.gratuity.policy.endorsement.dto.EndorsementDto;
import com.lic.epgs.gratuity.policy.endorsement.entity.EndorsementEntity;
import com.lic.epgs.gratuity.policy.endorsement.helper.EndorsementHelper;
import com.lic.epgs.gratuity.policy.endorsement.repository.EndorsementRepository;
import com.lic.epgs.gratuity.policy.endorsement.service.EndorsementService;
import com.lic.epgs.gratuity.policy.entity.MasterPolicyEntity;
import com.lic.epgs.gratuity.policy.helper.PolicyHelper;
import com.lic.epgs.gratuity.policy.member.entity.HistoryMemberEntity;
import com.lic.epgs.gratuity.policy.member.entity.PolicyMemberEntity;
import com.lic.epgs.gratuity.policy.member.entity.TempMemberEntity;
import com.lic.epgs.gratuity.policy.member.repository.HistoryMemberRepository;
import com.lic.epgs.gratuity.policy.member.repository.PolicyMemberRepository;
import com.lic.epgs.gratuity.policy.member.repository.TempMemberRepository;
import com.lic.epgs.gratuity.quotation.helper.QuotationHelper;
import com.lic.epgs.gratuity.quotation.member.helper.MemberHelper;

@Service
public class EndorsementServiceImpl implements EndorsementService {
	private static Long endorsementStatusId = (long) 180;
	private static Long endorsementSubStatusId =(long) 160;
	private static Long endorsementStatusIdSendBacktoMaker=(long)159;
	private static Long endorsementSubStatusIdSendBacktoMaker=(long)161;
	private static Long endorsementStatusIDSubmitforapproval=(long)181;
	private static Long endorsementStatusIDReject=(long)158;
	private static Long endorsementSubStatusIDReject=(long)163;
	private static Long endorsementStatusIDApprove=(long)157;
	private static Long endorsementSubStatusIDApprove=(long)162;
	@Autowired
	private EndorsementRepository endorsementRepository;
	
	@Autowired
	private TempMemberRepository tempMemberRepository;
	
	@Autowired
	private PolicyMemberRepository policyMemberRepository;
	
	@Autowired
	private HistoryMemberRepository historyMemberRepository;

	@Override
	public ApiResponseDto<List<EndorsementDto>> findBypolicyId(Long policyId) {
		List<EndorsementEntity> endorsementEntities = endorsementRepository.findBypolicyId(policyId);
		List<EndorsementDto> endorsementDts = new ArrayList<EndorsementDto>();

		if (endorsementEntities.size() > 0) {
			for (EndorsementEntity endorsementEntity : endorsementEntities) {
				EndorsementDto endorsementDto = EndorsementHelper.entityToDto(endorsementEntity);
				endorsementDto.setIsInitiated(endorsementRepository.isInitiated(endorsementDto.getId()).isPresent());
				if(endorsementDto.getIsInitiated() != false)
				endorsementDts.add(endorsementDto);
			}	
			
			if (endorsementDts.size() > 0) 
			return ApiResponseDto.success(endorsementDts);
			else
				return ApiResponseDto.notFound(null);
		} else {
			return ApiResponseDto.notFound(null);
		}

	}

	@Override
	public ApiResponseDto<EndorsementDto> create(long policyId, EndorsementDto endorsementDto) {

		Long nextQuotationNumber = EndorsementHelper.nextServiceNumber(endorsementRepository.maxServiceNumber());
		EndorsementEntity endorsementEntity = new EndorsementEntity();
		endorsementEntity.setEndorsementNumber(endorsementDto.getEndorsementNumber());
		endorsementEntity.setPolicyId(policyId);
		endorsementEntity.setEndorsementStatusId(endorsementStatusId);
		endorsementEntity.setEndorsementSubStatusId(endorsementSubStatusId);
		endorsementEntity.setRequestDate(endorsementDto.getRequestDate());
		endorsementEntity.setRequestReceivedFrom(endorsementDto.getRequestReceivedFrom());
		endorsementEntity.setEndorsementTypeId(endorsementDto.getEndorsementTypeId());
		endorsementEntity.setEndorsementSubtypeId(endorsementDto.getEndorsementSubtypeId());
		endorsementEntity.setServiceTypeId(endorsementDto.getServiceTypeId());
		endorsementEntity.setEffectiveTypeId(endorsementDto.getEffectiveTypeId());
		endorsementEntity.setEffectiveDate(endorsementDto.getEffectiveDate());
		endorsementEntity.setServiceNumber(nextQuotationNumber.toString());
		endorsementEntity.setCommunicationOptionId(endorsementDto.getCommunicationOptionId());
		endorsementEntity.setIsActive(true);
		endorsementEntity.setCreatedBy(endorsementDto.getCreatedBy());
		endorsementEntity.setCreatedDate(new Date());

		EndorsementEntity newEndorsementEntity = endorsementRepository.save(endorsementEntity);
		EndorsementDto endorsement = EndorsementHelper.entityToDto(newEndorsementEntity);
		endorsement.setIsInitiated(false);
		return ApiResponseDto.created(endorsement);
	}

	@Override
	public ApiResponseDto<EndorsementDto> update(long id, EndorsementDto endorsementDto) {
		EndorsementEntity endorsementEntity = endorsementRepository.findById(id).get();
	
		endorsementEntity.setEndorsementTypeId(endorsementDto.getEndorsementTypeId());
		endorsementEntity.setEndorsementSubtypeId(endorsementDto.getEndorsementSubtypeId());
		endorsementEntity.setServiceTypeId(endorsementDto.getServiceTypeId());
		endorsementEntity.setEffectiveTypeId(endorsementDto.getEffectiveTypeId());
		endorsementEntity.setEffectiveDate(endorsementDto.getEffectiveDate());
		endorsementEntity.setCommunicationOptionId(endorsementDto.getCommunicationOptionId());
		endorsementEntity.setRequestDate(endorsementDto.getRequestDate());
		endorsementEntity.setRequestReceivedFrom(endorsementDto.getRequestReceivedFrom());
		endorsementEntity.setIsActive(true);
		endorsementEntity.setModifiedBy(endorsementDto.getModifiedBy());
		endorsementEntity.setModifiedDate(new Date());
		EndorsementEntity newEndorsementEntity = endorsementRepository.save(endorsementEntity);
		EndorsementDto endorsement = EndorsementHelper.entityToDto(newEndorsementEntity);
		endorsement.setIsInitiated(false);
		return ApiResponseDto.success(endorsement);
	}

	@Override
	public ApiResponseDto<EndorsementDto> submitForApproval(Long id, String username) {

		EndorsementEntity endorsementEntity = endorsementRepository.findById(id).get();

		endorsementEntity.setEndorsementStatusId(endorsementStatusIDSubmitforapproval);
		endorsementEntity.setEndorsementSubStatusId(endorsementSubStatusId);
		endorsementEntity.setModifiedBy(username);
		endorsementEntity.setModifiedDate(new Date());
		return ApiResponseDto.success(EndorsementHelper.entityToDto(endorsementRepository.save(endorsementEntity)));
	}

	public ApiResponseDto<EndorsementDto> sendBackToMaker(Long id, String username) {

		EndorsementEntity endorsementEntity = endorsementRepository.findById(id).get();
		endorsementEntity.setEndorsementStatusId(endorsementStatusIdSendBacktoMaker);
		endorsementEntity.setEndorsementSubStatusId(endorsementSubStatusIdSendBacktoMaker);
		endorsementEntity.setModifiedBy(username);
		endorsementEntity.setModifiedDate(new Date());
		return ApiResponseDto.success(EndorsementHelper.entityToDto(endorsementRepository.save(endorsementEntity)));
	}

	@Override
	public ApiResponseDto<EndorsementDto> reject(Long id, String username) {
		EndorsementEntity endorsementEntity = endorsementRepository.findById(id).get();
		endorsementEntity.setEndorsementStatusId(endorsementStatusIDReject);
		endorsementEntity.setEndorsementSubStatusId(endorsementSubStatusIDReject);
		endorsementEntity.setIsActive(false);
		endorsementEntity.setModifiedBy(username);
		endorsementEntity.setModifiedDate(new Date());
		return ApiResponseDto.success(EndorsementHelper.entityToDto(endorsementRepository.save(endorsementEntity)));
	}

	@Transactional
	@Override
	public ApiResponseDto<EndorsementDto> approve(Long id, String username) {
		EndorsementEntity endorsementEntity = endorsementRepository.findById(id).get();
		endorsementEntity.setEndorsementStatusId(endorsementStatusIDApprove);
		endorsementEntity.setEndorsementSubStatusId(endorsementSubStatusIDApprove);
		endorsementEntity.setIsActive(false);
		endorsementEntity.setModifiedBy(username);
		endorsementEntity.setModifiedDate(new Date());

		if (endorsementEntity.getEndorsementTypeId().equals(165L)) {
			PolicyMemberEntity policyMemberEntity = copyMemberMastertoHistory(id, username);
		}
		return ApiResponseDto.success(EndorsementHelper.entityToDto(endorsementRepository.save(endorsementEntity)));
	}

	private PolicyMemberEntity copyMemberMastertoHistory(Long id, String username) {
		PolicyMemberEntity policyMemberEntity = null;
	
		List<TempMemberEntity> tempMemberEntity = tempMemberRepository.findByendorsementId(id);

		for (TempMemberEntity newTempMemberEntity : tempMemberEntity) {
			policyMemberEntity = policyMemberRepository.findById(newTempMemberEntity.getPmstMemberId()).get();

			HistoryMemberEntity historyMemberEntity = historyMemberRepository
					.save(MemberHelper.policymastertohistoryupdate(policyMemberEntity, username));
			policyMemberEntity=EndorsementHelper.updateTemptoPolicyMemberMaster(newTempMemberEntity, username);
			policyMemberRepository.save(policyMemberEntity);
		}
		return policyMemberEntity;
	}

}
