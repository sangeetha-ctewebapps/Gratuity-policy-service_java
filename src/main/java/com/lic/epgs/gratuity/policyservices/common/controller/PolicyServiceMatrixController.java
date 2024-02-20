package com.lic.epgs.gratuity.policyservices.common.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.lic.epgs.gratuity.policyservices.common.dto.PolicyServiceCommonResponseDto;
import com.lic.epgs.gratuity.policyservices.common.service.impl.PolicyServiceMatrixService;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/policyServiceMatrix")
public class PolicyServiceMatrixController {
	
	@Autowired
	PolicyServiceMatrixService policyServiceMatrixService;

	@GetMapping("/getMatrix")
	public ResponseEntity<?> getServiceDetailsByServiceId() {
		return new ResponseEntity<>(policyServiceMatrixService.getMatrix(), HttpStatus.OK);
			}
	
	@GetMapping("/getActiveServiceDetails/{policyNumber}/{currentService}")
	public ResponseEntity<?> getActiveServiceDetailsByPolicy(@PathVariable("policyNumber") String policyNumber, @PathVariable("currentService") String currentService) {
		return new ResponseEntity<>(policyServiceMatrixService.getActiveServiceDetailsByPolicy(policyNumber,currentService), HttpStatus.OK);
	}
}