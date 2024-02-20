package com.lic.epgs.gratuity.quotation.document.service.impl;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lic.epgs.gratuity.common.dto.ApiResponseDto;
import com.lic.epgs.gratuity.quotation.document.dto.DocumentDto;
import com.lic.epgs.gratuity.quotation.document.entity.DocumentEntity;
import com.lic.epgs.gratuity.quotation.document.entity.MasterDocumentEntity;
import com.lic.epgs.gratuity.quotation.document.helper.DocumentHelper;
import com.lic.epgs.gratuity.quotation.document.repository.DocumentRepository;
import com.lic.epgs.gratuity.quotation.document.repository.MasterDocumentRepository;
import com.lic.epgs.gratuity.quotation.document.service.DocumentService;

/**
 * @author Ismail Khan A
 *
 */

@Service
public class DocumentServiceImpl implements DocumentService {

	@Autowired
	private DocumentRepository documentRepository;
	
	@Autowired
	private MasterDocumentRepository masterDocumentRepository;

	@Override
	public ApiResponseDto<List<DocumentDto>> findByQuotationId(Long quotationId, String type) {
		if (type.equals("INPROGRESS")) {
			List<DocumentEntity> entities = documentRepository.findByQuotationId(quotationId);
			if(entities.isEmpty()) {
				return ApiResponseDto.notFound(Collections.emptyList());
			} else {
				return ApiResponseDto.success(entities.stream().map(DocumentHelper::entityToDto).collect(Collectors.toList()));
						
			}
		} else {
			List<MasterDocumentEntity> entities = masterDocumentRepository.findByQuotationId(quotationId);
			if(entities.isEmpty()) {
				return ApiResponseDto.notFound(Collections.emptyList());
			} else {
				return ApiResponseDto.success(entities.stream().map(DocumentHelper::entityToDto).collect(Collectors.toList()));
						
			}
		}
	}

	@Override
	public ApiResponseDto<List<DocumentDto>> create(DocumentDto documentDto) {
		
		DocumentEntity documentEntity = DocumentHelper.dtoToEntity(documentDto);
		
		documentEntity.setIsActive(true);
		documentEntity.setCreatedBy(documentDto.getCreatedBy());
		documentEntity.setCreatedDate(new Date());
		
		documentRepository.save(documentEntity);
		
		return ApiResponseDto.created(documentRepository
				.findByQuotationId(documentEntity.getQuotationId())
				.stream().map(DocumentHelper::entityToDto).collect(Collectors.toList()));
	}
	
	@Override
	public ApiResponseDto<List<DocumentDto>> update(Long id, DocumentDto documentDto) {

		DocumentEntity documentEntity = documentRepository.findById(id).get();
		
		documentEntity.setDocumentStatus(documentDto.getDocumentStatus());
		documentEntity.setModifiedBy(documentDto.getModifiedBy());
		documentEntity.setModifiedDate(new Date());
		
		documentRepository.save(documentEntity);
		
		return ApiResponseDto.success(documentRepository
				.findByQuotationId(documentEntity.getQuotationId()).stream()
				.map(DocumentHelper::entityToDto).collect(Collectors.toList()));

	}

	@Override
	public ApiResponseDto<List<DocumentDto>> delete(Long id) {

		DocumentEntity documentEntity = documentRepository.findById(id).get();
		
		documentEntity.setIsDeleted(true);
		documentEntity.setModifiedDate(new Date());
		documentRepository.save(documentEntity);
		
		return ApiResponseDto.success(documentRepository
				.findByQuotationId(documentEntity.getQuotationId())
				.stream().map(DocumentHelper::entityToDto).collect(Collectors.toList()));
	}
}
