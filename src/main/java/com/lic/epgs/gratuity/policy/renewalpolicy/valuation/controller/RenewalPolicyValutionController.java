package com.lic.epgs.gratuity.policy.renewalpolicy.valuation.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lic.epgs.gratuity.common.dto.ApiResponseDto;
import com.lic.epgs.gratuity.policy.renewalpolicy.dto.RenewalPolicyTMPDto;
import com.lic.epgs.gratuity.policy.renewalpolicy.valuation.dto.RenewalValuationTMPDto;
import com.lic.epgs.gratuity.policy.renewalpolicy.valuation.service.RenewalPolicyValutionService;


@RestController
@CrossOrigin("*")
@RequestMapping("/api/renewalvalution")
public class RenewalPolicyValutionController {

	@Autowired
	RenewalPolicyValutionService RenewalPolicyValutionService;
	
	@GetMapping("/getvaluation/{tmpPolicyId}")
	public ApiResponseDto<RenewalValuationTMPDto> fetchPolicyValuation(@PathVariable("tmpPolicyId") Long tmpPolicyId ) {

		return RenewalPolicyValutionService.fetchPolicyValuation(tmpPolicyId);
	}
	
	@PutMapping("/updatevalutiontemp/{id}")
	public ApiResponseDto<RenewalValuationTMPDto> updatePolicyValuation(@PathVariable(value = "id") Long id,@RequestBody RenewalValuationTMPDto renewalValuationTMPDto ) {

		return RenewalPolicyValutionService.updatePolicyValuation(id,renewalValuationTMPDto);
	}
	

	
}
