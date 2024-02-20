package com.lic.epgs.gratuity.policy.gratuitybenefit.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lic.epgs.gratuity.common.dto.ApiResponseDto;
import com.lic.epgs.gratuity.policy.gratuitybenefit.dto.PolicyGratuityBenefitDto;
import com.lic.epgs.gratuity.policy.gratuitybenefit.service.PolicyGratuityBenefitService;
import com.lic.epgs.gratuity.quotation.gratuitybenefit.dto.GratuityBenefitDto;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/policy/gratuitybenefit")
public class PolicyGratuityBenefitController {
	

	
	@Autowired
	private PolicyGratuityBenefitService policyGratuityBenefitService;
	
	@GetMapping(value = "/{policyId}/{type}")
	public ApiResponseDto<List<PolicyGratuityBenefitDto>> findBypolicyId(@PathVariable("policyId") Long policyId,
			@PathVariable("type") String type) {
		return policyGratuityBenefitService.findBypolicyId(policyId, type);
	}

}
