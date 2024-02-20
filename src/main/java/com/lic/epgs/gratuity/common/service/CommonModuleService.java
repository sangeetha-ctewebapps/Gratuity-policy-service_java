package com.lic.epgs.gratuity.common.service;

import com.fasterxml.jackson.databind.JsonNode;

public interface CommonModuleService {

	String getProductCode(Long productId);
	
	String getVariantCode(Long variantId);
	
	JsonNode getProposalNumber(String ProposalNumber);

	JsonNode getProductCodeJsonNode(Long productId);
	
}
