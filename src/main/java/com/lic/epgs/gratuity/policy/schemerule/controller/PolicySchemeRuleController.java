package com.lic.epgs.gratuity.policy.schemerule.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lic.epgs.gratuity.common.dto.ApiResponseDto;
import com.lic.epgs.gratuity.policy.schemerule.dto.PolicyLifeCoverAndGratuityDto;
import com.lic.epgs.gratuity.policy.schemerule.dto.PolicySchemeRuleDto;
import com.lic.epgs.gratuity.policy.schemerule.entity.PolicySchemeEntity;
import com.lic.epgs.gratuity.policy.schemerule.service.PolicySchemeRuleService;
import com.lic.epgs.gratuity.quotation.lifecover.dto.LifeCoverAndGratuityDto;
import com.lic.epgs.gratuity.quotation.schemerule.dto.NewSchemeRuleDto;
import com.lic.epgs.gratuity.quotation.schemerule.dto.SchemeRuleDto;



@RestController
@CrossOrigin("*")
@RequestMapping("/api/policy/schemerule")
public class PolicySchemeRuleController {

	

	@Autowired
	private PolicySchemeRuleService policySchemeRuleService;
	
	
	
	@GetMapping(value = "{policyId}/{type}")
	public ApiResponseDto<PolicySchemeRuleDto> findBypolicyId(@PathVariable("policyId") Long policyId,
			@PathVariable("type") String type) {
		return policySchemeRuleService.findBypolicyId(policyId, type);
	}
	
	@GetMapping(value = "findall/{policyId}/{type}")
	public ApiResponseDto<List<PolicyLifeCoverAndGratuityDto>> findAll(@PathVariable("policyId") Long policyId,
			@PathVariable("type") String type) {
		return policySchemeRuleService.findAll(policyId, type);
	}
}
