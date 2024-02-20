package com.lic.epgs.gratuity.policy.document.service.impl;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lic.epgs.gratuity.common.dto.ApiResponseDto;
import com.lic.epgs.gratuity.policy.document.dto.PolicyDocumentDto;
import com.lic.epgs.gratuity.policy.document.entity.PolicyDocumentEntity;
import com.lic.epgs.gratuity.policy.document.helper.PolicyDocumentHelper;
import com.lic.epgs.gratuity.policy.document.repository.PolicyDocumentRepository;
import com.lic.epgs.gratuity.policy.document.service.PolicyDocumentService;
import com.lic.epgs.gratuity.quotation.document.dto.DocumentDto;
import com.lic.epgs.gratuity.quotation.document.entity.DocumentEntity;
import com.lic.epgs.gratuity.quotation.document.entity.MasterDocumentEntity;
import com.lic.epgs.gratuity.quotation.document.helper.DocumentHelper;

import io.swagger.v3.oas.annotations.servers.Server;

@Service
public class PolicyDocumentServiceImpl implements PolicyDocumentService {

	@Autowired
	private PolicyDocumentRepository policyDocumentRepository;

	@Override
	public ApiResponseDto<List<PolicyDocumentDto>> create(PolicyDocumentDto policyDocumentDto) {
		PolicyDocumentEntity policyDocumentEntity = PolicyDocumentHelper.dtoToEntity(policyDocumentDto);
		policyDocumentEntity.setIsActive(true);
		policyDocumentEntity.setCreatedBy(policyDocumentDto.getCreatedBy());
		policyDocumentEntity.setCreatedDate(new Date());
		policyDocumentRepository.save(policyDocumentEntity);
		return ApiResponseDto.created(policyDocumentRepository.findBypolicyId(policyDocumentDto.getPolicyId()).stream()
				.map(PolicyDocumentHelper::entityToDto).collect(Collectors.toList()));
	}

	@Override
	public ApiResponseDto<List<PolicyDocumentDto>> update(Long id, PolicyDocumentDto policyDocumentDto) {
		PolicyDocumentEntity policyDocumentEntity = policyDocumentRepository.findById(id).get();

		policyDocumentEntity.setDocumentStatus(policyDocumentDto.getDocumentStatus());
		policyDocumentEntity.setModifiedBy(policyDocumentDto.getModifiedBy());
		policyDocumentEntity.setModifiedDate(new Date());

		policyDocumentRepository.save(policyDocumentEntity);

		return ApiResponseDto.success(policyDocumentRepository.findBypolicyId(policyDocumentDto.getPolicyId()).stream()
				.map(PolicyDocumentHelper::entityToDto).collect(Collectors.toList()));
	}

	@Override
	public ApiResponseDto<List<PolicyDocumentDto>> findByPolicyId(Long policyId, String type) {
		if (type.equals("INPROGRESS")) {
			List<PolicyDocumentEntity> entities = policyDocumentRepository.findBypolicyId(policyId);
			if (entities.isEmpty()) {
				return ApiResponseDto.notFound(Collections.emptyList());
			} else {
				return ApiResponseDto
						.success(entities.stream().map(PolicyDocumentHelper::entityToDto).collect(Collectors.toList()));
			}
		} else {

			return ApiResponseDto.notFound(Collections.emptyList());

		}

	}

	@Override
	public ApiResponseDto<List<PolicyDocumentDto>> delete(Long id) {

		PolicyDocumentEntity policyDocumentEntity = policyDocumentRepository.findById(id).get();

		policyDocumentEntity.setIsDeleted(true);
		policyDocumentEntity.setModifiedDate(new Date());
		policyDocumentRepository.save(policyDocumentEntity);

		return ApiResponseDto.success(policyDocumentRepository.findBypolicyId(policyDocumentEntity.getPolicyId())
				.stream().map(PolicyDocumentHelper::entityToDto).collect(Collectors.toList()));
	}

}
