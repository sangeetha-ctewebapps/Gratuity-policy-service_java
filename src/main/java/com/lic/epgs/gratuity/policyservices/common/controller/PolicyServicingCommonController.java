package com.lic.epgs.gratuity.policyservices.common.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.lic.epgs.gratuity.policyservices.common.dto.PolicyServiceCommonResponseDto;
import com.lic.epgs.gratuity.policyservices.common.dto.PolicyServiceDto;
import com.lic.epgs.gratuity.policyservices.common.dto.PolicyServiceStatusResponseDto;
import com.lic.epgs.gratuity.policyservices.common.service.PolicyServicingCommonService;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/policyServicingCommon")
public class PolicyServicingCommonController {

	@Autowired
	PolicyServicingCommonService policyServicingCommonService;
	
	@PostMapping("/save")
	public PolicyServiceStatusResponseDto checkService(@RequestBody PolicyServiceDto policyServiceDto) {
		return policyServicingCommonService.savePolicyService(policyServiceDto);
	}
	
//	@GetMapping("/getServiceDetailsByServiceId")
//	public PolicyServiceCommonResponseDto getServiceDetailsByServiceId(@RequestParam Long serviceId) {
//		return policyServicingCommonService.getServiceDetailsByServiceId(serviceId);
//	}
	
//	@GetMapping("/getActiveServiceDetails/{policyNumber}/{currentService}")
//	public ResponseEntity<?> getActiveServiceDetailsByPolicy(@PathVariable("policyNumber") String policyNumber, @PathVariable("currentService") String currentService) {
//		return new ResponseEntity<>(policyServicingCommonService.getActiveServiceDetailsByPolicy(policyNumber,currentService), HttpStatus.OK);
//	}

	/*
	@PostMapping("/checkService")
	public PolicyServiceCommonResponseDto checkService(@RequestParam Long policyId, @RequestParam String serviceType) {
		return policyServicingCommonService.checkService(policyId, serviceType);
	}

	@PostMapping("/startService")
	public PolicyServiceCommonResponseDto initiateService(@RequestBody PolicyServiceDto policyServiceDto,
			@RequestParam String serviceType) {
		return policyServicingCommonService.initiateService(policyServiceDto, serviceType);
	}

	@PostMapping("/endService")
	public PolicyServiceCommonResponseDto endService(@RequestBody PolicyServiceDto policyServiceDto,
			@RequestParam String serviceType) {
		return policyServicingCommonService.endService(policyServiceDto, serviceType);
	}

	@PostMapping("/startService1")
	public PolicyServiceCommonResponseDto startService(@RequestBody PolicyServiceDto policyServiceDto,
			@RequestParam String serviceType) {
		return policyServicingCommonService.startService(policyServiceDto, serviceType);
	}

	@PostMapping("/endService1")
	public PolicyServiceCommonResponseDto endService(@RequestParam Long policyId, @RequestParam Long serviceId) {
		return policyServicingCommonService.endService1(policyId, serviceId);
	}

	@GetMapping("/getServiceDetailsByServiceId")
	public PolicyServiceCommonResponseDto getServiceDetailsByServiceId(@RequestParam Long serviceId) {
		return policyServicingCommonService.getServiceDetailsByServiceId(serviceId);
	}

	@GetMapping("/getServiceDetailsByPolicyId")
	public PolicyServiceCommonResponseDto getServiceDetailsByPolicyId(@RequestParam Long policyId) {
		return policyServicingCommonService.getServiceDetailsByPolicyId(policyId);
	}

	@PostMapping("/generateServiceId")
	public PolicyServiceCommonResponseDto generateServiceId(@RequestBody PolicyServiceDto policyServiceDto) {
		return policyServicingCommonService.generateServiceId(policyServiceDto);
	}
*/
}