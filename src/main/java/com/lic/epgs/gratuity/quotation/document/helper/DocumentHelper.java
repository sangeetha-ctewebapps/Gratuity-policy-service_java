package com.lic.epgs.gratuity.quotation.document.helper;

import org.modelmapper.ModelMapper;

import com.lic.epgs.gratuity.quotation.document.dto.DocumentDto;
import com.lic.epgs.gratuity.quotation.document.entity.DocumentEntity;
import com.lic.epgs.gratuity.quotation.document.entity.MasterDocumentEntity;

public class DocumentHelper {
	public static MasterDocumentEntity entityToMasterEntity(DocumentEntity entity) {
		return new ModelMapper().map(entity, MasterDocumentEntity.class);
	}
	
	public static DocumentDto entityToDto(DocumentEntity entity){
		return new ModelMapper().map(entity, DocumentDto.class);
	}
	
	public static DocumentDto entityToDto(MasterDocumentEntity entity){
		return new ModelMapper().map(entity, DocumentDto.class);
	}
	
	public static DocumentEntity dtoToEntity(DocumentDto dto) {
		return new ModelMapper().map(dto, DocumentEntity.class);
		
	}
}
