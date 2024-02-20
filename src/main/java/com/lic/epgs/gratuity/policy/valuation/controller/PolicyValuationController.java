package com.lic.epgs.gratuity.policy.valuation.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lic.epgs.gratuity.common.dto.ApiResponseDto;
import com.lic.epgs.gratuity.policy.valuation.dto.PolicyValuationDto;
import com.lic.epgs.gratuity.policy.valuation.service.PolicyValuationService;


@RestController
@CrossOrigin("*")
@RequestMapping("/api/policyvaluation")
public class PolicyValuationController {

	@Autowired
	private PolicyValuationService  policyValuationService;
	
	
	
	@GetMapping(value = "{policyid}/{type}")
	public ApiResponseDto<PolicyValuationDto> findByPolicyId(@PathVariable("policyid") Long policyid,
			@PathVariable("type") String type) {
		return policyValuationService.findByPolicyId(policyid, type);
	}
	
}
