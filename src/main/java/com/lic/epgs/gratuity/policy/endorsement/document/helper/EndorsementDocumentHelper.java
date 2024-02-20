package com.lic.epgs.gratuity.policy.endorsement.document.helper;

import org.modelmapper.ModelMapper;

import com.lic.epgs.gratuity.policy.endorsement.document.dto.EndorsementDocumentDto;
import com.lic.epgs.gratuity.policy.endorsement.document.entity.EndorsementDocumentEntity;
import com.lic.epgs.gratuity.policyservices.aom.dto.DocumentUploadDto;

public class EndorsementDocumentHelper {

	public static EndorsementDocumentEntity dtoToEntity(EndorsementDocumentDto endorsementDocumentDto) {
		return new ModelMapper().map(endorsementDocumentDto, EndorsementDocumentEntity.class);
	}
	

	public static EndorsementDocumentDto entityToDto(EndorsementDocumentEntity endorsementDocumentEntity) {
		return new ModelMapper().map(endorsementDocumentEntity, EndorsementDocumentDto.class);
	}
	
	public static DocumentUploadDto entityToDocumentUploadDto(EndorsementDocumentEntity endorsementDocumentEntity) {
		return new ModelMapper().map(endorsementDocumentEntity, DocumentUploadDto.class);
	}
}
