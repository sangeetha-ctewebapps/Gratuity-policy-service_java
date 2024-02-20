package com.lic.epgs.gratuity.common.service;

import java.util.List;

import com.lic.epgs.gratuity.common.dto.ApiResponseDto;
import com.lic.epgs.gratuity.common.dto.ProductFeatureDto;

public interface CommonService {

	public String getSequence(String type);

	public ApiResponseDto<ProductFeatureDto> getProductFeature(Long productId, Long variantId);

	public ApiResponseDto<List<ProductFeatureDto>> getallproductfeature();
	
}
