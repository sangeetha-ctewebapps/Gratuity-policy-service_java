package com.lic.epgs.gratuity.policy.lifecover.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lic.epgs.gratuity.common.dto.ApiResponseDto;
import com.lic.epgs.gratuity.policy.lifecover.dto.PolicyLifeCoverDto;
import com.lic.epgs.gratuity.policy.lifecover.service.PolicyLifeCoverService;

/**
 * @author Ismail Khan A
 *
 */

@RestController
@CrossOrigin("*")
@RequestMapping("/api/policy/lifecover")
public class PolicyLifeCoverController {
	
	@Autowired
	private PolicyLifeCoverService policyLifeCoverService;

	@GetMapping(value = "/{policyId}/{type}")
	public ApiResponseDto<List<PolicyLifeCoverDto>> findByPolicyId(@PathVariable("policyId") Long policyId,
			@PathVariable("type") String type) {
		return policyLifeCoverService.findByPolicyId(policyId, type);
	}
}
