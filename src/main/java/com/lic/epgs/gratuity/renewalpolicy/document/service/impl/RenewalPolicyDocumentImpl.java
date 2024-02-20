package com.lic.epgs.gratuity.renewalpolicy.document.service.impl;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lic.epgs.gratuity.common.dto.ApiResponseDto;
import com.lic.epgs.gratuity.quotation.document.entity.DocumentEntity;
import com.lic.epgs.gratuity.quotation.document.entity.MasterDocumentEntity;
import com.lic.epgs.gratuity.quotation.document.helper.DocumentHelper;
import com.lic.epgs.gratuity.renewalpolicy.document.dto.RenewalPolicyDocumentDto;
import com.lic.epgs.gratuity.renewalpolicy.document.entity.RenewalPolicyDocumentEntity;
import com.lic.epgs.gratuity.renewalpolicy.document.helper.RenewalPolicyDocumentHelper;
import com.lic.epgs.gratuity.renewalpolicy.document.repository.RenewalPolicyDocumentRepository;
import com.lic.epgs.gratuity.renewalpolicy.document.service.RenewalPolicyDocumentService;

@Service
public class RenewalPolicyDocumentImpl implements  RenewalPolicyDocumentService {
	
	@Autowired
	private RenewalPolicyDocumentRepository renewalPolicyDocumentRepository;
	
	

	@Override
	public ApiResponseDto<List<RenewalPolicyDocumentDto>> create(RenewalPolicyDocumentDto renewalPolicyDocumentDto) 
	{			
		RenewalPolicyDocumentEntity renewalPolicyDocumentEntity = RenewalPolicyDocumentHelper.dtoToEntity(renewalPolicyDocumentDto);
		
		renewalPolicyDocumentEntity.setIsActive(true);
		renewalPolicyDocumentEntity.setCreatedBy(renewalPolicyDocumentDto.getCreatedBy());
		renewalPolicyDocumentEntity.setCreatedDate(new Date());
		
		renewalPolicyDocumentRepository.save(renewalPolicyDocumentEntity);
		
		return ApiResponseDto.created(renewalPolicyDocumentRepository.findBytmpPolicyId(renewalPolicyDocumentDto.getTmpPolicyId())
				.stream().map(RenewalPolicyDocumentHelper::entityToDto).collect(Collectors.toList()));	
	}
	
	
	@Override
	public ApiResponseDto<List<RenewalPolicyDocumentDto>> findByPolicyIdandModuleType(Long tmpPolicyId,String moduleType) 
	{
		
		
			List<RenewalPolicyDocumentEntity> renewalPolicyDocumentEntity = renewalPolicyDocumentRepository.findBytmpPolicyIdandmoduleType(tmpPolicyId,moduleType);
			if(renewalPolicyDocumentEntity.isEmpty()) {
				System.out.println("get11 inside");
				return ApiResponseDto.notFound(Collections.emptyList());
			} else {
				System.out.println("get inside");
				return ApiResponseDto.success(renewalPolicyDocumentEntity.stream().map(RenewalPolicyDocumentHelper::entityToDto).collect(Collectors.toList()));
						
			}
	
	
	}

	
	@Override
	public ApiResponseDto<List<RenewalPolicyDocumentDto>> delete(Long id) {

		RenewalPolicyDocumentEntity renewalPolicyDocumentEntity = renewalPolicyDocumentRepository.findById(id).get();
		
		renewalPolicyDocumentEntity.setIsDeleted(true);
		renewalPolicyDocumentEntity.setModifiedDate(new Date());
		renewalPolicyDocumentRepository.save(renewalPolicyDocumentEntity);
		
		return ApiResponseDto.success(renewalPolicyDocumentRepository
				.findBytmpPolicyId(renewalPolicyDocumentEntity.getTmpPolicyId())
				.stream().map(RenewalPolicyDocumentHelper::entityToDto).collect(Collectors.toList()));
	
	}

	
}
