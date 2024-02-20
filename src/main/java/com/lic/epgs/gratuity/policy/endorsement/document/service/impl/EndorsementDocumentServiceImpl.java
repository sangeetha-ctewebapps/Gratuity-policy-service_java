package com.lic.epgs.gratuity.policy.endorsement.document.service.impl;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lic.epgs.gratuity.common.dto.ApiResponseDto;
import com.lic.epgs.gratuity.policy.endorsement.document.dto.EndorsementDocumentDto;
import com.lic.epgs.gratuity.policy.endorsement.document.entity.EndorsementDocumentEntity;
import com.lic.epgs.gratuity.policy.endorsement.document.helper.EndorsementDocumentHelper;
import com.lic.epgs.gratuity.policy.endorsement.document.repository.EndorsementDocumentRepository;
import com.lic.epgs.gratuity.policy.endorsement.document.service.EndorsementDocumentService;

@Service
public class EndorsementDocumentServiceImpl implements EndorsementDocumentService {

	@Autowired
	private EndorsementDocumentRepository endorsementDocumentRepository;
	@Override
	public ApiResponseDto<List<EndorsementDocumentDto>> create(EndorsementDocumentDto endorsementDocumentDto) {
		EndorsementDocumentEntity endorsementDocumentEntity =EndorsementDocumentHelper.dtoToEntity(endorsementDocumentDto);
		endorsementDocumentEntity.setCreatedBy(endorsementDocumentDto.getCreatedBy());
		endorsementDocumentEntity.setIsActive(true);
		endorsementDocumentEntity.setCreatedDate(new Date());
		endorsementDocumentRepository.save(endorsementDocumentEntity);
		return ApiResponseDto.created(endorsementDocumentRepository.findByTmpPolicyId(endorsementDocumentEntity.getTmpPolicyId()).stream().
				map(EndorsementDocumentHelper::entityToDto).collect(Collectors.toList()));
	}
	@Override
	public ApiResponseDto<List<EndorsementDocumentDto>> update(Long id, EndorsementDocumentDto endorsementDocumentDto) {
		EndorsementDocumentEntity endorsementDocumentEntity = endorsementDocumentRepository.findById(id).get();

		endorsementDocumentEntity.setDocumentStatus(endorsementDocumentDto.getDocumentStatus());
		endorsementDocumentEntity.setModifiedBy(endorsementDocumentDto.getModifiedBy());
		endorsementDocumentEntity.setModifiedDate(new Date());

		endorsementDocumentRepository.save(endorsementDocumentEntity);

		return ApiResponseDto.created(endorsementDocumentRepository.findByTmpPolicyId(endorsementDocumentEntity.getTmpPolicyId()).stream().
				map(EndorsementDocumentHelper::entityToDto).collect(Collectors.toList()));
	}

	@Override
	public ApiResponseDto<List<EndorsementDocumentDto>> findByTmpPolicyId(Long endorsementId) {

		List<EndorsementDocumentEntity> entities = endorsementDocumentRepository.findByTmpPolicyId(endorsementId);
		if (entities.isEmpty()) {
			return ApiResponseDto.notFound(Collections.emptyList());
		} else {
			return ApiResponseDto.success(
					entities.stream().map(EndorsementDocumentHelper::entityToDto).collect(Collectors.toList()));
		}

	}
	@Override
	public ApiResponseDto<List<EndorsementDocumentDto>> delete(Long id) {
		EndorsementDocumentEntity endorsementDocumentEntity = endorsementDocumentRepository.findById(id).get();

		endorsementDocumentEntity.setIsDeleted(true);
		endorsementDocumentEntity.setModifiedDate(new Date());
		endorsementDocumentRepository.save(endorsementDocumentEntity);

		return ApiResponseDto.success(endorsementDocumentRepository.findByTMPPolicyId(endorsementDocumentEntity.getTmpPolicyId()).stream().
				map(EndorsementDocumentHelper::entityToDto).collect(Collectors.toList()));
		
	}

}
