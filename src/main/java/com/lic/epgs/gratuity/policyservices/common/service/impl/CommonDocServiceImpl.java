package com.lic.epgs.gratuity.policyservices.common.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lic.epgs.gratuity.policyservices.common.dto.CommonDocsDto;
import com.lic.epgs.gratuity.policyservices.common.dto.PolicyServiceStatusResponseDto;
import com.lic.epgs.gratuity.policyservices.common.entity.CommonDocEntity;
import com.lic.epgs.gratuity.policyservices.common.repository.CommonDocRepository;

@Service
public class CommonDocServiceImpl {

	@Autowired
	CommonDocRepository commonDocRepository;

	public PolicyServiceStatusResponseDto saveDocument(List<CommonDocEntity> commonDocsDto) {

		PolicyServiceStatusResponseDto response = new PolicyServiceStatusResponseDto();

		try {

			List<CommonDocEntity> commonDocsDtoSaved = new ArrayList<>();

			for (Iterator iterator = commonDocsDto.iterator(); iterator.hasNext();) {
				CommonDocEntity commonDocEntity = (CommonDocEntity) iterator.next();

				commonDocEntity.setCreatedOn(new Date());
				commonDocEntity.setModifiedOn(new Date());
				commonDocEntity.setIsDeleted(false);
				CommonDocEntity save = commonDocRepository.save(commonDocEntity);

				commonDocsDtoSaved.add(save);
			}

			response.setMessage("Save Successfully.");
			response.setStatus("SUCCESS");
			response.setResponseData(commonDocsDtoSaved);
			return response;
		} catch (Exception e) {
			response.setMessage("Failed to save");
			response.setStatus("FAILED");
			return response;
		}

	}

	public Object deleteDocument(Long documentId, String modifiedBy) {

		PolicyServiceStatusResponseDto response = new PolicyServiceStatusResponseDto();

		Optional<CommonDocEntity> findDocumnetById = commonDocRepository.findById(documentId);
		if (findDocumnetById.isPresent()) {
			CommonDocEntity commonDocEntity = findDocumnetById.get();
			commonDocEntity.setIsDeleted(true);
			commonDocEntity.setModifiedBy(modifiedBy);
			commonDocEntity.setModifiedOn(new Date());
			commonDocRepository.save(commonDocEntity);
			response.setMessage("Deleted successfully");
			response.setStatus("SUCCESS");
			return response;
		} else {
			response.setMessage("Documnet not found for id:-" + documentId);
			response.setStatus("NotFound");
			return response;
		}
	}

	public PolicyServiceStatusResponseDto getDocumentList(String serviceNumber) {

		PolicyServiceStatusResponseDto response = new PolicyServiceStatusResponseDto();

		try {
			List<CommonDocEntity> documnetList = commonDocRepository.findByServiceNumberAndIsDeleted(serviceNumber,
					false);
			response.setResponseData(documnetList);
			response.setStatus("SUCCESS");
			return response;
		} catch (Exception e) {
			response.setMessage(e.getMessage());
			response.setStatus("FAILED");
			return response;
		}

	}
//		List<CommonDocsDto> listOfDocumnets = new ArrayList<CommonDocsDto>();
//
//		CommonDocEntity commonDocEntity = documnetList.get(0);
//		if (documnetList.isEmpty()) {
//			response.setMessage("Documnets not found for given ServiceNumber");
//			response.setStatus("Failed to get Documnets");
//
//		} else if (commonDocEntity.getIsDeleted().equals(false)
//				|| commonDocEntity.getServiceNumber().equals(commonDocEntity.getServiceNumber())) {
//			for (CommonDocEntity documnetLists : documnetList) {
//
//				CommonDocsDto commonDocsDto = new CommonDocsDto();
//				commonDocsDto.setDocumentTypeId(documnetLists.getDocumentSubTypeId());
//				commonDocsDto.setQuotationId(documnetLists.getQuotationId());
//				commonDocsDto.setDocumentSubTypeId(documnetLists.getDocumentSubTypeId());
//				commonDocsDto.setDocumentName(documnetLists.getDocumentName());
//				commonDocsDto.setDocumentIndex(documnetLists.getDocumentIndex());
//				commonDocsDto.setDocumentIndexNo(documnetLists.getDocumentIndexNo());
//
//				commonDocsDto.setDocumentStatus(documnetLists.getDocumentStatus());
//				commonDocsDto.setFolderIndexNo(documnetLists.getFolderIndexNo());
//
//				commonDocsDto.setPolicyNumber(documnetLists.getPolicyNumber());
//				commonDocsDto.setServiceNumber(documnetLists.getServiceNumber());
//
//				commonDocsDto.setReferenceId(documnetLists.getReferenceId());
//				commonDocsDto.setReferenceServiceType(documnetLists.getReferenceServiceType());
//				commonDocsDto.setCreatedBy(documnetLists.getCreatedBy());
//				commonDocsDto.setCreatedBy(documnetLists.getCreatedBy());
//				commonDocsDto.setCreatedBy(documnetLists.getCreatedBy());
//				commonDocsDto.setCreatedBy(documnetLists.getCreatedBy());
//				commonDocsDto.setExpiryDate(documnetLists.getExpiryDate());
//				commonDocsDto.setIssuedById(documnetLists.getIssuedById());
//
//				listOfDocumnets.add(commonDocsDto);
//
//			}
//
//		}

}
