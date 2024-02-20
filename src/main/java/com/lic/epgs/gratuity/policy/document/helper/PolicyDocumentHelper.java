package com.lic.epgs.gratuity.policy.document.helper;

import org.modelmapper.ModelMapper;

import com.lic.epgs.gratuity.policy.document.dto.PolicyDocumentDto;
import com.lic.epgs.gratuity.policy.document.entity.PolicyDocumentEntity;

public class PolicyDocumentHelper {

	public static PolicyDocumentEntity dtoToEntity(PolicyDocumentDto policyDocumentDto) {
		return new ModelMapper().map(policyDocumentDto, PolicyDocumentEntity.class);
	}
	public static PolicyDocumentDto entityToDto(PolicyDocumentEntity policyDocumentEntity) {
		return new ModelMapper().map(policyDocumentEntity, PolicyDocumentDto.class);
	}
}
