package com.lic.epgs.gratuity.policyservices.common.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lic.epgs.gratuity.common.dto.ApiResponseDto;
import com.lic.epgs.gratuity.policy.dto.PolicyDto;
import com.lic.epgs.gratuity.policy.dto.PolicySearchDto;
import com.lic.epgs.gratuity.policyservices.common.service.impl.PolicyServicesImpl;

@RestController
@CrossOrigin("*")
@RequestMapping({ "/api/policyService" })
public class PolicyServiceController {
	
	@Autowired
	PolicyServicesImpl policyServiceImpl;

	@PostMapping(value = "/masterPolicySearch")
	public ApiResponseDto<List<PolicyDto>> masterPolicySearch(@RequestBody PolicySearchDto policySearchDto) {
		return policyServiceImpl.masterPolicySearch(policySearchDto);
	}
}
