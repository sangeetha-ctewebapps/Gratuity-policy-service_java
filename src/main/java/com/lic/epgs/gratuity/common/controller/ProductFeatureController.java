package com.lic.epgs.gratuity.common.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lic.epgs.gratuity.common.dto.ApiResponseDto;
import com.lic.epgs.gratuity.common.dto.ProductFeatureDto;
import com.lic.epgs.gratuity.common.service.CommonService;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/productfeature")
public class ProductFeatureController {
	
	@Autowired
	CommonService commonService;

	@GetMapping("/{masterPolicyId}/{tmpPolicyId}")
	public ApiResponseDto<ProductFeatureDto> getProductFeature(
			@PathVariable("masterPolicyId") Long masterPolicyId, @PathVariable("tmpPolicyId") Long tmpPolicyId) {
		return commonService.getProductFeature(masterPolicyId, tmpPolicyId);
	}
	
	
	@GetMapping("/getallproductfeature")
	public ApiResponseDto<List<ProductFeatureDto>> getallproductfeature() {
		return commonService.getallproductfeature();
	}
	
}
