package com.lic.epgs.gratuity.renewalpolicy.document.helper;

import org.modelmapper.ModelMapper;


import com.lic.epgs.gratuity.renewalpolicy.document.dto.RenewalPolicyDocumentDto;
import com.lic.epgs.gratuity.renewalpolicy.document.entity.RenewalPolicyDocumentEntity;



public class RenewalPolicyDocumentHelper {
	
	public static RenewalPolicyDocumentDto entityToDto(RenewalPolicyDocumentEntity  renewalPolicyDocumentEntity){
		return new ModelMapper().map(renewalPolicyDocumentEntity, RenewalPolicyDocumentDto.class);
	}
	
	public static RenewalPolicyDocumentEntity dtoToEntity (RenewalPolicyDocumentDto  renewalPolicyDocumentDto) {
		return new ModelMapper().map(renewalPolicyDocumentDto, RenewalPolicyDocumentEntity.class);
		
	}

}
